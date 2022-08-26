package com.emicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.emicalculator.databinding.ActivityGratuityCalculatorBinding
import com.emicalculator.databinding.ActivitySipcalculatorBinding

class Gratuity_calculator : AppCompatActivity() {
    private lateinit var mBinding: ActivityGratuityCalculatorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityGratuityCalculatorBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        title = "Gratuity Calculator"
        mBinding.btnCal.setOnClickListener {
            val basic_salary = mBinding.montlyInvestment.text.toString().trim()
            val time = mBinding.timeDuaration.text.toString().trim()

            if (basic_salary.isNullOrEmpty()) {
                mBinding.montlyInvestment.apply {
                    error = "Enter your basic salary"
                    requestFocus()
                }
            } else if (time.isNullOrEmpty()) {
                mBinding.timeDuaration.apply {
                    error = "Enter service duration"
                    requestFocus()
                }
            } else {
                hideKeyboard()
                val result = (basic_salary.toDouble() * time.toDouble() * 15) / 26
                mBinding.ll8.visibility = View.VISIBLE
                mBinding.maturityValue.text = "₹" + String.format("%.02f", result.toDouble())
            }

        }
    }
}