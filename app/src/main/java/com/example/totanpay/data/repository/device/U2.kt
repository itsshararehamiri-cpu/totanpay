//package com.example.totanpay.data.repository.device
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.device.DeviceManager
//import android.graphics.Bitmap
//import android.os.Build
//import javax.inject.Inject
//
//
//class U2 @Inject constructor(val context: Context) : IDevice {
//    val TAG = "UrovoDevice"
//    private val cardType: Byte = 0
//    override val INDEX_TEK = 1
//    override val INDEX_MK = 1
//    override val INDEX_WK = 1
//    override val INDEX_PIN = 1
//    override val hasKeyboard: Boolean
//        get() = false
//    override val model: String
//        get() = Build.MODEL
//
//    override fun writeMasterKey(masterKey: ByteArray, index: Int) {
//
//    }
//
//    override fun isInjectMaster(): Boolean {
//        return false
//    }
//
//    override fun writeMacKey(macKey: ByteArray, index: Int) {
//    }
//
//    override fun writeDataKey(dataKey: ByteArray) {
//    }
//
//    override fun writePinKey(pinKey: ByteArray) {
//    }
//
//    override fun getMac(data: ByteArray, index: Int): ByteArray {
//        return ByteArray(0)
//    }
//
//    override fun readCard(
//        onSuccess: (String) -> Unit,
//        onError: (String) -> Unit,
//        onTimeOut: () -> Unit
//    ) {
//
//    }
//
//    override fun getPinBlock(
//        pan: String, onError: (String) -> Unit,
//        onInput: (Int) -> Unit, onConfirm: (String) -> Unit,
//        onCancel: () -> Unit, onTimeOut: () -> Unit
//    ) {
//
//
//    }
//
//    @SuppressLint("MissingPermission")
//    override fun getSerial(): String {
//        //return  "92261946156409"//"92261946156409"
//        //  return "92261946156409"
//        //return "92261946154710"
//        //return "92261946155760"
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            //  "92261946155760"
//            "98282013260916"
//            // Build.getSerial()
//        } else {
//            DeviceManager().deviceId
//        }
//    }
//
//    override fun requestDecryptData(data: ByteArray?): ByteArray? {
//        return null
//    }
//
//
//    override suspend fun print(
//        bitmap: Bitmap,
//        context: Context,
//        onSuccess: () -> Unit,
//        onFailed: (String) -> Unit
//    ) {
//
//
//    }
//
//    override fun encrypt(data: ByteArray): ByteArray? {
//        return null
//    }
//
//    override fun decrypt(data: ByteArray): ByteArray? {
//        return null
//    }
//
//    override fun powerOnIcCard(): Boolean {
//
//        return false
//
//    }
//
//    override fun powerOffIcCard() {
//
//    }
//
//    private fun getBitMapBytes(bitmap: Bitmap): ByteArray? {
//        return null
//    }
//
//    override fun isIcCardDetect(): Boolean {
//
//        return false
//    }
//
//    override suspend fun sendApdu(byteArray: ByteArray): ByteArray? {
//
//        return null
//
//    }
//
//    override suspend fun setDateTime(dataTime: String) {
//
//    }
//
//
//    override suspend fun getBatteryStatus(): Boolean {
//        return true
//    }
//
//    override fun disableHome() {
//
//    }
//
//    override fun enableHome() {
//
//    }
//
//    override suspend fun scan(
//        context: Context,
//        onSuccess: (String) -> Unit,
//        onError: (String) -> Unit,
//        onTimeout: () -> Unit,
//        onCancel: () -> Unit
//    ) {
//
//    }
//    /*
//
//     */
//}