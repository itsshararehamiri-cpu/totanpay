package com.example.totanpay.data.repository.device

import android.annotation.SuppressLint
import android.content.Context
import android.device.DeviceManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import com.example.totanpay.R
import com.example.totanpay.data.util.toEnglishNumber
import com.urovo.sdk.insertcard.InsertCardHandlerImpl
import com.urovo.sdk.magcard.MagCardReaderImpl
import com.urovo.sdk.magcard.listener.MagCardListener
import com.urovo.sdk.pinpad.PinPadProviderImpl
import com.urovo.sdk.pinpad.listener.PinInputListener
import com.urovo.sdk.pinpad.utils.Constant
import com.urovo.sdk.pinpad.utils.Constant.KeyType
import com.urovo.sdk.print.PrinterProviderImpl
import com.urovo.sdk.scanner.InnerScannerImpl
import com.urovo.sdk.scanner.listener.ScannerListener
import com.urovo.sdk.scanner.utils.Constant.CameraID.FRONT
import com.urovo.sdk.utils.BytesUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jpos.iso.ISOUtil
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class UrovoDevice @Inject constructor(val context: Context) : IDevice {
    val TAG = "UrovoDevice"
    private var icReader: InsertCardHandlerImpl? = null
    private val cardType: Byte = 0
    override val INDEX_TEK = 1
    override val INDEX_MK = 1
    override val INDEX_WK = 1
    override val INDEX_PIN = 1
    override val hasKeyboard: Boolean
        get() = false
    private val pinPad: PinPadProviderImpl = PinPadProviderImpl.getInstance()
    override val model: String
        get() = Build.MODEL
    private val printerMutex = Mutex()

    override fun writeMasterKey(masterKey: ByteArray, index: Int) {
        pinPad.deleteKey(KeyType.MAIN_KEY, INDEX_MK)
        pinPad.deleteKey(KeyType.MAC_KEY, INDEX_WK)
        pinPad.deleteKey(KeyType.ENCDEC_KEY, INDEX_TEK)
        pinPad.deleteKey(KeyType.PIN_KEY, INDEX_PIN)
        val result = pinPad.loadMainKey(
            index, masterKey, null
        )
        writeMacKey(masterKey, INDEX_WK)
    }

    override fun isInjectMaster(): Boolean {
        val result = pinPad.isKeyExist(KeyType.MAIN_KEY, INDEX_MK)
        return result
    }

    override fun writeMacKey(macKey: ByteArray, index: Int) {
        val result = pinPad.loadWorkKey(KeyType.MAC_KEY, INDEX_MK, index, macKey, null)
    }

    override fun writeDataKey(dataKey: ByteArray) {
        val result = pinPad.loadWorkKey(KeyType.ENCDEC_KEY, INDEX_MK, INDEX_TEK, dataKey, null)
    }

    override fun writePinKey(pinKey: ByteArray) {
        val result = pinPad.loadWorkKey(KeyType.PIN_KEY, INDEX_MK, INDEX_PIN, pinKey, null)
    }

    override fun getMac(data: ByteArray, index: Int): ByteArray {
        val result = pinPad.calcMAC(index.toString().toEnglishNumber().toInt(), data, 0x11)
        return result
    }

    override fun readCard(
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onTimeOut: () -> Unit
    ) {
        val magCardReader = MagCardReaderImpl.getInstance()
        try {
            magCardReader.searchCard(30, object : MagCardListener {
                override fun onSuccess(track: Bundle) {
                    if (!track.getString("TRACK2").isNullOrEmpty()) {
                        onSuccess(track.getString("TRACK2")!!)
                    } else {
                        onError(context.getString(R.string.reading_card_have_error))
                    }
                }

                override fun onError(error: Int, message: String) {
                    onError("$message $error")
                }

                override fun onTimeout() {
                    onTimeOut()
                }
            })
        } catch (e: Exception) {
            onError("${e.cause}")
        }
    }

    override fun getPinBlock(
        context: Context,
        pan: String,
        onError: (String) -> Unit,
        onInput: (Int) -> Unit,
        onConfirm: (String) -> Unit,
        onCancel: () -> Unit,
        onTimeOut: () -> Unit
    ) {
        val pinPadBundle = Bundle()
        pinPadBundle.putString("cardNo", pan.toEnglishNumber())
        pinPadBundle.putBoolean("sound", false)
        pinPadBundle.putBoolean("bypass", true)
        pinPadBundle.putString("supportPinLen", "0,4".toEnglishNumber())
        pinPadBundle.putBoolean("customization", false)
        pinPadBundle.putBoolean("FullScreen", true)
        pinPadBundle.putBoolean("onlinePin", true)
        pinPadBundle.putInt("PINKeyNo", INDEX_PIN)
        pinPadBundle.putLong(
            "timeOutMS", (30 * 1000).toLong().toString().toEnglishNumber().toLong()
        )
//        pinPadBundle.putString("title", context.getString(R.string.enter_pin))
//
//        pinPadBundle.putString("message", context.getString(R.string.required_by_customer))
//        pinPadBundle.putString("head", context.getString(R.string.voucher_pin))

        pinPadBundle.putString("infoLocation", "CENTER")
        pinPadBundle.putBoolean("randomKeyboard", false)
        pinPadBundle.putInt("fontSize", 30)
        //  val persianNumbers = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
        val persianNumbers = context.resources.getStringArray(R.array.numbers)
        pinPadBundle.putStringArray("numberText", persianNumbers)
        pinPadBundle.putBoolean("randomKeyboardLocation", false)
        val textSize = shortArrayOf(25, 25, 25, 25, 25, 25, 25)
        pinPadBundle.putShortArray("textSize", textSize)
        pinPadBundle.putString("cancelText", context.getString(R.string.cancel_))
        pinPadBundle.putString("deleteText", context.getString(R.string.delete))
        pinPadBundle.putString("okText", context.getString(R.string.confirm))
        pinPadBundle.putString("message", context.getString(R.string.plz_enter_pin))

        val textColor = intArrayOf(
            -0xa8de,  // عنوان اول
            -0xde690d,  // عنوان دوم
            Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK
        )

        pinPadBundle.putIntArray("textColor", textColor)
        pinPad.getPinBlockEx(pinPadBundle, object : PinInputListener {
            override fun onInput(p0: Int, p1: Int) {
                onInput(p0)
            }

            override fun onConfirm(p0: ByteArray?, p1: Boolean) {
                if (p0 != null) onConfirm(String(p0).toEnglishNumber())
            }

            override fun onConfirm_dukpt(p0: ByteArray?, p1: ByteArray?) {
                println("onConfirm_dukpt")
            }

            override fun onCancel() {
                onCancel()
            }

            override fun onTimeOut() {
                onTimeOut()
            }

            override fun onError(p0: Int) {
                onError("CODE ${p0}")
            }
        })

    }

    @SuppressLint("MissingPermission")
    override fun getSerial(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Build.getSerial()
            ///  "98282013260916"
            "98282013260916"
            //  "98262351307754"
        } else {
            DeviceManager().deviceId
        }
    }

    override fun requestDecryptData(data: ByteArray?): ByteArray? {
        val out2 = ByteArray(data?.size ?: 0)
        val ret = pinPad.calculateDes(
            Constant.DesMode.DEC,
            Constant.Algorithm.DES_ECB,
            KeyType.ENCDEC_KEY,
            INDEX_TEK,
            data,
            out2
        )
        if (ret == 0) {

        }

        return out2
    }


    override suspend fun print(
        bitmap: Bitmap, context: Context, onSuccess: () -> Unit, onFailed: (String) -> Unit
    ) {
        printerMutex.withLock {
            val printManager: PrinterProviderImpl? = PrinterProviderImpl.getInstance(context)
            try {
                if (printManager == null) {
                    onFailed(context.getString(R.string.unknown_error))
                } else {
                    printManager.initPrint()
                    var status = printManager.status
                    if (status != 0) {
                        when (status) {
                            240 -> onFailed(context.getString(R.string.no_paper))
                            247 -> {
                                onFailed(context.getString(R.string.printer_is_printing))
                            }

                            243 -> onFailed(context.getString(R.string.printer_be_broken))
                            else -> onFailed(context.getString(R.string.print_failed))
                        }
                        printManager.close()
                        return
                    }
                    printManager.setGray(6)
                    status = printManager.status
                    if (status != 0) {
                        when (status) {
                            240 -> onFailed(context.getString(R.string.no_paper))
                            247 -> onFailed(context.getString(R.string.printer_is_printing))
                            243 -> onFailed(context.getString(R.string.printer_be_broken))
                            else -> onFailed(context.getString(R.string.print_failed))
                        }
                        printManager.close()
                        return
                    }
                    val imageData = getBitMapBytes(bitmap)
                    val format = Bundle()
                    format.putInt("align", 1)
                    format.putInt("offset", 0)
                    format.putInt("height", bitmap.height + 5)
                    printManager.addImage(format, imageData)
                    printManager.feedLine(-1)
                    printManager.startPrint()
                    printManager.status
                    delay(1000)
                    status = printManager.status
                    if (status == 0) {
                        onSuccess()
                    } else {
                        when (status) {
                            240 -> onFailed(context.getString(R.string.no_paper))
                            247 -> onFailed(context.getString(R.string.printer_is_printing))
                            243 -> onFailed(context.getString(R.string.printer_be_broken))
                            else -> onFailed(context.getString(R.string.print_failed))
                        }
                    }
                    printManager.close()
                }
            } catch (e: Exception) {
                println("print exception cause->${e.cause}")
                println("print exception message->${e.message}")
                onFailed("${context.getString(R.string.unknown_error)} ${if (e.cause == null) "" else e.cause}")
                printManager?.close()
                e.printStackTrace()
            }
        }
    }

    override suspend fun getPrinterError(
    ): String {
        printerMutex.withLock {
            val printManager: PrinterProviderImpl? = PrinterProviderImpl.getInstance(context)
            try {
                if (printManager == null) {
                    return context.getString(R.string.unknown_error)
                } else {
                    printManager.initPrint()
                    var status = printManager.status
                    if (status != 0) {
                        when (status) {
                            240 -> return context.getString(R.string.no_paper)
                            247 -> {
                                return context.getString(R.string.printer_is_printing)
                            }

                            243 -> return context.getString(R.string.printer_be_broken)
                            else -> return context.getString(R.string.print_failed)
                        }
                        printManager.close()
                        return ""
                    }
                    printManager.close()
                }
            } catch (e: Exception) {
                println("print exception cause->${e.cause}")
                println("print exception message->${e.message}")
                return "${context.getString(R.string.unknown_error)} ${if (e.cause == null) "" else e.cause}"
                printManager?.close()
                e.printStackTrace()
            }
            return ""
        }
    }

    override fun encrypt(data: ByteArray): ByteArray? {
        val out = ByteArray(data.size)
        val result = pinPad.calculateDes(
            Constant.DesMode.ENC, Constant.Algorithm.DES_ECB, KeyType.MAIN_KEY, INDEX_MK, data, out
        )
        return out
    }

    override fun decrypt(data: ByteArray): ByteArray? {
        val out = ByteArray(data.size)
        val result = pinPad.calculateDes(
            Constant.DesMode.DEC,//DEC
            Constant.Algorithm.DES_ECB,//DES_ECB
            KeyType.MAIN_KEY, INDEX_MK, data, out
        )
        return out
    }

    override fun powerOnIcCard(): Boolean {
        icReader = InsertCardHandlerImpl.getInstance()
        val atrData = ByteArray(64)
        var ret = 0
        try {
            ret = icReader!!.powerUp(cardType, atrData)
            Log.d(TAG, "powerOnIcCard: $ret")

            if (ret == 0) return true
        } catch (e: RemoteException) {
            Log.e("TAG", "powerOnIcCard: ", e.cause)
            Log.e("TAG", "powerOnIcCard: " + e.message.toString())
            e.printStackTrace()
            // throw RuntimeException(e)
        }
        if (ret > 0) {
            val dataOut = ByteArray(ret)
            System.arraycopy(atrData, 0, dataOut, 0, ret)
        }
        return false

    }

    override fun powerOffIcCard() {
        try {
            //outputText("powerDown")
            val status = icReader!!.powerDown(0)
            Log.d(TAG, "powerOffIcCard: $status")

            //outputText("" + status)
        } catch (e: java.lang.Exception) {
            Log.d("TAG", "onClick cause " + e.cause)
            Log.d("TAG", "onClick message: " + e.message)
        }
    }

    private fun getBitMapBytes(bitmap: Bitmap): ByteArray? {
        val imageData: ByteArray?
        try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            imageData = byteArrayOutputStream.toByteArray()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return null
        }
        return imageData
    }

    override fun isIcCardDetect(): Boolean {
        try {
            val status = icReader!!.isCardIn
            Log.d(TAG, "isIcCardDetect: $status")
            if (status) return true
        } catch (e: Exception) {
            Log.d("TAG", "onClick:cause " + e.cause)
            Log.d("TAG", "onClick: message" + e.message)
        }
        return false
    }

    override suspend fun sendApdu(byteArray: ByteArray): ByteArray? {
        try {
            Log.d(TAG, "sendApdu: icReader=$icReader")
            Log.d(TAG, "sendApdu: apdu=${ISOUtil.hexString(byteArray)}")
            Log.d(TAG, "sendApdu: calling exchangeApdu slot=0")
            Log.d(TAG, "sendApdu request: ${ISOUtil.hexString(byteArray)}")
            delay(1000)
            if (icReader == null) {
                Log.e(TAG, "sendApdu: icReader is NULL")
                return null
            }

            val rspData = icReader!!.exchangeApdu(0, byteArray)

            if (rspData == null) {
                Log.e(TAG, "sendApdu: exchangeApdu returned NULL")
                return null
            }

            Log.d(TAG, "sendApdu response: ${ISOUtil.hexString(rspData)}")

            return rspData

        } catch (e: Exception) {
            Log.e(TAG, "sendApdu exception", e)
            return null
        }
    }

    override suspend fun setDateTime(dataTime: String) {
        try {
            val deviceManager = DeviceManager()
            deviceManager.setSettingProperty("persist-persist.sys.timezone", "GMT+03:30")
            deviceManager.setCurrentTime(dataTime.toLong())
        } catch (e: Exception) {
            Log.d(TAG, "setDateTime: ${e.cause}")
            Log.d(TAG, "setDateTime: ${e.message}")

            e.printStackTrace()
        }
    }


    override suspend fun getBatteryStatus(): Boolean {
        val batteryInfo = DeviceManager().batteryInfo
        val level: Int = batteryInfo.getInt("level")
        return batteryInfo.getInt("level") > 10
    }

    override fun disableHome() {
        try {
            val deviceManager = DeviceManager()
            deviceManager.rightKeyEnabled = true
            deviceManager.leftKeyEnabled = true
            deviceManager.homeKeyEnabled = false

            deviceManager.enableStatusBar(false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun enableHome() {
        try {
            val deviceManager = DeviceManager()
            deviceManager.rightKeyEnabled = true
            deviceManager.leftKeyEnabled = true
            deviceManager.homeKeyEnabled = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun scan(
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onTimeout: () -> Unit,
        onCancel: () -> Unit
    ) {
        val mInnerScanner: InnerScannerImpl = InnerScannerImpl.getInstance(context)
        val bundle = Bundle()
        bundle.putString(
            com.urovo.sdk.scanner.utils.Constant.Scankey.upPromptString,
            context.getString(R.string.plz_put_barcode_front_device)
        )
        bundle.putString(
            com.urovo.sdk.scanner.utils.Constant.Scankey.downPromptString, ""
        )
        bundle.putString(com.urovo.sdk.scanner.utils.Constant.Scankey.title, "")
        try {
            mInnerScanner.startScan(
                context,
                bundle,
                if (model.contains("i9100/W")) com.urovo.sdk.scanner.utils.Constant.CameraID.BACK else FRONT,
                40,
                object : ScannerListener {
                    override fun onSuccess(data: String?, byData: ByteArray) {
                        if (data != null) onSuccess(data)
                    }

                    override fun onError(error: Int, message: String?) {
                        onError("$error\\n$message")
                    }

                    override fun onTimeout() {
                        onTimeout()
                    }

                    override fun onCancel() {
                        onCancel()
                    }

                })
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            onError("${context.getString(R.string.unknown_error)} ${e.cause}")
        }
    }

    override suspend fun getKCv(): KCV {
        val masterResult = ByteArray(16)
        pinPad.calculateDes(
            0, 1, KeyType.MAIN_KEY, INDEX_MK, ISOUtil.hex2byte("0000000000000000"), masterResult
        )
        val dataResult = ByteArray(16)
        pinPad.calculateDes(
            0, 1, KeyType.ENCDEC_KEY, INDEX_TEK, ISOUtil.hex2byte("0000000000000000"), dataResult
        )
        val pinResult = ByteArray(16)
        pinPad.calculateDes(
            0, 1, KeyType.PIN_KEY, INDEX_PIN, ISOUtil.hex2byte("0000000000000000"), pinResult
        )
        val macResult = ByteArray(16)
        pinPad.calculateDes(
            0, 1, KeyType.MAC_KEY, INDEX_WK, ISOUtil.hex2byte("0000000000000000"), macResult
        )
        return KCV(
            master = ISOUtil.hexString(masterResult).take(6),
            data = ISOUtil.hexString(dataResult).take(6),
            pin = ISOUtil.hexString(pinResult).take(6),
            mac = ISOUtil.hexString(macResult).take(6)
        )
    }
}
