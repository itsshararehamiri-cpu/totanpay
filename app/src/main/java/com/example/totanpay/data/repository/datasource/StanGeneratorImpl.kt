package com.example.totanpay.data.repository.datasource

import javax.inject.Inject

class StanGeneratorImpl @Inject constructor(    private val totanPayPreference: TotanPayPreference):StanGenerator {
    override fun generate(): Int {
        var stan:Int = totanPayPreference.getStan()
        stan++
        if(stan==Int.MIN_VALUE){
            stan=1
        }
        totanPayPreference.storeStan(stan)
        return stan
    }
}