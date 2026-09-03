package com.example.totanpay.data.repository.datasource.settings

interface LocalAmountTransaction {
    fun setMaximumAmount(amount:String)
    fun getMaximumAmount():String
}