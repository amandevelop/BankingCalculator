package com.emicalculator

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.emicalculator.databinding.ActivityMainBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.snackbar.Snackbar
import java.lang.Math.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    var isYearm = true
    var isMonthm = false

    companion object {
        var isEnable = 0

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
            .apply {
                isSelected = isEnable
                isYear = isYearm
                isMonth = isMonthm
            }
        setContentView(mBinding.root)

        title = "EMI Calculator"

        when (isEnable) {
            0 -> mBinding.etLoanAmout.isEnabled = false
            1 -> mBinding.etPeriodYear.isEnabled = false
            2 -> mBinding.etEmi.isEnabled = false
            3 -> mBinding.etInterest.isEnabled = false
            else ->{}
        }

        mBinding.apply {
            itemSwitch.cv1.setOnClickListener {
                if (isYearm) {
                    isYearm = false
                    mBinding.isYear = false
                    isMonthm = true
                    mBinding.isMonth = true
                } else {
                    isYearm = true
                    mBinding.isYear = true
                    isMonthm = false
                    mBinding.isMonth = false
                }
            }
            itemSwitch.cv2.setOnClickListener {
                if (isMonthm) {
                    isYearm = true
                    mBinding.isYear = true
                    isMonthm = false
                    mBinding.isMonth = false
                } else {
                    isYearm = false
                    mBinding.isYear = false
                    isMonthm = true
                    mBinding.isMonth = true
                }
            }

            loanAmtIv.setOnClickListener {
                mBinding.isSelected = 0
                isEnable = 0
                cvResult.visibility = View.GONE
                mBinding.etInterest.isEnabled = true
                mBinding.etPeriodYear.isEnabled = true
                mBinding.etEmi.isEnabled = true
                mBinding.etPeriodMonth.isEnabled = true
                mBinding.etLoanAmout.isEnabled = false
                mBinding.etLoanAmout.setText("")
            }
            timeperiodIv.setOnClickListener {
                mBinding.isSelected = 1
                isEnable = 1
                cvResult.visibility = View.GONE
                mBinding.etInterest.isEnabled = true
                mBinding.etPeriodYear.isEnabled = false
                mBinding.etEmi.isEnabled = true
                mBinding.etLoanAmout.isEnabled = true
                mBinding.etPeriodMonth.isEnabled = false
                mBinding.etPeriodMonth.setText("")
                mBinding.etPeriodYear.setText("")
            }
            interestRateIv.setOnClickListener {
                mBinding.isSelected = 3
                isEnable = 3
                cvResult.visibility = View.GONE
                mBinding.etInterest.isEnabled = false
                mBinding.etPeriodYear.isEnabled = true
                mBinding.etEmi.isEnabled = true
                mBinding.etPeriodMonth.isEnabled = true
                mBinding.etLoanAmout.isEnabled = true
                mBinding.etInterest.setText("")
            }
            emiAmtIv.setOnClickListener {
                mBinding.isSelected = 2
                isEnable = 2
                cvResult.visibility = View.GONE
                mBinding.etInterest.isEnabled = true
                mBinding.etPeriodYear.isEnabled = true
                mBinding.etEmi.isEnabled = false
                mBinding.etPeriodMonth.isEnabled = true
                mBinding.etLoanAmout.isEnabled = true
                mBinding.etEmi.setText("")

            }
        }
    }

    private fun setupPieChart(totalAmunt: Float, totalInterest: Float) {
        val view = mBinding
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()
        listPie.add(PieEntry(totalAmunt, "Principle Amount"))
        listColors.add(resources.getColor(R.color.app_light_color))
        listPie.add(PieEntry(totalInterest, "Interest Amount"))
        listColors.add(resources.getColor(R.color.app_color))

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

    fun Calculate(view: View) {
        val loan_amount = mBinding.etLoanAmout.text.trim().toString()
        val interest = mBinding.etInterest.text.trim().toString()
        val periodYear = mBinding.etPeriodYear.text.trim().toString()
        val periodMonth = mBinding.etPeriodMonth.text.trim().toString()
        val emi = mBinding.etEmi.text.trim().toString()

        hideKeyboard()

        if (isEnable == 0) {
            if (isValidateLoanAmtValue()) {
                Log.d("loanamnt", "this")
                mBinding.cvResult.visibility = View.VISIBLE
                principalCalculate()
            }
        } else if (isEnable == 3) {
            //   calculateInterest(loan_amount, periodYear, periodMonth, emi)
            if (isValidateInterestValue()) {

                mBinding.cvResult.visibility = View.VISIBLE
                val yearTOMonth = if (isYearm) {
                    (periodYear.toDouble() * 12).roundToInt()
                } else {
                    periodYear.toInt()
                }
                val time = yearTOMonth
                // showSnackBar("Interest rate calculation pending.")
                val value = calculateMissingLoanInterestRate(
                    loan_amount.toDouble(),
                    emi.toDouble(),
                    time.toDouble()
                )
            }


        } else if (isEnable == 2) {
            if (isValidateEMIValue()) {
                mBinding.cvResult.visibility = View.VISIBLE
                calculateEMI(loan_amount, interest, periodMonth, periodYear)
            }
        } else if (isEnable == 1) {
            if (isValidatePeriodValue()) {
                mBinding.cvResult.visibility = View.VISIBLE
                val timeperiodcalculate = calculateMissingLoanPaymentTerms(
                    interest.toDouble(),
                    loan_amount.toDouble(),
                    emi.toDouble()
                )
                LoanTimeConversion(timeperiodcalculate)
                Log.d("Mainactivity", "calculate: $timeperiodcalculate")
                mBinding.monthlyEmi.text = "$emi"
                val totalPayment = emi.toDouble() * (timeperiodcalculate.toInt() + 1)
                mBinding.totalPayment.text = String.format("%.02f", totalPayment)

                mBinding.totalInterest.text =
                    (totalPayment - loan_amount.toInt()).toInt().toString() + ""
                setupPieChart(
                    totalAmunt = totalPayment.toFloat(),
                    totalInterest = (totalPayment - loan_amount.toInt()).toFloat()
                )

            }

        }


    }

    fun LoanTimeConversion(value: Double) {
        val year = value
        if (isYearm) {
            val time = year.toDouble() / 12
            Log.d("Timeconversio", "LoanTimeConversion: ${value}")
            mBinding.etPeriodYear.setText(String.format("%.02f", time))
            /* isYearm = false
             mBinding.isYear = false
             isMonthm = true
             mBinding.isMonth = true*/
        } else {
            mBinding.etPeriodYear.setText("${year.toInt()}")
        }
    }

    private fun calculateEMI(
        loanAmount: String,
        interest: String,
        periodMonth: String,
        periodYear: String
    ) {
        if (loanAmount.isNullOrEmpty()) {
            showSnackBar("Enter Loan Amount")
        } else if (interest.isNullOrEmpty()) {
            showSnackBar("Enter Interest")
        } else if (periodMonth.isNullOrEmpty() && periodYear.isNullOrEmpty()) {
            showSnackBar("Enter period")
        } else {

            mBinding.ll3.visibility = View.VISIBLE

            val yearTOMonth = if (isYearm) {
                (periodYear.toDouble() * 12).roundToInt()
            } else {
                periodYear.toInt()
            }


            val time = yearTOMonth

            val p: Float = loanAmount.toFloat()
            val r: Float = interest.toFloat() / 12 / 100
            val t: Int = time.toInt()

            val emi = ((p * r * Math.pow((1 + r).toDouble(), t.toDouble())) / (Math.pow(
                (1 + r).toDouble(),
                t.toDouble()
            ) - 1))
            val formattedString = String.format("%.02f", emi)
            mBinding.etEmi.setText("$formattedString")
            mBinding.monthlyEmi.text = "$formattedString"
            val totalPayment = emi * time
            mBinding.totalPayment.text = String.format("%.02f", totalPayment + 1)

            mBinding.totalInterest.text = String.format("%.02f", (totalPayment - p.toInt()) + 1)

            setupPieChart(
                totalAmunt = totalPayment.toFloat(),
                totalInterest = (totalPayment - p.toInt()).toFloat()
            )
        }
    }

    // Function to calculate EMI

    fun calculateMissingLoanInterestRate(
        principalAmount: Double,
        monthlyPayment: Double,
        terms: Double
    ): Double {
        var x = 1 + (((monthlyPayment * terms / principalAmount) - 1) / 12)

        Log.d("valueofrate", x.toString())

        val FINANCIAL_PRECISION = 0.000001
        fun F(): Double {
            return (principalAmount * x * Math.pow(1 + x, terms) / (Math.pow(
                1 + x,
                terms
            ) - 1) - monthlyPayment)
        }

        fun FPrime(): Double {
            val c_derivative = pow(x + 1, terms)
            return principalAmount * pow(x + 1, terms - 1) *
                    (x * c_derivative + c_derivative - (terms * x) - x - 1) / pow(
                c_derivative - 1,
                2.0
            )
        }
        while (abs(F()) > FINANCIAL_PRECISION) {
            x = x - F() / FPrime()
        }

        Log.d("valueofrate", x.toString())
        val R = (12 * x * 100).toDouble()
        Log.d("valueofrate", R.toString())
        mBinding.etInterest.setText("%.2f".format(R))
        mBinding.monthlyEmi.text = "$monthlyPayment"
        val totalPayment = monthlyPayment.toInt() * terms
        mBinding.totalPayment.text = "$totalPayment"

        mBinding.totalInterest.text = (totalPayment - principalAmount.toInt()).toInt().toString()
        setupPieChart(
            totalAmunt = totalPayment.toFloat(),
            totalInterest = (totalPayment - principalAmount.toInt()).toFloat()
        )
        return R
    }


    fun principalCalculate() {
        mBinding.apply {
            var emiAmt = etEmi.text.toString().toDouble()
            var interAmt = etInterest.text.toString().toDouble()
            val r: Float = interAmt.toFloat() / 12 / 100
            val yearTOMonth = if (isYearm) {
                (etPeriodYear.text.toString().toDouble() * 12).toInt()
            } else {
                etPeriodYear.text.toString().toInt()
            }


            var timePeriod = (yearTOMonth).toDouble()
            var principle = (emiAmt * (Math.pow(
                (1 + r).toDouble(),
                timePeriod
            ) - 1) / (r * Math.pow((1 + r).toDouble(), (timePeriod).toDouble())))
            etLoanAmout.setText(principle.roundToInt().toString())
            val totalPayment = emiAmt * timePeriod
            monthlyEmi.text = String.format("%.02f", emiAmt)
            mBinding.totalPayment.text = String.format("%.02f", totalPayment)
            totalInterest.text = String.format("%.02f", (totalPayment - principle))

            setupPieChart(
                totalAmunt = totalPayment.toFloat(),
                totalInterest = (totalPayment - principle.toInt()).toFloat()
            )
        }
    }

    fun calculateMissingLoanMonthlyPayment(
        interest: Double,
        principalAmount: Double,
        terms: Int
    ): Double {
        var R = (interest / 100.0) / 12
        var P = principalAmount
        var N = terms
        var PMT = (R * P) / (1 - pow(1 + R, -N.toDouble()))
        return PMT
    }

    fun calculateMissingLoanPaymentTerms(
        interest: Double,
        principalAmount: Double,
        monthlyPayment: Double
    ): Double {
        /// To find the minimum monthly payment
        var minMonthlyPayment =
            calculateMissingLoanMonthlyPayment(interest, principalAmount, 1) - principalAmount

        if (monthlyPayment.toInt() <= minMonthlyPayment.toInt()) {
            // showSnackBar("Invalid monthly payment")
        }

        var PMT = monthlyPayment
        var P = principalAmount
        var I = (interest / 100.0) / 12
        var D = PMT / I
        var N = (log(D / (D - P)) / log(1 + I))

        Log.d("loanTime", N.toString())
        return N
    }


    fun Reset(view: View) {
        mBinding.apply {
            cvResult.visibility = View.GONE
            etLoanAmout.setText("")
            etInterest.setText("")
            etEmi.setText("")
            etPeriodYear.setText("")
            etPeriodMonth.setText("")
            isSelected = 0
        }
        mBinding.etInterest.isEnabled = true
        mBinding.etPeriodYear.isEnabled = true
        mBinding.etEmi.isEnabled = true
        mBinding.etPeriodMonth.isEnabled = true
        mBinding.etLoanAmout.isEnabled = false
        isEnable = 0
    }


    fun showSnackBar(result: String) {
        Snackbar.make(mBinding.root, "$result", Snackbar.LENGTH_LONG).show()
    }


    fun isValidateLoanAmtValue(): Boolean {
        var isValid = false
        if (mBinding.etPeriodYear.text.toString()
                .isNullOrEmpty()
        ) {
            // showSnackBar("Enter Loan Period")
            mBinding.etPeriodYear.setError("Enter time period")
            mBinding.etPeriodYear.requestFocus()
        } else if (mBinding.etEmi.text.toString().isNullOrEmpty()) {
            mBinding.etEmi.setError("Enter emi")
            mBinding.etEmi.requestFocus()
        } else if (mBinding.etInterest.text.toString().isNullOrEmpty()) {
            //  showSnackBar("Enter Interest")
            mBinding.etInterest.setError("Enter rate of interest")
            mBinding.etInterest.requestFocus()
        } else {
            isValid = true
        }

        return isValid
    }

    fun isValidateInterestValue(): Boolean {
        var isValid = false
        if (mBinding.etPeriodYear.text.toString()
                .isNullOrEmpty()
        ) {
            //    showSnackBar("Enter Loan Period")
            mBinding.etPeriodYear.apply {
                setError("Enter loan period")
                requestFocus()
            }
        } else if (mBinding.etLoanAmout.text.toString().isNullOrEmpty()) {
            //  showSnackBar("Enter Loan Amount")
            mBinding.etLoanAmout.setError("Enter loan amount")
            mBinding.etLoanAmout.requestFocus()
        } else if (mBinding.etEmi.text.toString().isNullOrEmpty()) {
            //  showSnackBar("Enter EMI of Per Month ")
            mBinding.etEmi.setError("Enter EMI of per month")
            mBinding.etEmi.requestFocus()
        } else {
            isValid = true
        }

        return isValid
    }

    fun isValidatePeriodValue(): Boolean {
        var isValid = false
        if (mBinding.etLoanAmout.text.toString().isNullOrEmpty()) {
            //  showSnackBar("Enter Loan Amount")
            mBinding.etLoanAmout.setError("Enter loan amount")
            mBinding.etLoanAmout.requestFocus()
        } else if (mBinding.etEmi.text.toString().isNullOrEmpty()) {
            //  showSnackBar("Enter Emi In Per Month")
            mBinding.etEmi.setError("Enter emi in per month")
            mBinding.etEmi.requestFocus()

        } else if (mBinding.etInterest.text.toString().isNullOrEmpty()) {
            //  showSnackBar("Enter Interest")
            mBinding.etInterest.setError("Enter rate of interest")
            mBinding.etInterest.requestFocus()

        } else {
            isValid = true
        }

        return isValid
    }

    fun isValidateEMIValue(): Boolean {
        var isValid = false
        if (mBinding.etPeriodYear.text.toString()
                .isNullOrEmpty()
        ) {
            mBinding.etPeriodYear.setError("Enter loan period")
            mBinding.etPeriodYear.requestFocus()

            // showSnackBar("Enter Loan Period")
        } else if (mBinding.etLoanAmout.text.toString().isNullOrEmpty()) {
            //  showSnackBar("Enter Loan Amount")
            mBinding.etLoanAmout.setError("Enter loan amount")
            mBinding.etLoanAmout.requestFocus()

        } else if (mBinding.etInterest.text.toString().isNullOrEmpty()) {
            //  showSnackBar("Enter Interest")
            mBinding.etInterest.setError("Enter rate of interest")
            mBinding.etInterest.requestFocus()

        } else {
            isValid = true
        }

        return isValid
    }

}