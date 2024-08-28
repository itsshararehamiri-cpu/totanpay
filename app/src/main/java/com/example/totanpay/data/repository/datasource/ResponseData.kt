package com.example.totanpay.data.repository.datasource

sealed class ResponseData<out T>(
    val data: T? = null,
    val error: String?=null
) {
    class Success<T>(data: T) : ResponseData<T>(data)
    class Error<T>(error:String?=null, data: T? = null) : ResponseData<T>(data, error)
}
/*
 data class Success<T>(val data: T) : Result<T>
    data class Error<T>(val data: T? = null, val message: String? = "") : Result<T>
 */

