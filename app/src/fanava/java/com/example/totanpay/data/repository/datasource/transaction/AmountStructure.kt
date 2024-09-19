package com.example.totanpay.data.repository.datasource.transaction


class AmountStructure(
    val accountType: String,
    val currencyCode: String,
    val amount: String,
    val status: Char
)