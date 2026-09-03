package com.example.totanpay.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.example.totanpay.data.repository.device.IDevice
import com.example.totanpay.data.repository.device.KCV
import com.example.totanpay.data.util.toEnglishNumber
import com.example.totanpay.receiver.DeviceEventsReceiver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jpos.iso.ISOUtil
import org.slf4j.LoggerFactory
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DeviceRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val device: IDevice,
) : DeviceRepository {
    init {
        device.disableHome()
    }

    //    override suspend fun readCard(
//        onSuccess: (String) -> Unit,
//        onError: (String) -> Unit,
//        onTimeOut: () -> Unit
//    ) {
//        withContext(ioDispatcher) {
//            logger.info("Card Swipe")
//            device.readCard(onSuccess, onError, onTimeOut)
//            device.readCard(
//                onSuccess = { track2 ->
//                    continuation.resume(Result.success(track2))
//                },
//                onError = { error ->
//                    continuation.resume(Result.failure(Throwable(error)))
//                }, onTimeOut = {
//                    continuation.resume(Result.failure(Throwable("")))
//                }
//            )
//        }
//    }
    override suspend fun readCard(context: Context): CardReadResult = suspendCoroutine { cont ->
        device.readCard(
            onSuccess = { track2 ->
                cont.resume(CardReadResult.Success(track2))
            },
            onError = { error ->
                cont.resume(CardReadResult.Error(error))
            },
            onTimeOut = {
                cont.resume(CardReadResult.TimeOut)
            }, context = context
        )
    }

    private val logger = LoggerFactory.getLogger(DeviceEventsReceiver::class.java)

    override suspend fun getPinBlock(context: Context,
        pan: String,
        onError: (String) -> Unit,
        onInput: (Int) -> Unit,
        onConfirm: (String) -> Unit,
        onCancel: () -> Unit,
        onTimeOut: () -> Unit
    ) {
        withContext(ioDispatcher) {
            device.getPinBlock(context = context,
                pan,
                onError = onError,
                onInput = onInput,
                onConfirm = onConfirm,
                onCancel = onCancel,
                onTimeOut = onTimeOut
            )
        }
    }

    override fun getSerial(): String {
        return device.getSerial()
    }

    override fun writeDataKey(key: String?) {
        device.writeDataKey(ISOUtil.hex2byte(key))
    }

    override fun writePinKey(key: String?) {
        device.writePinKey(ISOUtil.hex2byte(key))
    }

    override fun writeMacKey(key: String?) {
        device.writeMacKey(ISOUtil.hex2byte(key))
    }

    override fun writeMasterKey(key: String?) {
        device.writeMasterKey(ISOUtil.hex2byte(key))
    }

    override fun decrypt(data: ByteArray): ByteArray {
        return device.decrypt(data)!!
    }

    override fun encrypt(data: ByteArray): ByteArray {
        return device.encrypt(data)!!
    }

    override suspend fun setDateTime(toFormattedDate: String) {
        device.setDateTime(toFormattedDate)
    }

    override suspend fun print(
        bitmap: Bitmap,
        context: Context,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        withContext(ioDispatcher) {
            device.
            print(bitmap, context, onSuccess = onSuccess, onFailed = { onFailed(it) })
        }
    }

    override suspend fun scan(
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onTimeout: () -> Unit,
        onCancel: () -> Unit
    ) {
        withContext(ioDispatcher) {
            device.scan(
                context = context,
                onSuccess = { onSuccess(it) },
                onError = { onError(it) },
                onTimeout = onTimeout,
                onCancel = onCancel
            )
        }
    }

    override suspend fun getPrinterError(onFailed: (String) -> Unit) {
        withContext(ioDispatcher) {
           onFailed(device.getPrinterError ())
        }
    }

    override suspend fun getBatteryStatus(): Boolean {
        return device.getBatteryStatus()
    }

    override suspend fun isInjectMaster(): Boolean {
        return withContext(ioDispatcher) {
            device.isInjectMaster()
        }
    }

    override fun requestDecryptData(data: ByteArray?): ByteArray? {
        return device.requestDecryptData(data)
    }

    override suspend fun batteryIsEnough(): Boolean {
        return device.getBatteryStatus()

    }

    override suspend fun enableHome() {
        device.enableHome()
    }

    override suspend fun disableHome() {
        device.disableHome()
    }
    override suspend fun getKcv(): KCV {
        return device.getKCv()
    }
}