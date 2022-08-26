package com.emicalculator.tdsCalculator

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.emicalculator.*
import com.emicalculator.databinding.ActivityTdsCalculatorBinding


class TDS_Calculator : AppCompatActivity() {
    private lateinit var mBinding: ActivityTdsCalculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTdsCalculatorBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        title = "TDS Calculator"


        mBinding.tv2.text = "Financial Year : ${getFinancialYear()}"




        mBinding.isPanAvailable.text = isPanAvailable[0]
        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                R.layout.spinner_item, isPanAvailable
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    mBinding.isPanAvailable.text = isPanAvailable[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
      /*  mBinding.isPanAvailable.setOnClickListener(View.OnClickListener {
            mBinding.amount.clearFocus()
            hideKeyboard()
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.hpdialog)
            dialog.window!!.setLayout(1000, 1200)

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val editText = dialog.findViewById<EditText>(R.id.editText)
            val listView = dialog.findViewById<ListView>(R.id.list)
            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                this,
                R.layout.spinner_item, isPanAvailable
            )
            listView.adapter = arrayAdapter
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    arrayAdapter.filter.filter(charSequence)
                }

                override fun afterTextChanged(editable: Editable) {}
            })
            listView.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, view, i, l ->
                    //    textView.setText(arrayAdapter.getItem(i))
                    //   getcity = textView.getText().toString()
                    //   Toast.makeText(this, "Deduction can only be claimed by resident Indians and not NRIs", Toast.LENGTH_LONG).show()

                    mBinding.isPanAvailable.text = arrayAdapter.getItem(i)
                    dialog.dismiss()
                }
        })*/

        mBinding.natureOfPayment.text = getTDSnatureList()[0]
        mBinding.natureOfPayment.setOnClickListener(View.OnClickListener {
            mBinding.amount.clearFocus()
            hideKeyboard()
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.hpdialog)
            dialog.window!!.setLayout(1000, 1200)

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val editText = dialog.findViewById<EditText>(R.id.editText)
            val listView = dialog.findViewById<ListView>(R.id.list)
            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                this,
                R.layout.spinner_item, getTDSnatureList()
            )
            listView.adapter = arrayAdapter
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    arrayAdapter.filter.filter(charSequence)
                }

                override fun afterTextChanged(editable: Editable) {}
            })
            listView.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, view, i, l ->
                    //    textView.setText(arrayAdapter.getItem(i))
                    //   getcity = textView.getText().toString()
                    //   Toast.makeText(this, "Deduction can only be claimed by resident Indians and not NRIs", Toast.LENGTH_LONG).show()

                    mBinding.natureOfPayment.text = arrayAdapter.getItem(i)
                    dialog.dismiss()
                }
        })


        mBinding.btn1.setOnClickListener {
            mBinding.amount.clearFocus()
            hideKeyboard()
            calculateTDS()
        }


    }

    private fun calculateTDS() {
        val amount = mBinding.amount.text
        if (amount.isNullOrEmpty()) {
            mBinding.amount.apply {
                error = "Enter amount"
                requestFocus()
            }
        } else if (amount.toString().toDouble() <= 0.0 || amount.toString()
                .toDouble() > 10000000.0
        ) {
            mBinding.amount.apply {
                error = "Amount Must Be 0 - 1,00,00,000"
                requestFocus()
            }
        } else {
            mBinding.llResult.visibility = View.VISIBLE

            val isPanAvailable = if (mBinding.isPanAvailable.text.equals("Yes")) true else false
            val natureOfPayment = mBinding.natureOfPayment.text.toString()
            val isOther = if (mBinding.removeGst.isChecked) true else false
            val natureofPaymentItem =
                tds_section_list_rate.filter { it.Section == natureOfPayment }[0]

            if (amount.toString()
                    .toInt() >= natureofPaymentItem.threshold_limit.toInt() && isPanAvailable
            ) {
                if (isOther && natureofPaymentItem.Section.equals("Section 192A - Payment of accumulated PF balance to an employee")){
                    mBinding.result.text =  "0 Rs."
                }else{
                    val rateofTDS =
                        natureofPaymentItem.tds_rate.toDouble() * (amount.toString().toDouble() / 100)
                    mBinding.result.text = String.format("%.02f", rateofTDS.toDouble()) + "Rs."
                }

                mBinding.ruleOfTds.text = "${natureofPaymentItem.rule_of_tds}"
            } else if (amount.toString()
                    .toInt() >= natureofPaymentItem.threshold_limit.toInt() && isPanAvailable == false
            ) {
                if (isOther && natureofPaymentItem.Section.equals("Section 192A - Payment of accumulated PF balance to an employee")) {

                    mBinding.result.text = "0 Rs."
                }else{
                    val rateofTDS =
                        natureofPaymentItem.withoutPanRate.toDouble() * (amount.toString().toDouble() / 100)
                    mBinding.result.text = String.format("%.02f", rateofTDS.toDouble()) + "Rs."
                }

                mBinding.ruleOfTds.text =
                    "In case PAN is not available then TDS would be applicable at higher rates. ${natureofPaymentItem.rule_of_tds}"
            } else if (amount.toString().toInt() < natureofPaymentItem.threshold_limit.toInt()) {

                mBinding.result.text = "o RS"
                mBinding.ruleOfTds.text = "${natureofPaymentItem.rule_of_tds}"

            } else {
                mBinding.result.text = "We are working on it."
                mBinding.ruleOfTds.text = ""
            }

            if (mBinding.scrollView.scrollY == mBinding.scrollView.top){
                scrollDown(mBinding.scrollView)

            }
        }
    }


    fun getTDSnatureList(): List<String> {
        return tds_section_list_rate.map { it.Section }
    }
}