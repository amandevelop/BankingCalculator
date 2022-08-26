package com.emicalculator.stampDuty

import android.animation.ObjectAnimator
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.emicalculator.R
import com.emicalculator.databinding.ActivityStampDutyBinding
import com.emicalculator.hideKeyboard
import com.emicalculator.scrollDown
import com.emicalculator.stampDutyRateList


class StampDuty : AppCompatActivity() {
    private lateinit var mBinding: ActivityStampDutyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityStampDutyBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        title = "Stamp Duty Calculator"


        mBinding.tv8.setOnClickListener(View.OnClickListener {
            mBinding.et1.clearFocus()
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
                R.layout.spinner_item, getStateList()
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
                    mBinding.tv8.text = arrayAdapter.getItem(i)
                    dialog.dismiss()
                }
        })

       /* mBinding.et1.doOnTextChanged { text, start, before, count ->

            if (!text?.trim().isNullOrEmpty()){
                val selectedState = mBinding.tv8.text.toString().trim()

                if (text.toString().toDouble() < 1 || text.toString().toDouble() > 100000000.0){
                    mBinding.et1.apply {

                        error = "Property value should be between 0 to 10,00,00,000."
                    }
                }else{
                    mBinding.et1.error = null
                    if (!selectedState.isNullOrEmpty()){
                        val rateofStamp = stampDutyRateList.filter { it.state == selectedState }[0]
                        val finalResult =
                            text.toString().toDouble() * (rateofStamp.stampdutyRate.toDouble() / 100)

                        mBinding.cv1.visibility = View.VISIBLE
                        mBinding.result.text =
                            "Stamp Duty of your property is \nRs. ${String.format("%.02f", finalResult)}"
                        mBinding.ruleOfTds.text = "Rate in your state is ${
                            String.format(
                                "%.02f",
                                rateofStamp.stampdutyRate.toDouble()
                            )
                        } %"

                        if (mBinding.scrollView.scrollY == mBinding.scrollView.top){
                            scrollDown(mBinding.scrollView)
                        }
                    }
                }

            }
        }*/

        mBinding.btn1.setOnClickListener {
            mBinding.cv1.visibility = View.GONE
            mBinding.et1.apply {
                clearFocus()
            }


            val propertyValue = mBinding.et1.text.toString()
            val selectedState = mBinding.tv8.text.toString().trim()

            if (selectedState.isNullOrEmpty()) {
                Toast.makeText(this, "Select your state", Toast.LENGTH_SHORT).show()
            } else if (propertyValue.isNullOrEmpty()) {
                mBinding.et1.apply {
                    requestFocus()
                    error = "Enter property value."
                }
            } else if (propertyValue.toDouble() < 1 || propertyValue.toDouble() > 100000000.0) {
                mBinding.et1.apply {
                    requestFocus()
                    error = "Property value should be between 0 to 10,00,00,000."
                }
            } else {

                hideKeyboard(it)
                val rateofStamp = stampDutyRateList.filter { it.state == selectedState }[0]
                val finalResult =
                    propertyValue.toDouble() * (rateofStamp.stampdutyRate.toDouble() / 100)

                mBinding.cv1.visibility = View.VISIBLE
                mBinding.result.text =
                    "Stamp Duty of your property is \nRs. ${String.format("%.02f", finalResult)}"
                mBinding.ruleOfTds.text = "Rate in your state is ${
                    String.format(
                        "%.02f",
                        rateofStamp.stampdutyRate.toDouble()
                    )
                } %"

                if (mBinding.scrollView.scrollY == mBinding.scrollView.top){
                    scrollDown(mBinding.scrollView)
                }

            }
        }

    }

    fun getStateList(): List<String> {
        return stampDutyRateList.map { it.state }.sorted()
    }

}