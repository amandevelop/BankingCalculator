package com.emicalculator

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Location
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.CallLog
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emicalculator.CapitalGain.CapitalGainTax
import com.emicalculator.databinding.ActivityStartBinding
import com.emicalculator.stampDuty.StampDuty
import com.emicalculator.tdsCalculator.TDS_Calculator
import java.lang.Long
import java.math.BigInteger
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.ByteOrder
import java.util.*
import kotlin.Array
import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Double
import kotlin.Float
import kotlin.Int
import kotlin.IntArray
import kotlin.String
import kotlin.apply


class StartActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        Log.d("countries", "${resources.getString(R.string.google_maps_key)}")
        if(Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.ADB_ENABLED, 0) == 1) {
            // debugging enabled
            Log.d("debuging", "enabled")
            Toast.makeText(this, "Debuging enable", Toast.LENGTH_SHORT).show()

        } else {
            Log.d("debuging", "disable")
            Toast.makeText(this, "Debuging disable", Toast.LENGTH_SHORT).show()

            //;debugging does not enabled
        }

        mBinding.apply {

            ll1.setOnClickListener {
                MainActivity.isEnable = 2
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
            }
            ll2.setOnClickListener {
                MainActivity.isEnable = 1
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
            }
            ll3.setOnClickListener {
                startActivity(Intent(this@StartActivity, SIPCalculator::class.java))
            }
            ll4.setOnClickListener {
                startActivity(Intent(this@StartActivity, FDCalculator::class.java))
            }
            ll5.setOnClickListener {
                startActivity(Intent(this@StartActivity, RDCalculator::class.java))
            }
            ll6.setOnClickListener {
                startActivity(Intent(this@StartActivity, PPFCalculatorActivity::class.java))
            }
            ll7.setOnClickListener {
                MainActivity.isEnable = 0
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
            }
            ll8.setOnClickListener {
                MainActivity.isEnable = 3
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
            }
            ll10.setOnClickListener {
                startActivity(Intent(this@StartActivity, GSTCalculator::class.java))
            }
            ll11.setOnClickListener {
                startActivity(Intent(this@StartActivity, Gratuity_calculator::class.java))
            }
            ll12.setOnClickListener {
                startActivity(Intent(this@StartActivity, CompareLoan::class.java))
            }
            ll13.setOnClickListener {
                startActivity(Intent(this@StartActivity, StampDuty::class.java))
            }
            ll14.setOnClickListener {
                startActivity(Intent(this@StartActivity, TDS_Calculator::class.java))
            }
            ll15.setOnClickListener {
                startActivity(Intent(this@StartActivity, CapitalGainTax::class.java))
            }
            hraCalculator.setOnClickListener {
                startActivity(Intent(this@StartActivity, HRACalculator::class.java))
            }

            eightyUCalculator.setOnClickListener {
                startActivity(Intent(this@StartActivity, EightyUCalculator::class.java))
            }





        }


    }

    private fun getAllCountries(): ArrayList<String> {

        val locales = Locale.getAvailableLocales()
        val countries = ArrayList<String>()
        for (locale in locales) {
            val country = locale.displayCountry
            if (country.trim { it <= ' ' }.isNotEmpty() && !countries.contains(country)) {
                countries.add(country)
            }
        }
        countries.sort()

        return countries
    }
    private fun getSymbol(countryCode: String?): String? {
        val availableLocales = Locale.getAvailableLocales()
        for (i in availableLocales.indices) {
            if (availableLocales[i].country == countryCode
            ) return Currency.getInstance(availableLocales[i]).currencyCode
        }
        return ""
    }


    /**
     * A method for getting a country's code from the country name
     * e.g Nigeria - NG
     */

    private fun getCountryCode(countryName: String) = Locale.getISOCountries().find { Locale("", it).displayCountry == countryName }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }


    @SuppressLint("HardwareIds")
    private fun getSystemDetail(): String {
        return "Brand: ${Build.BRAND} \n" +
                "DeviceID: ${
                    Settings.Secure.getString(
                        contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                } \n" +
                "DeviceID: ${
                    Settings.Secure.getString(
                        contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                } \n" +
                "Model: ${Build.MODEL} \n" +
                "ID: ${Build.ID} \n" +
                "SDK: ${Build.VERSION.SDK_INT} \n" +
                "Manufacture: ${Build.MANUFACTURER} \n" +
                "Brand: ${Build.BRAND} \n" +
                "User: ${Build.USER} \n" +
                "Type: ${Build.TYPE} \n" +
                "Base: ${Build.VERSION_CODES.BASE} \n" +
                "Incremental: ${Build.VERSION.INCREMENTAL} \n" +
                "Board: ${Build.BOARD} \n" +
                "Host: ${Build.HOST} \n" +
                "FingerPrint: ${Build.FINGERPRINT} \n" +
                "Version Code: ${Build.VERSION.RELEASE}"
    }
    private fun available(name: String): Boolean {
        var available = true
        try {
            // check if available
            packageManager.getPackageInfo(name, 0)
        } catch (e: PackageManager.NameNotFoundException) {

            available = false
        }
        return available
    }

    fun appInstalledOrNot(mActivity: Activity, packageName: String?): Boolean {
        val pm = mActivity.packageManager
        try {
            pm.getPackageInfo(packageName!!, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false
    }

    private fun distance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val loc1 = Location("")
        loc1.setLatitude(lat1)
        loc1.setLongitude(lng1)
        val loc2 = Location("")
        loc2.setLatitude(lat2)
        loc2.setLongitude(lng2)
        val distanceInMeters: Float = loc1.distanceTo(loc2)
        return (distanceInMeters / 1000).toDouble()
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
     fun wifiIpAddress(context: Context): String? {
        val wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager
        var ipAddress = wifiManager.connectionInfo.ipAddress

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress)
        }
        val ipByteArray: ByteArray = BigInteger.valueOf(ipAddress.toLong()).toByteArray()
        val ipAddressString: String?
        ipAddressString = try {
            InetAddress.getByAddress(ipByteArray).getHostAddress()
        } catch (ex: UnknownHostException) {
            Log.e("WIFIIP", "Unable to get host address.")
            null
        }
        return ipAddressString
    }
    private fun getCallDetails(): String? {
        val sb = StringBuffer()
        val managedCursor: Cursor = managedQuery(
            CallLog.Calls.CONTENT_URI, null,
            null, null, null
        )
        val number: Int = managedCursor.getColumnIndex(CallLog.Calls.NUMBER)
        val type: Int = managedCursor.getColumnIndex(CallLog.Calls.TYPE)
        val date: Int = managedCursor.getColumnIndex(CallLog.Calls.DATE)
        val duration: Int = managedCursor.getColumnIndex(CallLog.Calls.DURATION)
        sb.append("Call Details :")
        while (managedCursor.moveToNext()) {
            val phNumber: String = managedCursor.getString(number)
            val callType: String = managedCursor.getString(type)
            val callDate: String = managedCursor.getString(date)
            val callDayTime = Date(Long.valueOf(callDate))
            val callDuration: String = managedCursor.getString(duration)
            var dir: String? = null
            val dircode = callType.toInt()
            when (dircode) {
                CallLog.Calls.OUTGOING_TYPE -> dir = "OUTGOING"
                CallLog.Calls.INCOMING_TYPE -> dir = "INCOMING"
                CallLog.Calls.MISSED_TYPE -> dir = "MISSED"
            }
            sb.append(
                """
Phone Number:--- $phNumber 
Call Type:--- $dir 
Call Date:--- $callDayTime 
Call duration in sec :--- $callDuration"""
            )
            sb.append("\n----------------------------------")
        }
        managedCursor.close()
        return sb.toString()
    }

}