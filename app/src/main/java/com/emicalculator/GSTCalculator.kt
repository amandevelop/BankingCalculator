package com.emicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.Toast
import com.emicalculator.databinding.ActivityGstcalculatorBinding

class GSTCalculator : AppCompatActivity() {
    private lateinit var mBinding: ActivityGstcalculatorBinding
    var isAddGST = true
    var billerState = ""
    var customerState = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityGstcalculatorBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        title = "GST Calculator"

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item, listOfState
        )
        mBinding.yourSpinner.adapter = adapter
        mBinding.customerSpinner.adapter = adapter
        mBinding.customerSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                if (position != 0) {
                    customerState = listOfState[position]

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        mBinding.yourSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                if (position != 0) {
                    billerState = listOfState[position]

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        findViewById<RadioGroup>(R.id.rg_gst).setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.add_gst) {
                isAddGST = true
            } else {
                isAddGST = false
            }
        }
    }

    fun Calculate(view: View) {
        mBinding.ll8.visibility = View.VISIBLE
        if (isAddGST) {
            addGSTCalculation()
        } else {
            removeGSTCalculation()
        }
    }

    private fun removeGSTCalculation() {
        val amount = mBinding.initialAmnt.text.toString()
        val gstinper = mBinding.rateOfGst.text.toString()
        if (amount.isNullOrEmpty()) {
            mBinding.initialAmnt.setError("Enter Amount")
        } else if (gstinper.isNullOrEmpty()) {
            mBinding.rateOfGst.setError("Enter GST In Percent")
        } else if (billerState.isNullOrEmpty()) {
            Toast.makeText(this, "Select Your State", Toast.LENGTH_SHORT).show()
        } else if (customerState.isNullOrEmpty()) {
            Toast.makeText(this, "Select Customer State", Toast.LENGTH_SHORT).show()

        } else {
            val finalAmount =
                amount.toDouble() - (amount.toDouble() * (100 / (gstinper.toDouble() + 100)))
            val netAmt = amount.toDouble() - finalAmount
            mBinding.netAmtTv.text = String.format("%.02f", amount.toDouble()) + "Rs."
            mBinding.gstAmtTv.text = String.format("%.02f", finalAmount.toDouble()) + "Rs."
            mBinding.totalAmtTv.text = String.format("%.02f", netAmt.toDouble()) + "Rs."
            if (billerState.equals(customerState)){
                mBinding.cgstTv.text = "${String.format("%.02f", (finalAmount) / 2) } Rs."
                mBinding.sgstTv.text = "${String.format("%.02f", (finalAmount) / 2) } Rs."
                mBinding.igstTv.text = "0 Rs."
            }else{
                mBinding.cgstTv.text = "0 Rs."
                mBinding.sgstTv.text = "0 Rs."
                mBinding.igstTv.text = "${String.format("%.02f", (finalAmount)) } Rs."
            }
        }

    }

    private fun addGSTCalculation() {
        val amount = mBinding.initialAmnt.text.toString()
        val gstinper = mBinding.rateOfGst.text.toString()
        hideKeyboard()
        if (amount.isNullOrEmpty()) {
            mBinding.initialAmnt.setError("Enter Amount")
            mBinding.initialAmnt.requestFocus()

        } else if (gstinper.isNullOrEmpty()) {
            mBinding.rateOfGst.setError("Enter GST In Percent")
            mBinding.rateOfGst.requestFocus()
        } else if (billerState.isNullOrEmpty()) {
            Toast.makeText(this, "Select Your State", Toast.LENGTH_SHORT).show()
        } else if (customerState.isNullOrEmpty()) {
            Toast.makeText(this, "Select Customer State", Toast.LENGTH_SHORT).show()
        } else {

            val finalAmount = ((amount.toDouble() * gstinper.toDouble()) / 100) + amount.toDouble()
            /*  mBinding.finalResult.text = "Gross Price : ${String.format("%.02f", finalAmount)}\n" +
                      "CGST : \n" +
                      "IGST : \n" +
                      "Total Tax : ${String.format("%.02f", (finalAmount - amount.toDouble()))}"*/
            mBinding.netAmtTv.text = String.format("%.02f", amount.toDouble()) + " Rs."
            mBinding.gstAmtTv.text =
                "${String.format("%.02f", (finalAmount - amount.toDouble()))} Rs."
            mBinding.totalAmtTv.text = "${String.format("%.02f", finalAmount)} Rs."



            if (billerState.equals(customerState)){
                mBinding.cgstTv.text = "${String.format("%.02f", (finalAmount - amount.toDouble()) / 2) } Rs."
                mBinding.sgstTv.text = "${String.format("%.02f", (finalAmount - amount.toDouble()) / 2) } Rs."
                mBinding.igstTv.text = "0 Rs."
            }else{
                mBinding.cgstTv.text = "0 Rs."
                mBinding.sgstTv.text = "0 Rs."
                mBinding.igstTv.text = "${String.format("%.02f", (finalAmount - amount.toDouble())) } Rs."
            }
        }


    }
}