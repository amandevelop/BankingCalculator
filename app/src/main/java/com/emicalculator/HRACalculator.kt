package com.emicalculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emicalculator.databinding.ActivityHracalculatorBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class HRACalculator : AppCompatActivity() {
    private lateinit var mBinding: ActivityHracalculatorBinding

    var metroNonMetro = 50
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityHracalculatorBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        mBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val rb = findViewById<View>(checkedId) as RadioButton
            if (rb.text.equals("Metro City")) {
                metroNonMetro = 50
            } else {
                metroNonMetro = 40
            }
            //   Toast.makeText(this, "${rb.text}", Toast.LENGTH_SHORT).show()
        }
        mBinding.btnCaculate.setOnClickListener {
            hraCalculate()
        }


    }

    fun hraCalculate() {
        if (mBinding.basicSalary.text.isNullOrEmpty()) {
            mBinding.basicSalary.apply {
                requestFocus()
                error = "Enter your basic salary"
            }
        } else if (mBinding.hraReceived.text.isNullOrEmpty()) {
            mBinding.hraReceived.apply {
                requestFocus()
                error = "Enter hra received"
            }

        } else if (mBinding.rentPaid.text.isNullOrEmpty()) {
            mBinding.rentPaid.apply {
                requestFocus()
                error = "Enter rent paid"
            }
        } else {
            hideKeyboard( mBinding.rentPaid)
            mBinding.rentPaid.clearFocus()
            mBinding.hraReceived.clearFocus()
            mBinding.basicSalary.clearFocus()

            val basic_salary = mBinding.basicSalary.text.toString().toDouble()

            var hra_received = mBinding.hraReceived.text.toString().toDouble()
            var rent_p = mBinding.rentPaid.text.toString().toDouble()


            var step1 = hra_received.toDouble()
            var step2 = rent_p - ((basic_salary * 10) / 100)
            var step3 = (basic_salary * metroNonMetro.toDouble()) / 100



            step1 = if (step1 < 0.0) 0.0 else step1
            step2 = if (step2 < 0.0) 0.0 else step2
            step3 = if (step3 < 0.0) 0.0 else step3

            mBinding.ll8.visibility = View.VISIBLE
            Log.d("HRA Calculatopm", "hraCalculate: $step1 , $step2 , $step3")
            if (step1 < step2 && step1 < step3) {
                mBinding.maturityValue.text =
                    "Exempted HRA : " + String.format("%.02f", step1.toDouble()) + " Rs.\n" +
                            "HRA taxable : " + String.format(
                        "%.02f",
                        (hra_received - step1).toDouble()
                    ) + " Rs."
                setupPieChart(step1.toFloat(), (hra_received - step1).toFloat())
            } else if (step2 < step1 && step2 < step3) {
                mBinding.maturityValue.text =
                    "Exempted HRA : " + String.format("%.02f", step2.toDouble()) + " Rs.\n" +
                            "HRA taxable : " + String.format(
                        "%.02f",
                        (hra_received - step2).toDouble()
                    ) + " Rs."
                setupPieChart(step2.toFloat(), (hra_received - step2).toFloat())

            } else if (step3 < step1 && step3 < step2) {
                mBinding.maturityValue.text =
                    "Exempted HRA : " + String.format("%.02f", step3.toDouble()) + " Rs.\n" +
                            "HRA taxable : " + String.format(
                        "%.02f",
                        (hra_received - step3).toDouble()
                    ) + " Rs."
                setupPieChart(step3.toFloat(), (hra_received - step3).toFloat())

            } else {
                mBinding.maturityValue.text =
                    "Exempted HRA : " + String.format("%.02f", 0.toDouble()) + " Rs.\n" +
                            "HRA taxable : " + String.format("%.02f", 0.toDouble()) + " Rs."
                setupPieChart(0.toFloat(), 0.toFloat())

            }

        }
        //   $(".taxable_hra").text(c) +
        //                    "Taxable HRA : "+  String.format("%.02f", c.toDouble()) + "Rs."


    }

    private fun setupPieChart(hra_exempted: Float, hra_taxable: Float) {
        mBinding.scrollview.post {
            mBinding.scrollview.fullScroll(View.FOCUS_DOWN)
        }
        val view = mBinding
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()
        listPie.add(PieEntry(hra_exempted, "HRA exempted"))
        listColors.add(resources.getColor(R.color.app_color))
        listPie.add(PieEntry(hra_taxable, "HRA taxable"))
        listColors.add(resources.getColor(R.color.app_light_color))
        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.colors = listColors
        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(0F)
        view.pieChart.setDrawEntryLabels(false)
        view.pieChart.data = pieData
        view.pieChart.setHoleRadius(75F);
        view.pieChart.setUsePercentValues(false)
        view.pieChart.isDrawHoleEnabled = true
        view.pieChart.description.isEnabled = false
        view.pieChart.setEntryLabelColor(R.color.black)
        view.pieChart.animateY(1400, Easing.EaseInOutQuad)
    }

}
