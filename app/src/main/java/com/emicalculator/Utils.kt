package com.emicalculator

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.emicalculator.CapitalGain.CapitalGainModel
import com.emicalculator.stampDuty.StampDutyModel
import com.emicalculator.tdsCalculator.TDS_Model
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun Activity.showSnackBar(view: View, result: String) {
    Snackbar.make(view, "$result", Snackbar.LENGTH_LONG)
        .setBackgroundTint(
            ContextCompat.getColor(
                this,
                R.color.app_color
            )
        )
        .show()
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun getAfterDateMonth(month: Int): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MONTH, (month).toInt())
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    val currentDateandTime: String = sdf.format(calendar.time)
    return currentDateandTime
}

fun getAfterDateYear(year: Int): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.YEAR, (year).toInt())
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    val currentDateandTime: String = sdf.format(calendar.time)
    return currentDateandTime
}


fun getFinancialYear(): String {
    val CurrentYear = Calendar.getInstance()[Calendar.YEAR]
    val CurrentMonth = Calendar.getInstance()[Calendar.MONTH] + 1

    return if (CurrentMonth < 4) {
        "${(CurrentYear - 1)} - $CurrentYear"
    } else {
        "${CurrentYear} - ${(CurrentYear + 1)}"
    }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

val financial_year = listOf<String>(
    getFinancialYear()
)
val status = listOf<String>(
    "Resident Individual",
    "other"
)

val percentageDisability = listOf<String>(
    "Normal(40% or more)",
    "Severe(80% or more)"
)
val listOfState = listOf<String>(
    "Select State",
    "Andhra Pradesh",
    "Arunachal Pradesh",
    "Assam",
    "Bihar",
    "Chhattisgarh",
    "Goa",
    "Gujarat",
    "Haryana",
    "Himachal Pradesh",
    "Jammu and Kashmir",
    "Jharkhand",
    "Karnataka",
    "Kerala",
    "Madhya Pradesh",
    "Maharashtra",
    "Manipur",
    "Meghalaya",
    "Mizoram",
    "Nagaland",
    "Odisha",
    "Punjab",
    "Rajasthan",
    "Sikkim",
    "Tamil Nadu",
    "Telangana",
    "Tripura",
    "Uttar Pradesh",
    "Uttarakhand",
    "West Bengal",
)

/** For TDS calculator */
val tds_section_list_rate = listOf<TDS_Model>(
    TDS_Model(
        "Section 192A - Payment of accumulated PF balance to an employee",
        "10",
        "50000",
        "If gross withdrawal before 5 year continuous services during the F.Y. exceeds Rs. 50,000 then TDS will be applicable @ 10 %",
        "30"
    ),
    TDS_Model(
        "Section 193 - Interest on Securities",
        "10",
        "10000",
        "If gross payment to the party during the F.Y. exceeds Rs. 10,000 then TDS will be applicable @ 10 %. In case where debentures are issued by the listed companies, no TDS shall be deducted upto Rs. 5000/-",
        "20"
    ),
    TDS_Model(
        "Section 194 - Dividend other than the Dividend as referred to in Section 115-O",
        "10",
        "5000",
        "If gross payment to the party during the F.Y. exceeds Rs. 5,000/- then TDS will be applicable @ 10 %.",
        "20"
    ),
    TDS_Model(
        "Section 194A - Interest from Banks",
        "10",
        "40000",
        "If gross payment to the party during the F.Y. exceeds Rs. 40,000 then TDS will be applicable @ 10 %. For senior citizen this limit is Rs. 50,000.",
        "20"
    ),
    TDS_Model(
        "Section 194A - Interest other than Banks",
        "10",
        "5000",
        "If gross payment to the party during the F.Y. exceeds Rs. 5,000/- then TDS will be applicable @ 10 %.",
        "20"
    ),
    TDS_Model(
        "Section 194B - Winnings from Lotteries/ Puzzles/ Game etc.",
        "30",
        "10000",
        "If gross payment to the party during the F.Y. exceeds Rs. 10,000 then TDS will be applicable @ 30 %.",
        "30"
    ),
    TDS_Model(
        "Section 194BB - Income by way of Winnings from Horse Races",
        "30",
        "10000",
        "If gross payment to the party during the F.Y. exceeds Rs. 10,000 then TDS will be applicable @ 30 %.",
        "30"
    ),
    TDS_Model(
        "Section 194DA - Maturity of Life Insurance Policy",
        "5",
        "100000",
        "If gross payment to the party during the F.Y. is Rs. 1,00,000 or more then TDS will be applicable @ 5 % & it is not applicable if amount is exempt u/s 10(10D).",
        "20"
    ),
    TDS_Model(
        "Section 194H - Commission or Brokerage",
        "5",
        "15000",
        "If gross payment to the party during the F.Y. exceeds Rs. 15,000 then TDS will be applicable @ 5%.",
        "20"
    ),
    TDS_Model(
        "Section 194I(a) - Rent on Plant & Machinery",
        "2",
        "240000",
        "If gross payment to the party during the F.Y. exceeds Rs. 2,40,000 then TDS will be applicable @ 2% in case of Plant& machinery and 10 % in case of land & building or furniture & fitting.",
        "20"
    ),
    TDS_Model(
        "Section 194I(b) - Rent on Land & building or Furniture & Fitting",
        "10",
        "240000",
        "If gross payment to the party during the F.Y. exceeds Rs. 2,40,000 then TDS will be applicable @ 2% in case of Plant& machinery and 10 % in case of land & building or furniture & fitting.",
        "20"
    ),
    TDS_Model(
        "Section 194IA - Payment on transfer of certain Immovable Property other than agricultural land",
        "1",
        "5000000",
        "If gross payment to the party during the F.Y. is Rs. 50,00,000 or more then TDS will be applicable @ 1 %.",
        "0"
    ),
    TDS_Model(
        "Section 194IB - Rent Payment to Landlord by Individuals",
        "5",
        "50000",
        "If monthly rent payment to the landlord exceeds Rs. 50,000 then TDS will be applicable @ 5 %. ",
        "5"
    ),
    TDS_Model(
        "Section 194J - Fees for Professional Services / Royalty etc. (Normally in all cases)",
        "10",
        "30000",
        "If gross payment to the party during the F.Y. exceeds Rs. 30,000 then TDS will be applicable @ 2%.",
        "20"
    ),
    TDS_Model(
        "Section 194J - Fees for technical services, Fees to Call centre operator",
        "2",
        "30000",
        "If gross payment to the party during the F.Y. exceeds Rs. 30,000 then TDS will be applicable @ 2%.",
        "20"
    ),
    TDS_Model(
        "Section 194K - Income in respect of units (UTI/MF)",
        "10",
        "5000",
        "If dividend amount during the F.Y. exceeds Rs. 5,000 then TDS will be applicable @ 10%. No TDS to be deducted on Income from Capital Gains on sale of MFs",
        "20"
    ),
    TDS_Model(
        "Section 194O - TDS on Payment by E-commerce Operator to E-commerce participant",
        "1",
        "5000000",
        " If sale of goods or provision of services during the F.Y. exceeds Rs. 5,00,000 then TDS will be applicable @ 1 %.",
        "5"
    )
)

val stampDutyRateList = listOf<StampDutyModel>(
    StampDutyModel("Andhra Pradesh", "5"),
    StampDutyModel("Assam", "8.25"),
    StampDutyModel("Arunachal Pradesh", "6"),
    StampDutyModel("Nagaland", "7.5"),
    StampDutyModel("Mizoram", "5"),
    StampDutyModel("Meghalaya", "9.37"),
    StampDutyModel("Manipur", "7"),
    StampDutyModel("Maharashtra", "3.88"),
    StampDutyModel("Madhya Pradesh", "7.5"),
    StampDutyModel("Kerala", "8.5"),
    StampDutyModel("Karnataka", "5"),
    StampDutyModel("Jharkhand", "7"),
    StampDutyModel("Himachal Pradesh", "8"),
    StampDutyModel("West Bengal", "7"),
    StampDutyModel("Uttarakhand", "8"),
    StampDutyModel("Uttar Pradesh", "14.5"),
    StampDutyModel("Tripura", "5"),
    StampDutyModel("Telangana", "5"),
    StampDutyModel("Tamil Nadu", "8"),
    StampDutyModel("Sikkim", "4"),
    StampDutyModel("Rajasthan", "10"),
    StampDutyModel("Punjab", "6"),
    StampDutyModel("Odisha", "14.7"),
    StampDutyModel("Gujarat", "7.5"),
    StampDutyModel("Goa", "8"),
    StampDutyModel("Haryana", "12.5"),
    StampDutyModel("Chandigarh", "6"),
    StampDutyModel("Bihar", "7"),
    StampDutyModel("Delhi", "13"),
    StampDutyModel("Chattisgarh", "7")










)


val isPanAvailable = listOf<String>(
    "Yes",
    "No"
)


/** For Capital Gain calculator */
val assests = listOf<CapitalGainModel>(
    CapitalGainModel(
        "stocks",
        "1",
        "15",
        "10"
    ),
    CapitalGainModel(
        "Equity oriented mutual funds",
        "1",
        "15",
        "10"
    ),
    CapitalGainModel(
        "Rest of the Mutual Funds",
        "3",
        "0",
        "10"
    ),
    CapitalGainModel(
        "Government and Corporate Bonds",
        "3",
        "0",
        "10"
    ),
    CapitalGainModel(
        "Gold",
        "3",
        "0",
        "10"
    ),
    CapitalGainModel(
        "Gold ETF",
        "3", "0",
        "10"
    ),
    CapitalGainModel(
        "Privately held stocks",
        "3",
        "0",
        "10"
    ),
    CapitalGainModel(
        "Property",
        "3",
        "0",
        "10"
    ),
)


fun scrollDown(view : View){
    val objectAnimator =
        ObjectAnimator.ofInt(view, "scrollY", 0, view.bottom).setDuration(2000)
    objectAnimator.start()

}
/**   Pending sections
TDS_Model("Section 194C - Payment to Contractor/Sub-Contractor ( In case of single payment )",""),
TDS_Model("Section 194C - Payment to Contractor/Sub-Contractor ( In case of during F.Y. ) ",""),
TDS_Model("Section 194D - TDS on insurance comission",""),
TDS_Model("Section 194J - Fees for Professional Services / Royalty etc. (Normally in all cases)",""),
TDS_Model("Section 194J - Fees for technical services, Fees to Call centre operator",""),
 */
