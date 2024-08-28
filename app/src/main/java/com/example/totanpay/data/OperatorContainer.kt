package com.example.totanpay.data

import com.example.totanpay.R
import com.example.totanpay.ui.theme.TurquoiseBlue
import com.example.totanpay.ui.theme.White100

object OperatorContainer {
    fun getOperators():List<Operator>{
        val chargeTypes:MutableList<Operator> = mutableListOf()
        var chargeList:MutableList<String> = mutableListOf()
        chargeList.add("50000")
        chargeList.add("100000")
        chargeList.add("200000")
        chargeList.add("500000")
        chargeTypes.add(Operator(12,"همراه اول","MCI", chargeList,"# شماره رمز شارژ # *140*",
            R.drawable.ic_irancel, TurquoiseBlue
        ))
        chargeList = mutableListOf()
        chargeList.add("10000")
        chargeList.add("20000")
        chargeList.add("50000")
        chargeList.add("100000")
        chargeList.add("200000")
        chargeTypes.add(Operator(17,"رایتل","Rightel", chargeList,"#رمز شارژ*141*", R.drawable.ic_hamraheaval,
            White100
        ))
        chargeList = mutableListOf()
        chargeList.add("10000")
        chargeList.add("20000")
        chargeList.add("50000")
        chargeList.add("100000")
        chargeList.add("200000")
        chargeTypes.add(Operator(11,"ایرانسل","Irancell", chargeList,"# شماره رمز *141*", R.drawable.ic_ritel, White100))
        return chargeTypes
    }
    fun getOperator(code:Int):Operator{
        var operator:Operator=getOperators().get(0)
        getOperators().forEach {
            if(it.code.equals(code)){
                operator=it
            }
        }
        return operator
    }
}