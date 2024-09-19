package com.example.totanpay.data.repository.device

import android.content.Context
import android.graphics.Bitmap

interface IDevice {
    val INDEX_TEK: Int
    val INDEX_MK: Int
    val INDEX_WK: Int
    val INDEX_PIN: Int
    val hasKeyboard:Boolean
    val model: String
    fun writeMasterKey(masterKey: ByteArray, index: Int = INDEX_MK)
    fun isInjectMaster(): Boolean
    fun writeMacKey(macKey: ByteArray, index: Int = INDEX_WK)
    fun writeDataKey(dataKey: ByteArray)
    fun writePinKey(pinKey: ByteArray)
    fun getMac(data: ByteArray, index: Int = INDEX_WK): ByteArray
    fun readCard(onSuccess: (String) -> Unit, onError: (String) -> Unit, onTimeOut: () -> Unit)
    fun getPinBlock(
        pan: String, onError: (String) -> Unit,
        onInput: (Int) -> Unit, onConfirm: (String) -> Unit,
        onCancel: () -> Unit, onTimeOut: () -> Unit
    )

    fun getSerial(): String

    fun requestDecryptData(data: ByteArray?): ByteArray?
    suspend fun print(
        bitmap: Bitmap,
        context: Context,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    )

    fun encrypt(data: ByteArray): ByteArray?
    fun decrypt(data: ByteArray): ByteArray?
    fun powerOnIcCard(): Boolean
    fun powerOffIcCard()
    fun isIcCardDetect(): Boolean

   suspend fun sendApdu(byteArray: ByteArray): ByteArray?
    suspend fun setDateTime(dataTime: String)
    suspend fun getBatteryStatus(): Boolean
    fun disableHome()
    fun enableHome()
    suspend fun scan(
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onTimeout: () -> Unit,
        onCancel: () -> Unit
    )
}

