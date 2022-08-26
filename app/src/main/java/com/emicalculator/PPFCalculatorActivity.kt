package com.emicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.emicalculator.databinding.ActivityPpfactivityBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.internal.Intrinsics

class PPFCalculatorActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityPpfactivityBinding
    var isYearm = 0
    var isMonthm = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPpfactivityBinding.inflate(layoutInflater)
            .apply {
                isProgress = isYearm
                isMonth = isMonthm
            }
        setContentView(mBinding.root)
        title = "PPF Calculator"

        mBinding.apply {

            tenureSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                    Log.d("Seekbarposition", "onProgressChanged:  $progress")
                    mBinding.isProgress = progress
                    isYearm = progress
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })


            btnCaculate.setOnClickListener {
                val amnt = depositAmt.text.toString()
                val mrate = rate.text.toString()
                hideKeyboard()
                if (amnt.isNullOrEmpty()) {
                    depositAmt.setError("Enter amount ")
                    depositAmt.requestFocus()
                } else if (mrate.isNullOrEmpty()) {
                    rate.setError("Enter rate of interest ")
                    rate.requestFocus()
                } else {
                    mBinding.ll7.visibility = View.VISIBLE
                    mBinding.ll8.visibility = View.VISIBLE
                    calculationFn(amnt, mrate, getTenure())
                }
            }
        }
    }

    private fun getTenure(): String {
        return when (mBinding.tenureSeekbar.progress) {
            0 -> "15"
            1 -> "20"
            2 -> "25"
            3 -> "30"
            else -> ""
        }
    }

    private fun calculationFn(amnt: String, rate: String, tenure: String) {
        val P = amnt.toDouble()
        val R = rate.toDouble()
        val T = tenure.toDouble()

///a = p((1+i)n - 1i
        var r = R / 100

        var a = (1 + (R / 100))
        var b = Math.pow(a, T)

        var c = b - 1

        var d = c / r

        var e = P * d * (1 + r)


        //  mBinding.finalResult.text = "$e"
        val Inactualmonth = tenure.toInt() * 12
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDateandTime: String = sdf.format(Date())
        val depositamnt = P * tenure.toInt()
        mBinding.maturitydata.text = "${getAfterDateMonth(Inactualmonth.toInt())}"
        mBinding.depositsDate.text = "$currentDateandTime"
        mBinding.maturityValue.text = "₹" + String.format("%.02f", e)
        mBinding.depositAmtTv.text = "₹" + String.format("%.02f", P.toDouble())
        mBinding.totalInterest.text =
            "₹" + String.format("%.02f", (e - depositamnt.toDouble()))
        Log.d("valueppf", e.toString())

    }
}