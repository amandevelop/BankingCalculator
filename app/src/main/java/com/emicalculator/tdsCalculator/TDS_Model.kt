package com.emicalculator.tdsCalculator

data class TDS_Model(
    val Section : String,
    val tds_rate : String,
    val threshold_limit : String,
    val rule_of_tds : String,
    val withoutPanRate : String
)

