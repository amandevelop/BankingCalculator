package com.emicalculator

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.emicalculator.databinding.ActivityRdcalculatorBinding
import java.text.SimpleDateFormat
import java.util.*


class RDCalculator : AppCompatActivity() {
    private lateinit var mBinding: ActivityRdcalculatorBinding
    var isYearm = true
    var isMonthm = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRdcalculatorBinding.inflate(layoutInflater).apply {
            isYear = isYearm
            isMonth = isMonthm
        }
        setContentView(mBinding.root)
        title = "RD Calculator"

        mBinding.apply {
            itemSwitch.cv1.setOnClickListener {
                if (isYearm){
                    isYearm = false
                    mBinding.isYear = false
                    isMonthm = true
                    mBinding.isMonth = true
                }else{
                    isYearm = true
                    mBinding.isYear = true
                    isMonthm = false
                    mBinding.isMonth = false
                }
            }
            itemSwitch.cv2.setOnClickListener {
                if (isMonthm){
                    isYearm = true
                    mBinding.isYear = true
                    isMonthm = false
                    mBinding.isMonth = false
                }else{
                    isYearm = false
                    mBinding.isYear = false
                    isMonthm = true
                    mBinding.isMonth = true
                }
            }

        }
    }

    fun Calculate(view: View) {
        val depositamnt = mBinding.depositAmt.text.toString().trim()
        val rateofInterest = mBinding.rateOfInterest.text.toString().trim()
        val tenure = mBinding.tenure.text.toString().trim()

        if (isValidating(depositamnt, rateofInterest, tenure)) {
            hideKeyboard()
            mBinding.ll8.visibility = View.VISIBLE
            mBinding.ll7.visibility = View.VISIBLE
            var p = depositamnt.toDouble()
            var i = rateofInterest.toDouble()
            var n = if (isYearm) (tenure.toDouble()  * 12 ) else tenure.toDouble()

            var f = Math.pow((1 + i / 400), n / 3)
            var d = Math.pow((1 + i / 400), (-1 / 3.0))
            val m = (p * (f - 1)) / (1 - d)


            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val currentDateandTime: String = sdf.format(Date())


            mBinding.maturitydata.text = "${getAfterDateMonth(n.toInt())}"
            mBinding.depositsDate.text = "$currentDateandTime"
            mBinding.maturityValue.text = "₹" + String.format("%.02f", m)
            mBinding.depositAmtTv.text = "₹" + String.format("%.02f", (p * n))
            mBinding.totalInterest.text =
                "₹" + String.format("%.02f", (m - (p * n)))
        }

    }

    private fun calculateTimeValue(month: Double): Double {
        return ((month * (month + 1)) / 24)
    }

    fun calculateRValue(rateofInterest: String): Double {
        Log.d("resultOfFD", rateofInterest.toString())
        return (rateofInterest.toDouble() / 100)
    }


    fun RDCalculate() {
        val result = 5000 * Math.pow((1 + 0.0825 / 4), (4.0 * 12 / 12))
        val result2 = 5000 * Math.pow((1 + 0.0825 / 4), (4.0 * 2 / 12))
        val result3 = 5000 * Math.pow((1 + 0.1 / 1), (1.toDouble() * (3 / 12)))
        Log.d("RdFind", " $result")
    }

    private fun isValidating(depositamnt: String, rateofInterest: String, tenure: String): Boolean {
        var isValid = false
        if (depositamnt.isNullOrEmpty()) {
            isValid = false
            mBinding.depositAmt.apply {
                setError("Enter deposit amount")
                requestFocus()
            }

        } else if (rateofInterest.isNullOrEmpty()) {
            isValid = false
            mBinding.rateOfInterest.apply {
                setError("Enter rate of interest")
                requestFocus()
            }

        } else if (tenure.isNullOrEmpty()) {
            isValid = false
            mBinding.tenure.apply {
                setError("Enter time period")
                requestFocus()
            }

        } else {
            isValid = true
        }

        return isValid

    }

}