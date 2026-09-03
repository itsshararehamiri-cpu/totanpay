package com.example.totanpay.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.example.totanpay.data.repository.device.KCV

interface DeviceRepository {
//    suspend fun readCard(
//        onSuccess: (String) -> Unit,
//        onError: (String) -> Unit,
//        onTimeOut: () -> Unit
//    )

    suspend fun getPinBlock(context: Context,
        pan: String, onError: (String) -> Unit,
        onInput: (Int) -> Unit, onConfirm: (String) -> Unit,
        onCancel: () -> Unit, onTimeOut: () -> Unit
    )

    fun getSerial(): String
    fun writeDataKey(key: String?)
    fun writePinKey(key: String?)
    fun writeMacKey(key: String?)
    fun writeMasterKey(key: String?)
    fun decrypt(hex2byte: ByteArray): ByteArray
    fun encrypt(hex2byte: ByteArray): ByteArray
    suspend fun setDateTime(toFormattedDate: String)
    suspend fun print(bitmap: Bitmap, context: Context, onSuccess: () -> Unit, onFailed: (String) -> Unit)
    suspend fun scan(
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onTimeout: () -> Unit,
        onCancel: () -> Unit
    )
    suspend fun getPrinterError(
        onFailed: (String) -> Unit
    )
    suspend fun getBatteryStatus(): Boolean
    suspend fun isInjectMaster(): Boolean
    fun requestDecryptData(data: ByteArray?): ByteArray?
    suspend fun batteryIsEnough(): Boolean
    suspend fun enableHome()
    suspend fun disableHome()
    suspend fun readCard(context: Context): CardReadResult
    suspend fun getKcv(): KCV
}