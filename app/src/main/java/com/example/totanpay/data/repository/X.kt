//package com.example.totanpay.data.repository
//
//import android.content.Context
//import android.graphics.Bitmap
//import javax.inject.Inject
//
//class X @Inject constructor() : IDevice {
//    override val model: String
//        get() = ""
//
//    override fun writeMasterKey(masterKey: ByteArray) {
//
//    }
//
//    override fun clearMasterKey() {
//
//    }
//
//    override fun writeMacKey(macKey: ByteArray) {
//    }
//
//    override fun clearMacKey() {
//    }
//
//    override fun writeDataKey(dataKey: ByteArray) {
//    }
//
//    override fun writePinKey(pinKey: ByteArray) {
//    }
//
//    override fun getMac(data: ByteArray): ByteArray {
//return ByteArray(0)   }
//
//    override fun readCard(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
//    }
//
//    override fun getPinBlock(
//        pan: String,
//        onError: (String) -> Unit,
//        onInput: (Int) -> Unit,
//        onConfirm: (String) -> Unit,
//        onCanecl: () -> Unit,
//        onTimeOut: () -> Unit
//    ) {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun getSerial(): String {return ""
//    }
//
//    override fun requestDecryptData(data: ByteArray?): ByteArray? {
//        return ByteArray(0)
//    }
//
//    override suspend fun print(bitmap: Bitmap, context: Context) {
//
//    }
//
//}