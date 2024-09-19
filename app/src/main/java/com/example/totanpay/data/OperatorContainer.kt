package com.example.totanpay.data

import androidx.compose.ui.graphics.Color
import com.example.totanpay.R
import com.example.totanpay.ui.theme.Purpule
import com.example.totanpay.ui.theme.TurquoiseBlue

object OperatorContainer {
    fun getOperators():List<Operator>{
        val chargeTypes:MutableList<Operator> = mutableListOf()
        var chargeList:MutableList<String> = mutableListOf()
        chargeList.add("50000")
        chargeList.add("100000")
        chargeList.add("200000")
        chargeList.add("500000")
        chargeTypes.add(Operator(12,"همراه اول","MCI", chargeList,"",
            R.drawable.ic_hamraheaval, TurquoiseBlue
        ))//"# شماره رمز شارژ#*140*"
        chargeList = mutableListOf()
        chargeList.add("10000")
        chargeList.add("20000")
        chargeList.add("50000")
        chargeList.add("100000")
        chargeList.add("200000")
        chargeTypes.add(Operator(17,"رایتل","Rightel", chargeList,"", R.drawable.ic_ritel,
            Purpule
        ))
        chargeList = mutableListOf()
        chargeList.add("10000")
        chargeList.add("20000")
        chargeList.add("50000")
        chargeList.add("100000")
        chargeList.add("200000")
        chargeTypes.add(Operator(11,"ایرانسل","Irancell", chargeList,"", R.drawable.ic_irancel,
            Color.Yellow))
        return chargeTypes
    }
    fun getVoucherChargeMSG(code: Int):String{
        return when(code){
            11->"#رمز شارژ *141*"
            12->"#رمز شارژ # *140*"
            17->"#رمز شارژ*141*"
            else->""
        }
    }
}