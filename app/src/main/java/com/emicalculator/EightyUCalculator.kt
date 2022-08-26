package com.emicalculator

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.emicalculator.databinding.ActivityEightyUcalculatorBinding

class EightyUCalculator : AppCompatActivity() {
    private lateinit var mBinding : ActivityEightyUcalculatorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityEightyUcalculatorBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        mBinding.financialYear.setOnClickListener(View.OnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.hpdialog)
            dialog.window!!.setLayout(1000, 1200)

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val editText = dialog.findViewById<EditText>(R.id.editText)
            val listView = dialog.findViewById<ListView>(R.id.list)
            val arrayAdapter: ArrayAdapter<String> =ArrayAdapter(
               this,
                R.layout.spinner_item, financial_year
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
                    mBinding.financialYear.setText("${arrayAdapter.getItem(i)}")
                    dialog.dismiss()
                }
        })


        mBinding.percentageOfDisablibility.setOnClickListener(View.OnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.hpdialog)
            dialog.window!!.setLayout(1000, 1200)

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val editText = dialog.findViewById<EditText>(R.id.editText)
            val listView = dialog.findViewById<ListView>(R.id.list)
            val arrayAdapter: ArrayAdapter<String> =ArrayAdapter(
                this,
                R.layout.spinner_item, percentageDisability
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
                    //    textView.setText(arrayAdapter.getItem(i))
                    //   getcity = textView.getText().toString()

                    mBinding.ll8.visibility = View.VISIBLE
                    mBinding.deducationAmnt.text = if (arrayAdapter.getItem(i).equals("Normal(40% or more)")){
                        "₹ 75,000"
                    }else {
                        "₹ 1,25,000"
                    }
                    mBinding.percentageOfDisablibility.setText("${arrayAdapter.getItem(i)}")
                    dialog.dismiss()
                }
        })

        mBinding.status.setOnClickListener(View.OnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.hpdialog)
            dialog.window!!.setLayout(1000, 1200)

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val editText = dialog.findViewById<EditText>(R.id.editText)
            val listView = dialog.findViewById<ListView>(R.id.list)
            val arrayAdapter: ArrayAdapter<String> =ArrayAdapter(
                this,
                R.layout.spinner_item, status
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
                    //    textView.setText(arrayAdapter.getItem(i))
                    //   getcity = textView.getText().toString()
                    if (arrayAdapter.getItem(i).equals("other")){

                        Toast.makeText(this, "Deduction can only be claimed by resident Indians and not NRIs", Toast.LENGTH_LONG).show()
                        mBinding.percentageOfDisablibility.visibility = View.GONE
                        mBinding.ll8.visibility = View.GONE
                        mBinding.tv5.visibility = View.GONE
                    }else{
                        mBinding.status.setText("${arrayAdapter.getItem(i)}")
                        mBinding.percentageOfDisablibility.visibility = View.VISIBLE
                        mBinding.tv5.visibility = View.VISIBLE
                    }
                    dialog.dismiss()
                }
        })


    }
}