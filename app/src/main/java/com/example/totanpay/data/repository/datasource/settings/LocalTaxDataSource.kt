package com.example.totanpay.data.repository.datasource.settings

interface LocalTaxDataSource {
    fun setTax(tax:String)
    fun getTax():String
}