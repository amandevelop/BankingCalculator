package com.emicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.emicalculator.databinding.ActivityFdcalculatorBinding
import java.text.SimpleDateFormat
import java.util.*

class FDCalculator : AppCompatActivity() {
    private lateinit var mBinding: ActivityFdcalculatorBinding
    var isYearm = true
    var isMonthm = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFdcalculatorBinding.inflate(layoutInflater).apply {
            isYear = isYearm
            isMonth = isMonthm
        }
        setContentView(mBinding.root)
        title = "FD Calculator"
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
            mBinding.ll7.visibility = View.VISIBLE
            mBinding.ll8.visibility = View.VISIBLE
            hideKeyboard()
            val month =  if (isYearm) (tenure.toDouble()) else tenure.toDouble() / 12
            val rate = calculateRValue(rateofInterest)
            val secd = Math.pow(rate.toDouble(), month.toDouble() * 4)
            Log.d("resultOfFd", "$rate , $secd , $month")
            val m = depositamnt.toDouble() * secd
            Log.d("resultOfFD", m.toString())

            val Inactualmonth =  if (isYearm) (tenure.toDouble() * 12) else tenure.toDouble()
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val currentDateandTime: String = sdf.format(Date())
            mBinding.maturitydata.text = "${getAfterDateMonth(Inactualmonth.toInt())}"
            mBinding.depositsDate.text = "$currentDateandTime"
            mBinding.maturityValue.text = "  ₹ " + String.format("%.02f", m)
            mBinding.depositAmtTv.text ="₹ " + String.format("%.02f", depositamnt.toDouble())
            mBinding.totalInterest.text =
                "₹ " + String.format("%.02f", (m - depositamnt.toDouble()))
        }

    }

    fun calculateRValue(rateofInterest: String): Double {
        Log.d("resultOfFD", rateofInterest.toString())
        return (400 + rateofInterest.toDouble()) / 400
    }


    private fun isValidating(depositamnt: String, rateofInterest: String, tenure: String): Boolean {
        var isValid = false
        if (depositamnt.isNullOrEmpty()) {
            isValid = false
            mBinding.depositAmt.error ="Enter Deposit Amount"
            mBinding.depositAmt.requestFocus()
        } else if (rateofInterest.isNullOrEmpty()) {
            isValid = false
            mBinding.rateOfInterest.error ="Enter rate of interest"
            mBinding.rateOfInterest.requestFocus()

        } else if (tenure.isNullOrEmpty()) {
            isValid = false
            mBinding.tenure.error ="Enter time period"
            mBinding.tenure.requestFocus()

        } else {
            isValid = true
        }
        return isValid

    }
}