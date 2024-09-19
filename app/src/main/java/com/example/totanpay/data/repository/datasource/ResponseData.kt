package com.example.totanpay.data.repository.datasource

import com.example.totanpay.data.repository.datasource.TotanPayException.Type

sealed class ResponseData<out T>(
    val data: T? = null,
    val error: TotanPayException?=null
) {
    class Success<T>(data: T) : ResponseData<T>(data)
    class Error<T>(error:TotanPayException?=null, data: T? = null ) : ResponseData<T>(data, error)
}





open class TotanPayException(val messageError:String?=null, val type:Type=Type.NORMAL){
    enum class Type{
       NORMAL,DISCONNECT,LOAD_SETTINGS,SWITCH_CONNECTION
    }
}//switchConnectionErrorMessage
 fun getNetworkIsNotAvailableMessage(): TotanPayException {
    return  TotanPayException(type = TotanPayException.Type.DISCONNECT)
}
fun getExceptionFromMessage(message:String?): TotanPayException {
    return  TotanPayException(type = TotanPayException.Type.NORMAL, messageError = message)
}
