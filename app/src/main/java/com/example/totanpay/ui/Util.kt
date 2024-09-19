package com.example.totanpay.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText

fun priceFilter(
    text: String,
    thousandSeparator: (String) -> String = { text -> text.withThousands() },
): TransformedText {
    val out = thousandSeparator(text)
    val offsetMapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            val rightOffset = text.lastIndex - offset
            val commasToTheRight = rightOffset / 3
            return out.lastIndex - rightOffset - commasToTheRight
        }

        override fun transformedToOriginal(offset: Int): Int {
            val totalCommas = ((text.length - 1) / 3).coerceAtLeast(0)
            val rightOffset = out.length - offset
            val commasToTheRight = rightOffset / 4
            return (offset - (totalCommas - commasToTheRight))
        }
    }
    return TransformedText(AnnotatedString(out), offsetMapping)
}
private fun String.withThousands(separator: Char = ','): String {
    val original = this
    return buildString {
        original.indices.forEach { position ->
            val realPosition = original.lastIndex - position
            val character = original[realPosition]
            insert(0, character)
            if (position != 0 && realPosition != 0 && position % 3 == 2) {
                insert(0, separator)
            }
        }
    }
}
fun getPersianDateFrom(year: Int, month: Int, day: Int): String {
    val monthName = when (month) {
        1 -> "فروردین"
        2 -> "اردیبهشت"
        3 -> "خرداد"
        4 -> "تیر"
        5 -> "مرداد"
        6 -> "شهریور"
        7 -> "مهر"
        8 -> "آبان"
        9 -> "آذر"
        10 -> "دی"
        11 -> "بهمن"
        12 -> "اسفند"
        else -> ""
    }
    return "$day $monthName $year"
}
fun selectedDateIsSmallerOrEqualThanCurrenDate(year: Int,month: Int,day: Int,currentYear:Int,currentMonth:Int,currentDay: Int):Boolean{
    var flag=true
    if(year>currentYear)
    {
        flag=false
    }
    else if(year==currentYear){
        if(month>currentMonth){
            flag=false
        }
        else  if(month==currentMonth){
            if(day>currentDay)
                flag=false
        }
    }
    return flag
}