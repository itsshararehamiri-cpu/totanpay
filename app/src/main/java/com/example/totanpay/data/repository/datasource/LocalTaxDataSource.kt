package com.example.totanpay.data.repository.datasource

interface LocalTaxDataSource {
    fun setTax(tax:String)
    fun getTax():String
}