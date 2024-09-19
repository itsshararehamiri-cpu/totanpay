package com.example.totanpay.data.util

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.example.totanpay.R
import com.example.totanpay.data.Operator
import com.example.totanpay.ui.theme.Purpule
import com.example.totanpay.ui.theme.TurquoiseBlue
import org.json.JSONObject
import java.io.IOException
import kotlin.math.pow

fun getProductCode(amount:String,code:String):String{
    val amountL:Long = amount?.replace(",","")?.toLong()?:0
    val productAmnt = "${amountL.countTrailingZeroBits()}${(amountL / 10.0.pow(amountL.countTrailingZeroBits())).toLong()}"
    return "${code}$productAmnt"
}

fun getAllCharges(context: Context): List<Operator>? {
    val charges = mutableListOf<Operator>()
    try {
        val jsonString: String = loadChargeJSONFromAsset(context)!!
        val jsonObject = JSONObject(jsonString)
        val chargesArray = jsonObject.getJSONArray("charge")
        for (i in 0 until chargesArray.length()) {
            val charge = chargesArray.getJSONObject(i)
            val code = charge.getString("code")
            val cellPrefix = charge.getString("cellPrefix")
            val operatorName = charge.getString("operatorName")
            val operatorCode = charge.getString("operatorCode")
            val dynamicValue = charge.getString("dynamicValue")
            val temp = charge.getJSONArray("chargeList")
            val chargeList = mutableListOf<String>()
            for (i in 0..temp.length() - 1) {
                chargeList.add(temp.getInt(i).toString())
            }
            charges.add(
                Operator(
                    code=code.toInt()
                    , persianName = operatorName,
                    englishName = operatorName, voucherChargeMSG = "", borderColor =
                    when(code.toInt()){
                        12-> TurquoiseBlue
                        17-> Purpule
                        11->Color.Yellow
                        else->Color.Yellow
                    }, imaged = when(code.toInt()){
                        12->R.drawable.ic_hamraheaval
                        17->R.drawable.ic_ritel
                        11->R.drawable.ic_irancel
                        else->R.drawable.ic_hamraheaval
                    }, chargeList = chargeList
                )
            )
        }
        return charges
    } catch (e: Exception) {
        println("cause=>${e.cause}")
        println("message=>${e.message}")
        // LogFile.appendLogFull(e)
    }
    return null
}

fun loadChargeJSONFromAsset(context:Context): String? {
    var json: String? = null
    try {
        val inputStream = context?.assets!!.open("ChargeConfig.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer, Charsets.UTF_8)
    } catch (e: IOException) {
        println("cause=>${e.cause}")
        println("message=>${e.message}")

        e.printStackTrace()
    }
    return json
}
