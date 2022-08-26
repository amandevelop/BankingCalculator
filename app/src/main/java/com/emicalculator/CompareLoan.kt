package com.emicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.emicalculator.databinding.ActivityCompareLoanBinding
import kotlin.math.roundToInt

class CompareLoan : AppCompatActivity() {
    private lateinit var mBinding: ActivityCompareLoanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCompareLoanBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        title = "Compare Loan"
        mBinding.btnCaculate.setOnClickListener {
            val loan1 = mBinding.etLoanamt1.text.toString().trim()
            val loan2 = mBinding.etLoanamt2.text.toString().trim()
            val interest1 = mBinding.etInterest1.text.toString().trim()
            val interest2 = mBinding.etInterest2.text.toString().trim()
            val timeduration1 = mBinding.etPeriod1.text.toString().trim()
            val timeduration2 = mBinding.etPeriod2.text.toString().trim()
            if (isValidatingEntries()) {
                hideKeyboard()
                val emi1 = calculateEMI1(loan1, interest1, timeduration1)
                val emi2 = calculateEMI2(loan2, interest2, timeduration2)

                val interest_Rate1 = (emi1 * timeduration1.toDouble()) - loan1.toDouble()
                val interest_Rate2 = (emi2 * timeduration2.toDouble()) - loan2.toDouble()

                val result = emi1 - emi2
                val interestResult = interest_Rate1 - interest_Rate2
                mBinding.resultL1.text = "₹" + String.format("%.02f", emi1.toDouble())
                mBinding.resultL2.text = "₹" + String.format("%.02f", emi2.toDouble())
                mBinding.differenceloan.text =
                    "₹" + String.format("%.02f", result.toDouble()).replace("-", "")

                mBinding.resultI1.text = "₹" + String.format("%.02f", interest_Rate1.toDouble())
                mBinding.resultI2.text = "₹" + String.format("%.02f", interest_Rate2.toDouble())

                mBinding.differenceinterest.text =
                    "₹" + String.format("%.02f", interestResult.toDouble()).replace("-", "")

                mBinding.resultT1.text =
                    "₹" + String.format("%.02f", (loan1.toDouble() + interest_Rate1))
                mBinding.resultT2.text =
                    "₹" + String.format("%.02f", (loan2.toDouble() + interest_Rate2))

                val dif = (loan1.toDouble() + interest_Rate1) - (loan2.toDouble() + interest_Rate2)
                mBinding.differenceperiod.text = "₹" + String.format("%.02f", dif).replace("-", "")
                Log.d("loanCompare", "$result")
            }

        }

    }

    private fun calculateEMI1(
        loanAmount: String,
        interest: String,
        periodMonth: String
    ): Double {
        var emi = 0.0
        if (loanAmount.isNullOrEmpty()) {
            mBinding.etLoanamt1.setError("Enter loan amount")
        } else if (interest.isNullOrEmpty()) {
            mBinding.etInterest1.setError("Enter rate of interest")
        } else if (periodMonth.isNullOrEmpty()) {
            mBinding.etPeriod1.setError("Enter loan tenure")
        } else if (interest.toDouble() > 50 || interest.toDouble() < 2) {
            mBinding.etInterest1.setError("Enter rate of interest between 2% to 50%")
        } else if (periodMonth.toDouble() > 600 || interest.toDouble() < 1) {
            mBinding.etPeriod1.setError("Enter loan time period between 1m to 600m")
        } else {

            val p: Float = loanAmount.toFloat()
            val r: Float = interest.toFloat() / 12 / 100
            val t: Int = periodMonth.toInt()

            emi = ((p * r * Math.pow((1 + r).toDouble(), t.toDouble())) / (Math.pow(
                (1 + r).toDouble(),
                t.toDouble()
            ) - 1))
            /*  val formattedString = String.format("%.02f", emi)
              mBinding.etEmi.setText("$formattedString")
              mBinding.monthlyEmi.text = "$formattedString"
              val totalPayment = emi * time
              mBinding.totalPayment.text = String.format("%.02f", totalPayment + 1)
              mBinding.totalInterest.text = String.format("%.02f", (totalPayment - p.toInt()) + 1)*/
            return emi

        }
        return emi
    }

    private fun calculateEMI2(
        loanAmount: String,
        interest: String,
        periodMonth: String
    ): Double {
        var emi = 0.0
        if (loanAmount.isNullOrEmpty()) {
            mBinding.etLoanamt2.setError("Enter loan amount")
        } else if (interest.isNullOrEmpty()) {
            mBinding.etInterest2.setError("Enter rate of interest")
        } else if (periodMonth.isNullOrEmpty()) {
            mBinding.etPeriod2.setError("Enter loan tenure")
        } else if (interest.toDouble() > 50 || interest.toDouble() < 2) {
            mBinding.etInterest2.setError("Enter rate of interest between 2% to 50%")
        } else if (periodMonth.toDouble() > 600 || interest.toDouble() < 1) {
            mBinding.etPeriod2.setError("Enter loan time period between 1m to 600m")
        } else {

            val p: Float = loanAmount.toFloat()
            val r: Float = interest.toFloat() / 12 / 100
            val t: Int = periodMonth.toInt()

            emi = ((p * r * Math.pow((1 + r).toDouble(), t.toDouble())) / (Math.pow(
                (1 + r).toDouble(),
                t.toDouble()
            ) - 1))


            return emi
            /*  val formattedString = String.format("%.02f", emi)
              mBinding.etEmi.setText("$formattedString")
              mBinding.monthlyEmi.text = "$formattedString"
              val totalPayment = emi * time
              mBinding.totalPayment.text = String.format("%.02f", totalPayment + 1)
              mBinding.totalInterest.text = String.format("%.02f", (totalPayment - p.toInt()) + 1)*/

        }
        return emi
    }


    fun isValidatingEntries(): Boolean {
        var isvalid = false
        if (mBinding.etLoanamt1.text.isNullOrEmpty()) {
            mBinding.etLoanamt1.apply {
                requestFocus()
                error = "Enter loan amount"
            }

        } else if (mBinding.etLoanamt2.text.isNullOrEmpty()) {
            mBinding.etLoanamt2.apply {
                requestFocus()
                error = "Enter loan amount"
            }

        } else if (mBinding.etInterest1.text.isNullOrEmpty()) {
            mBinding.etInterest1.apply {
                requestFocus()
                error = "Enter rate of interest"
            }

        } else if (mBinding.etInterest2.text.isNullOrEmpty()) {
            mBinding.etInterest2.apply {
                requestFocus()
                error = "Enter rate of interest"
            }

        } else if (mBinding.etPeriod1.text.isNullOrEmpty()) {
            mBinding.etPeriod1.apply {
                requestFocus()
                error = "Enter time period"
            }

        } else if (mBinding.etPeriod2.text.isNullOrEmpty()) {
            mBinding.etPeriod2.apply {
                requestFocus()
                error = "Enter time period"
            }

        } else {
            isvalid = true
        }



        return isvalid
    }


}