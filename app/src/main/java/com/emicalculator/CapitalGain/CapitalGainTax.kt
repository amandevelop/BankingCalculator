package com.emicalculator.CapitalGain

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emicalculator.*
import com.emicalculator.databinding.ActivityCapitalGainTaxBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class CapitalGainTax : AppCompatActivity() {
    private lateinit var mBinding: ActivityCapitalGainTaxBinding

    var boughtDateYear = 0
    var soldDateYear = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCapitalGainTaxBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        title = "Capital Gain Calculator"

        assestSpinner()
        onClickEvent()


    }


    private fun onClickEvent() {
        mBinding.apply {
            assestBoughtDate.setOnClickListener {
                hideKeyboard()
                netBuyingPrice.clearFocus()
                netSellingPrice.clearFocus()
                datePicker(assestBoughtDate)

            }
            assestSoldDate.setOnClickListener {
                hideKeyboard()
                netBuyingPrice.clearFocus()
                netSellingPrice.clearFocus()
                datePicker(assestSoldDate)
            }

            btn1.setOnClickListener {
                val sdate = assestBoughtDate.text.toString().trim()
                val eDate = assestSoldDate.text.toString().trim()
                val sellingPrice = netSellingPrice.text.toString().trim()
                val buyingPrice = netBuyingPrice.text.toString().trim()
                if (sellingPrice.isNullOrEmpty()) {
                    netSellingPrice.apply {
                        requestFocus()
                        error = "Enter net selling price"
                    }
                } else if (sellingPrice.toDouble() < 1) {
                    netSellingPrice.apply {
                        requestFocus()
                        error = "Amount can't be 0"
                    }
                } else if (buyingPrice.isNullOrEmpty()) {
                    netBuyingPrice.apply {
                        requestFocus()
                        error = "Enter net buying price"
                    }
                } else if (buyingPrice.toDouble() < 1) {
                    netBuyingPrice.apply {
                        requestFocus()
                        error = "Amount can't be 0"
                    }
                }  else if (sdate.isNullOrEmpty()) {
                    Toast.makeText(this@CapitalGainTax, "Select bought date ", Toast.LENGTH_SHORT)
                        .show()
                } else if (eDate.isNullOrEmpty()) {
                    Toast.makeText(this@CapitalGainTax, "Select sold date ", Toast.LENGTH_SHORT)
                        .show()
                } else {

                    hideKeyboard()
                    netBuyingPrice.clearFocus()
                    netSellingPrice.clearFocus()
                    checkValidDate(sdate, eDate)
                }
            }
        }
    }

    private fun assestSpinner() {
        val list = assests.map { it.assest }
        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item, list
        )
        mBinding.spinnerAssesst.adapter = adapter
        mBinding.spinnerAssesst.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                mBinding.selectedAssest.text = list[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun datePicker(textview: TextView) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this,
            R.style.customTHemer,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                textview.setText("$dayOfMonth/${monthOfYear + 1}/$year")
            },
            year,
            month,
            day
        )

        dpd.show()
    }


    private fun checkValidDate(sDate: String, eDate: String) {
        // converting the inputted date to string
        // converting the inputted date to string

        val simpleDateFormat1 = SimpleDateFormat("dd/MM/yyyy")
        try {
            // converting it to date format
            val date1: Date = simpleDateFormat1.parse(sDate)
            val date2: Date = simpleDateFormat1.parse(eDate)
            val startdate = date1.time
            val endDate = date2.time

            // condition
            if (startdate <= endDate) {
                val diff: Long =  endDate - startdate
                val seconds = diff / 1000
                val minutes = seconds / 60
                val hours = minutes / 60
                val days = hours / 24

                val year = days
                val assesst = mBinding.selectedAssest.text.toString().trim()
                val longTerm = assests.filter { it.assest == assesst  }[0]
                mBinding.llResult.visibility = View.VISIBLE
                if (mBinding.scrollView.scrollY == mBinding.scrollView.top){
                    scrollDown(mBinding.scrollView)
                }
                val myTerm = (longTerm.longTerm.toDouble() * 365)

                if (year.toDouble() > myTerm){
                    Log.d("days", "checkValidDate: Long Term ${myTerm} , ${year.toDouble()}")
                    val profit = mBinding.netSellingPrice.text.toString().toDouble() - mBinding.netBuyingPrice.text.toString().toDouble()
                    val tax  =   profit.toDouble() * (longTerm.longTermTax.toString().toDouble() / 100)
                     if (profit > 0){
                         mBinding.result.text = "Profit/Loss -: ${ String.format("%.02f", profit.toDouble()) + "Rs."}\n" +
                                 "Tax Amount -: ${String.format("%.02f", tax.toDouble()) + "Rs."}\n"+
                                 "Effective Tax Rate -: ${String.format("%.02f", longTerm.longTermTax.toString().toDouble()) + "%"}"
                         mBinding.ruleOfTds.visibility = View.GONE
                         mBinding.result.visibility = View.VISIBLE
                     }else{
                         mBinding.result.visibility = View.GONE
                         mBinding.ruleOfTds.visibility = View.VISIBLE
                         mBinding.ruleOfTds.text = "${resources.getString(R.string.lossMessage)}"
                     }
                }else{

                    Log.d("days", "checkValidDate: Short Term ${longTerm.longTerm} , ${year.toDouble()}")
                    val profit = mBinding.netSellingPrice.text.toString().toDouble() - mBinding.netBuyingPrice.text.toString().toDouble()

                    if (!longTerm.shortTermTax.equals("0") && profit > 0){

                            val tax =  profit.toDouble() * (longTerm.shortTermTax.toString().toDouble() / 100)
                            mBinding.result.text = "Profit/Loss -: ${ String.format("%.02f", profit.toDouble()) + "Rs."}\n" +
                                    "Tax Amount-: ${String.format("%.02f", tax.toDouble()) + "Rs."}\n"+
                                    "Effective Tax Rate -: ${String.format("%.02f", longTerm.shortTermTax.toString().toDouble()) + "%"}"
                            mBinding.ruleOfTds.visibility = View.GONE
                            mBinding.result.visibility = View.VISIBLE

                    }else{

                        if (!longTerm.shortTermTax.equals("0")){
                            mBinding.result.visibility = View.GONE
                            mBinding.ruleOfTds.visibility = View.VISIBLE
                            mBinding.ruleOfTds.text = "${resources.getString(R.string.lossMessage)}"
                        }else{

                            val finalValue  = if (profit < 0) 0 else profit
                            mBinding.ruleOfTds.text = "For tax computation, Your Gain of Rs.${String.format("%.02f", finalValue.toDouble()).replace("-", "")} will be added in your total income and tax will be applicable at effective tax rate"

                            mBinding.result.visibility = View.GONE
                            mBinding.ruleOfTds.visibility = View.VISIBLE


                        }
                    }
                }

            } else {
                // show message
                Toast.makeText(
                    this,
                    "Sold date can't before buy date",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
}