package com.example.totanpay.ui

private val ones = arrayOf(
    "", "یک", "دو", "سه", "چهار", "پنج", "شش", "هفت", "هشت", "نه"
)
private val teens = arrayOf(
    "ده", "یازده", "دوازده", "سیزده", "چهارده", "پانزده", "شانزده", "هفده", "هجده", "نوزده"
)
private val tens = arrayOf(
    "", "", "بیست", "سی", "چهل", "پنجاه", "شصت", "هفتاد", "هشتاد", "نود"
)
private val hundreds = arrayOf(
    "", "صد", "دویست", "سیصد", "چهارصد", "پانصد", "ششصد", "هفتصد", "هشتصد", "نهصد"
)
private val scales = arrayOf(
    "", "هزار", "میلیون", "میلیارد", "تریلیون", "کوادریلیون"
)

private fun threeDigitsToWords(number: Int): String {
    if (number == 0) return ""
    val parts = mutableListOf<String>()
    val hundredDigit = number / 100
    val remainder = number % 100
    if (hundredDigit != 0) parts.add(hundreds[hundredDigit])
    if (remainder in 10..19) {
        parts.add(teens[remainder - 10])
    } else {
        val tenDigit = remainder / 10
        val oneDigit = remainder % 10
        if (tenDigit != 0) parts.add(tens[tenDigit])
        if (oneDigit != 0) parts.add(ones[oneDigit])
    }
    return parts.joinToString(" و ")
}

fun numberToPersianWords(input: String): String {
    val cleaned = input.trim().replace(",", "")
    if (cleaned.isEmpty()) return ""
    val number = cleaned.toLongOrNull() ?: return ""
    if (number < 0) return ""
    if (number == 0L) return "صفر"

    var remaining = number
    val groups = mutableListOf<Int>()
    while (remaining > 0) {
        groups.add((remaining % 1000).toInt())
        remaining /= 1000
    }

    val parts = mutableListOf<String>()
    for (index in groups.indices.reversed()) {
        val groupValue = groups[index]
        if (groupValue == 0) continue
        val groupWords = threeDigitsToWords(groupValue)
        val scale = scales.getOrElse(index) { "" }
        parts.add(if (scale.isNotEmpty()) "$groupWords $scale" else groupWords)
    }
    return parts.joinToString(" و ")
}

fun amountInWords(amount: String, currency: String): String {
    val words = numberToPersianWords(amount)
    return if (words.isEmpty()) "" else "$words $currency"
}
