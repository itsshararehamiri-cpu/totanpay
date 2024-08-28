package com.example.totanpay.data

import androidx.compose.ui.graphics.Color

data class Operator(val code:Int, val persianName:String, val englishName:String, val chargeList:List<String>, val voucherChargeMSG:String
                    , val imaged: Int, val borderColor: Color, )
