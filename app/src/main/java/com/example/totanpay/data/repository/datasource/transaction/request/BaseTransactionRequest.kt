package com.example.totanpay.data.repository.datasource.transaction.request

sealed class BaseTransactionRequest(
    val serial: String,
    val appVersion: String,
    val nii: String,
    val stan: Int,val date:String,val time:String
)
//    constructor(stan: Int,  serial: String, appVersion: String, nii: String) {
//        this.stan = stan
//        this.serial = serial
//        this.appVersion = appVersion
//        this.nii = nii
//    }
//
//    constructor(serial: String, appVersion: String, nii: String, stan: Int) {
//        this.serial = serial
//        this.appVersion = appVersion
//        this.nii = nii
//        this.stan = stan
//    }
//}