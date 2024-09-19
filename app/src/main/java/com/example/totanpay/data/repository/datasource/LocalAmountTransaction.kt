package com.example.totanpay.data.repository.datasource

interface LocalAmountTransaction {
    fun setMaximumAmount(amount:String)
    fun getMaximumAmount():String
}