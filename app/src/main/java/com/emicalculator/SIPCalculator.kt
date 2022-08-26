package com.emicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.emicalculator.databinding.ActivitySipcalculatorBinding
import java.text.SimpleDateFormat
import java.util.*

class SIPCalculator : AppCompatActivity() {
    private lateinit var mBinding: ActivitySipcalculatorBinding
    var isYearm = true
    var isMonthm = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySipcalculatorBinding.inflate(layoutInflater).apply {
            isYear = isYearm
            isMonth = isMonthm
        }
        setContentView(mBinding.root)
        title = "SIP Calculator"

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
        }
    }

    fun CalculateSIP(view: View) {
        hideKeyboard()
        if (mBinding.montlyInvestment.text.isNullOrEmpty()) {
            mBinding.montlyInvestment.apply {
                error = "Enter monthly investment"
                requestFocus()
            }
        } else if (mBinding.etPeriodYear.text.isNullOrEmpty()) {
            mBinding.etPeriodYear.apply {
                error = "Enter loan tenure"
                requestFocus()
            }
        } else if (mBinding.annualRate.text.isNullOrEmpty()) {
            mBinding.annualRate.apply {
                error = "Enter rate of interest"
                requestFocus()
            }
        }else if (mBinding.annualRate.text.toString().toDouble() > 100 || mBinding.annualRate.text.toString().toDouble() == 0.0  )
            Toast.makeText(this, "Please interest valid rate of interest", Toast.LENGTH_SHORT).show()

        else {
            val principal = mBinding.montlyInvestment.text.toString()
            val rate = mBinding.annualRate.text.toString()
            val yearTOMonth = if (isYearm) {
                mBinding.etPeriodYear.text.toString().toInt() * 12
            } else {
                mBinding.etPeriodYear.text.toString().toInt()
            }


            var inmonth = yearTOMonth;
            var monthly_rate = rate.toDouble() / 12 / 100;
            var a = Math.pow(1 + monthly_rate, inmonth.toDouble());
            var b = (a - 1) / monthly_rate;
            var M = Math.round(principal.toDouble() * b * (1 + monthly_rate));
            var in_amount = principal.toDouble() * inmonth;



            mBinding.ll7.visibility = View.VISIBLE
            mBinding.ll8.visibility = View.VISIBLE
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val currentDateandTime: String = sdf.format(Date())
            mBinding.maturitydata.text = "${getAfterDateMonth(yearTOMonth.toInt())}"
            mBinding.depositsDate.text = "$currentDateandTime"
            mBinding.maturityValue.text = "₹" + String.format("%.02f", M.toDouble())
            mBinding.depositAmtTv.text =
                "₹" + String.format("%.02f", in_amount.toDouble())
            mBinding.totalInterest.text =
                "₹" + String.format("%.02f", (M - in_amount).toDouble())
        }
    }
}