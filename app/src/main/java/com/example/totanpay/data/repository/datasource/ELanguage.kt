package com.example.totanpay.data.repository.datasource

import com.example.totanpay.data.CurrentLanguage

data class ELanguage(val isFarsi:Boolean)
fun ELanguage.toCurrentLanguage(): CurrentLanguage {
    return CurrentLanguage(isFarsi = this.isFarsi)
}