package com.example.totanpay.data.repository

import android.content.Context
import android.graphics.Bitmap

interface IDevice {
     abstract val model:String
    fun writeTekKey(masterKey: ByteArray)//Load TEK, and TEK is the key for encrypting main key.

    fun writeMasterKey(masterKey: ByteArray)
    fun clearMasterKey()
    fun writeMacKey(macKey: ByteArray)
    fun clearMacKey()

    fun writeDataKey(dataKey: ByteArray)
    fun writePinKey(pinKey: ByteArray)
    fun getMac(data: ByteArray): ByteArray
    fun readCard(onSuccess:(String)->Unit,onError:(String)->Unit)
    fun getPinBlock(pan:String, onError: (String) -> Unit,
                    onInput:(Int)->Unit
                    ,onConfirm:(String)->Unit,
                    onCanecl: () -> Unit,onTimeOut:()->Unit)
    fun getSerial():String

    fun requestDecryptData(data: ByteArray?): ByteArray?
  suspend  fun print(bitmap:Bitmap,context:Context)
}

enum class PROTOCOLS {

}