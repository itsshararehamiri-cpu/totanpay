package com.example.totanpay.data.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import saman.zamani.persiandate.PersianDate
import java.time.LocalDateTime
import java.time.Month
import java.time.Year
import java.time.ZoneId
import java.util.TimeZone

fun String?.formatTime() : String{
    return if (this != null)
        with(this){"${take(2)}:${substring(2,4)}:${takeLast(2)}"}
    else ""
}
@RequiresApi(Build.VERSION_CODES.O)
fun getPersianDate(date_MMdd: String) : String{
    val localDateTime = LocalDateTime.of(
        Year.now().value,
        Month.of(date_MMdd.take(2).toIntOrNull() ?: 1),
        date_MMdd.takeLast(2).toIntOrNull() ?: 1,
         10,
        10
    )
    val pDate = PersianDate(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
    println("oii->${Gson().toJson(pDate)}")
    return "${pDate.shYear}/${if(pDate.shMonth<10)0 else ""}${pDate.shMonth}/${if(pDate.shDay<10)0 else ""}${pDate.shDay}"
}
//object DateUtil {
//    fun getCurrentDateTime():String{
//        val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
//        return sdf.format(Date())
//    }//yyyyMMdd_HHmmss
//
//    fun getPersianDate(date_MMdd: String) : String{
//        with(date_MMdd) {
//            val cal = PersianCalendar()
//            cal.set(2023, take(2).toInt() - 1, takeLast(2).toInt())
//            return cal.persianShortDate
//        }
//    }
//}