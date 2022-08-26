package com.emicalculator.invoice

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import com.emicalculator.R
import com.emicalculator.databinding.ActivityInvoiceGeneratorBinding
import com.emicalculator.databinding.AddItemBillBinding
import com.emicalculator.hideKeyboard
import java.io.File
import java.util.*


data class ItemModel(
    var itemDescription: String = "",
    var itemPricePerItem: String = "",
    var itemQuantity: String = "",
    var itemRateofGST: String = "",
    var itemFinalAmount: String = ""
)

class InvoiceGenerator : AppCompatActivity() {
    private lateinit var arrayAdapter: InvoiceItemAdapter
    private val itemList = ArrayList<ItemModel>()
    private val listString = ArrayList<ItemModel>()
    var PERMISSION_CODE = 101
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSION_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        WRITE_EXTERNAL_STORAGE
    )
    private lateinit var mBinding: ActivityInvoiceGeneratorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityInvoiceGeneratorBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        //  addItem()

        arrayAdapter = InvoiceItemAdapter(itemList)
        mBinding.itemList.adapter = arrayAdapter

        mBinding.addItem.setOnClickListener {
            it.clearFocus()
            hideKeyboard(it)
            addItem()
        }

        if (checkPermissions()) {
            // Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            this, R.style.customTHemer,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                mBinding.invoiceDate.setText(""+dayOfMonth+"/"+(monthOfYear + 1) +"/"+year)

            },
            year,
            month,
            day
        )

        mBinding.invoiceDate.setOnClickListener {
            it.clearFocus()
            hideKeyboard()
            dpd.show()
        }
        mBinding.generatePDF.setOnClickListener {

            hideKeyboard(it)

            currentFocus?.clearFocus()
            mBinding.generatePDF.visibility = View.GONE
            mBinding.addItem.visibility = View.GONE


            try {
                if (checkPermissions()) {
                    generatePDF()
                    // Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show()
                } else {
                    requestPermission()
                }


            } catch (e: Exception) {
                Log.d("exceptiondfs", e.localizedMessage)
            }
        }
    }

    fun generatePDF() {
        //  val inflater = LayoutInflater.from(this)
        val view = mBinding.pdfView.root
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.display?.getRealMetrics(displayMetrics)
            displayMetrics.densityDpi

        } else {
            this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        }
        view.measure(
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.widthPixels, View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.heightPixels, View.MeasureSpec.EXACTLY
            )
        )
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        val bitmap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        val pdfDocument = PdfDocument()
        val pageInfo =
            PdfDocument.PageInfo.Builder(displayMetrics.widthPixels, displayMetrics.heightPixels, 1)
                .create()
        val page = pdfDocument.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap, 0F, 0F, null)

        pdfDocument.finishPage(page)
        val fileName = "docssss${System.currentTimeMillis()}.pdf"
        val file = File(
            Environment.getExternalStorageDirectory().getPath() + "/Download",
            fileName
        )
      Log.d("filepath", file.absolutePath)
        pdfDocument.writeTo(file.outputStream())
        pdfDocument.close()
       Handler().postDelayed({
          openPDF(file)
       },1000)
        Toast.makeText(this, "Pdf Generated Successfull", Toast.LENGTH_SHORT).show()

        mBinding.generatePDF.visibility = View.VISIBLE
        mBinding.addItem.visibility = View.VISIBLE
    }




    private fun addItem() {
        val dialog = Dialog(this)
        val addBillBinding = AddItemBillBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(addBillBinding.root)
        dialog.window!!.setLayout(1000, 1500)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        var itemAmount = 0.0
        var itemQuantity = 0.0
        var itemRateofInterest = 0.0
        addBillBinding.itemAmount.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                itemAmount = text.toString().toDouble()

                val price = itemAmount * itemQuantity
                val rate =
                    if (itemRateofInterest == 0.0) 0.0 else ((price * itemRateofInterest) / 100)
                val finalAmt = price + rate
                addBillBinding.finalAmount.text = "  ₹ " + String.format("%.02f", finalAmt)
            } else {
                itemAmount = 0.0
            }
        }
        addBillBinding.itemQuantity.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                itemQuantity = text.toString().toDouble()

                val price = itemAmount * itemQuantity
                val rate =
                    if (itemRateofInterest == 0.0) 0.0 else ((price * itemRateofInterest) / 100)
                val finalAmt = price + rate
                addBillBinding.finalAmount.text = "  ₹ " + String.format("%.02f", finalAmt)
            } else {
                itemQuantity = 0.0
            }
        }
        addBillBinding.itemRatepercent.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                itemRateofInterest = text.toString().toDouble()

                val price = itemAmount * itemQuantity
                val rate =
                    if (itemRateofInterest == 0.0) 0.0 else ((price * itemRateofInterest) / 100)
                val finalAmt = price + rate
                addBillBinding.finalAmount.text = "  ₹ " + String.format("%.02f", finalAmt)
            } else {
                itemRateofInterest = 0.0
            }
        }
        addBillBinding.btnCaculate.setOnClickListener {
            if (addBillBinding.itemDescription.text.isNullOrEmpty()) {
                addBillBinding.itemDescription.setError("Please enter description")
            } else if (addBillBinding.itemAmount.text.isNullOrEmpty()) {
                addBillBinding.itemAmount.setError("Please enter amount per item")
            } else if (addBillBinding.itemQuantity.text.isNullOrEmpty()) {
                addBillBinding.itemQuantity.setError("Please enter item quantity")
            } else if (addBillBinding.itemRatepercent.text.isNullOrEmpty()) {
                addBillBinding.itemRatepercent.setError("Please enter rate of interest")
            } else if ( itemRateofInterest > 28)
                addBillBinding.itemRatepercent.setError("Please enter rate of interest")
            else {
                hideKeyboard(it)
                //   listString.add("${listString.size+1} - ${addBillBinding.itemDescription.text.toString()} - $itemQuantity - $itemAmount - $itemRateofInterest % - ${addBillBinding.finalAmount.text.toString()}")
                val item = ItemModel(
                    addBillBinding.itemDescription.text.toString(),
                    String.format("%.02f", itemAmount).toString(),
                    String.format("%.02f", itemQuantity).toString(),
                    String.format("%.02f", itemRateofInterest).toString(),
                    addBillBinding.finalAmount.text.toString()
                )


                listString.add(item)
                arrayAdapter.addItem(item)
                dialog.dismiss()
            }


        }


    }

    fun checkPermissions(): Boolean {

        var writeStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        var readStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PERMISSION_CODE
        )

    }
    private fun openPDF(file : File) {

        // Get the File location and file name.
        Log.d("pdfFIle", "" + file)

        // Get the URI Path of file.
        val uriPdfPath =
            FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", file)
        Log.d("pdfPath", "" + uriPdfPath)

        // Start Intent to View PDF from the Installed Applications.
        val pdfOpenIntent = Intent(Intent.ACTION_VIEW)
        pdfOpenIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        pdfOpenIntent.clipData = ClipData.newRawUri("", uriPdfPath)
        pdfOpenIntent.setDataAndType(uriPdfPath, "application/pdf")
        pdfOpenIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        try {
            startActivity(pdfOpenIntent)
        } catch (activityNotFoundException: ActivityNotFoundException) {
            Toast.makeText(this, "There is no app to load corresponding PDF", Toast.LENGTH_LONG)
                .show()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE) {

            if (grantResults.size > 0) {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()

                } else {

                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

}