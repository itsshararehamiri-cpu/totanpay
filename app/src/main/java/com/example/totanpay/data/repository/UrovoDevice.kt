package com.example.totanpay.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.example.totanpay.R
import com.urovo.sdk.magcard.MagCardReaderImpl
import com.urovo.sdk.magcard.listener.MagCardListener
import com.urovo.sdk.pinpad.PinPadProviderImpl
import com.urovo.sdk.pinpad.listener.PinInputListener
import com.urovo.sdk.pinpad.utils.Constant
import com.urovo.sdk.print.PrinterProviderImpl
import com.urovo.sdk.utils.BytesUtil
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class UrovoDevice @Inject constructor(val context: Context) : IDevice {
    val INDEX_TEK = 9
    val INDEX_MK = 10
    val INDEX_WK = 11
    val INDEX_P = 12
    val pinpad: PinPadProviderImpl = PinPadProviderImpl.getInstance()
    override val model: String
        get() = Build.MODEL

    override fun writeTekKey(tekKey: ByteArray) {
        pinpad!!.loadTEK(
            INDEX_TEK,
            tekKey,
            null
        )    }

    init {
        println("model->$model")
    }

    override fun writeMasterKey(masterKey: ByteArray) {
        pinpad!!.loadMainKey(
            INDEX_MK,
            masterKey,
            null
        )
      //  pinpad.loadEncryptMainKey(INDEX_TEK, INDEX_MK, masterKey, null)
    }

    override fun clearMasterKey() {
        pinpad!!.loadMainKey(
            INDEX_MK,
            null,
            null
        )
    }

    override fun writeMacKey(macKey: ByteArray) {
        //if(!isLogon)
            pinpad.loadWorkKey(Constant.KeyType.MAC_KEY, INDEX_MK, INDEX_WK, macKey, null)
      //  else         pinpad.loadWorkKey(Constant.KeyType.MAC_KEY, INDEX_MK, INDEX_MK, macKey, null)

    }

    override fun clearMacKey() {
        pinpad.loadWorkKey(Constant.KeyType.MAC_KEY, INDEX_MK, INDEX_WK, null, null)

    }
    override fun writeDataKey(dataKey: ByteArray) {
        pinpad.loadWorkKey(Constant.KeyType.TD_KEY, INDEX_MK, INDEX_TEK, dataKey, null)
    }

    override fun writePinKey(pinKey: ByteArray) {
        pinpad.loadWorkKey(Constant.KeyType.PIN_KEY, INDEX_MK, INDEX_P, pinKey, null)

    }

    override fun getMac(data: ByteArray): ByteArray {
        var pinpad: PinPadProviderImpl? = null
        pinpad = PinPadProviderImpl.getInstance()
        return pinpad!!.calcMAC(INDEX_WK  , data, 0x11)
    }
    override fun readCard(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        Log.d("TAG", "readCard() called with: onSuccess = $onSuccess, onError = $onError")
        val magCardReader = MagCardReaderImpl.getInstance()
        magCardReader.searchCard(5000, object : MagCardListener {
            override fun onSuccess(p0: Bundle?) {
                Log.d("TAG", "onSuccess() called with: p0 = $p0")
                println("yyy->${p0.toString()}")
                if (p0 != null && p0?.getString("TRACK2") != null)
                    onSuccess(p0!!.getString("TRACK2")!!)
                else onError("ff")// TODO:  
            }

            override fun onError(p0: Int, p1: String?) {
                Log.d("TAG", "onError() called with: p0 = $p0, p1 = $p1")
                println("yyyy->$p0")
                println("yyyy->$p1")
                onError(p1 ?: "$p0")

            }

            override fun onTimeout() {
                Log.d("TAG", "onTimeout() called")
                println("yyyy->ooo")
                onError("timeout")


            }

        })

    }

    override fun getPinBlock(
        pan: String, onError: (String) -> Unit,
        onInput: (Int) -> Unit, onConfirm: (String) -> Unit,
        onCanecl: () -> Unit, onTimeOut: () -> Unit
    ) {
        var pinpadBundle = Bundle()
        pinpadBundle = Bundle()
        pinpadBundle.putString("cardNo", pan)
        pinpadBundle.putBoolean("sound", true)
        pinpadBundle.putBoolean("bypass", true)
        pinpadBundle.putString("supportPinLen", "0,4,5,6,7,8,9,10,11,12")
        pinpadBundle.putBoolean("FullScreen", true)
        pinpadBundle.putBoolean("onlinePin", true)
        pinpadBundle.putInt("PINKeyNo", INDEX_P)
        pinpadBundle.putLong("timeOutMS", (30 * 1000).toLong())
        pinpadBundle.putBoolean("randomKeyboard", false)
        pinpadBundle.putString("title", "Security Keyborad")
        pinpadBundle.putString("message", "Please enter your pin")
        pinpadBundle.putString("cancelText", "Cancel1")
        pinpadBundle.putString("deleteText", "Delete1")
        pinpadBundle.putString("okText", "Ok1")
        pinpad.getPinBlockEx(pinpadBundle, object : PinInputListener {
            override fun onInput(p0: Int, p1: Int) {
                onInput(p0)
                println("onInput->$p1")
                println("onInput->$p0")

            }

            override fun onConfirm(p0: ByteArray?, p1: Boolean) {
                println("onConfirm->$p1")
                println("onCgggonfirm->$p0")

                println("onConfiggrm->${String(p0!!)}")

                if (p0 != null)
                    onConfirm(String(p0))
            }

            override fun onConfirm_dukpt(p0: ByteArray?, p1: ByteArray?) {
                println("onConfirm_dukpt")
            }

            override fun onCancel() {
                onCanecl()
            }

            override fun onTimeOut() {
                onTimeOut()
            }

            override fun onError(p0: Int) {
                onError("p0->$p0")
            }
        })

    }

    override fun getSerial(): String {
        return "92261946156409"//"92261946156409"
    }

    override fun requestDecryptData(data: ByteArray?): ByteArray? {
        var out2 = ByteArray(data?.size ?: 0)
        val ret = pinpad.calculateDes(
            Constant.DesMode.DEC,
            1,
            Constant.KeyType.ENCDEC_KEY,
            INDEX_TEK,
            data,
            out2
        )
        if (ret == 0) {
            Log.d(
                "", "response result: " + BytesUtil.bytes2HexString(out2) + "\n"
                        + "expected result: \n3632313939363034343434373634303032374430353036313031313532363431"
            )
        }

        return out2
    }


    override suspend fun print(bitmap: Bitmap, context: Context) {
                val mPrintManager: PrinterProviderImpl = PrinterProviderImpl.getInstance(context)
        try {
//            val imageData = getBitmapBytes(bitmap)
//          val  format = Bundle()
//            format.putInt("align", 1)
//            format.putInt("offset", 0)
//            format.putInt("height", 300)
//            format.putInt("width", 300);
//            mPrintManager.addImage(format, imageData)
         mPrintManager.initPrint()
//            val paintView = PaintView.getInstance(context)
//            val status = mPrintManager.status
            mPrintManager.setGray(6)
//            mPrintManager.appendBitmap(bitmap, 1);
//            mPrintManager.startPrint()
//            mPrintManager.close();

            var format = Bundle()
            val imageData = getBitmapBytes(bitmap)
            format = Bundle()
            format.putInt("align", 0)
            format.putInt("offset", 0)
          //  format.putInt("height", 300)
            mPrintManager.addImage(format, imageData)
            mPrintManager.setGray(6)
            val iRet = mPrintManager.startPrint()
            mPrintManager.close()
        } catch (e: Exception) {
            println("sssssssssss->${e.cause}")
            println("sssssssssss->${e.message}")

            e.printStackTrace()
        }

    }
    fun getBitmapBytes(bitmap: Bitmap): ByteArray? {
        var imageData: ByteArray? = null
        try {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            imageData = baos.toByteArray()
        } catch (e: java.lang.Exception) {
            // TODO: handle exception
            e.printStackTrace()
            return null
        }
        return imageData
    }
    private fun getLogoBitmap(context: Context, id: Int): Bitmap {
        val draw = context.resources.getDrawable(id) as BitmapDrawable
        val bitmap = draw.bitmap
        return bitmap
    }

}


