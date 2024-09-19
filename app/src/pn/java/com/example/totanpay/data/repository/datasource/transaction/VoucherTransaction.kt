package com.example.totanpay.data.repository.datasource.transaction


import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.VoucherTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FaildTransactionResponse
import org.jpos.iso.ISOUtil
import java.nio.charset.Charset


class VoucherTransaction(
    request: VoucherTransactionRequest, private val macGenerator: IMacGenerator,
    iConnection: IConnection,
    saveReverseData: suspend (msg: IsoMessage) -> Unit,
    saveTransactionLog: suspend (msg: IsoMessage) -> Unit,
    updateTransaction: suspend (
        date: String, time: String, pan: String, cardIssuer: String, responseCode: String, amount: String, rrn: String?,
        serviceDesc: String?, pinVoucher: String?, serialVoucher: String?, mobileNumber: String?, operatorCode: Int?
    ) -> Unit,
    val requestDecryptData: (ByteArray?) -> ByteArray?,
    sendTransactionInQueue: suspend () -> Boolean,
    private val setStatusToSettle: suspend (date: String, time: String) -> Unit) : BaseTransaction(
    request,
    iConnection,
    saveReverseData,
    saveTransactionLog,
    sendTransactionInQueue,
    updateTransaction
) {
    override val isReversible: Boolean
        get() = true
    override val type: Int
        get() = TransactionType.PURCHASE.tag
    override val needReport: Boolean
        get() = true

    override suspend fun buildMessage() {
        with(sendMessage) {
//            mti = "0200"
//            processCode = "180000"
//            pan = (request as VoucherTransactionRequest).pan
//            //amount = (request as VoucherTransactionRequest).amount
//            amount="000000020000"
//            stan = request.stan.toString()
//            setDateTime(request.date, request.time)
//            setPOS(request.POS)
//            setNii(request.nii)
//            set(25,"00")
//            setTrack2(request.track2)
//            setTerminalId(request.terminalId)
//            setMerchantId(request.merchantId)
//            setField48 {
//                setSerial(request.serial)
//                setVersion(request.appVersion)
//                setTerminalLanguage(request.terminalLanguage)
//                setTerminalConnectionType(request.terminalConnectionType)
//                setProductCode("1142")
//            }
//            setCurrency(request.currency)
//            setPinBlock(request.pinBlock)
//            macGenerator.getMac(this)

            mti = "0200"
            processCode = "150000"
            stan = (request as VoucherTransactionRequest).stan.toString()
            pan = request.pan
            amount = request.amount
            setTrack2(request.track2)
            setCurrency(request.currency)
            setPOS(request.POS)
            setPinBlock(request.pinBlock)
            setMerchantId(request.merchantId)
            setDateTime(request.date, request.time)
            setNii(request.nii)
            setTerminalId(request.terminalId!!)
            setField48 {
                setSerial(request.serial)
                setVersion(request.appVersion)
                setProductCode("1142")
                setTerminalLanguage(request.terminalLanguage)
                setTerminalConnectionType("2")
                setVoucherNo(1)
//                setVoucherNo(req.voucherNo)
            }
            macGenerator?.getMac(this)
        }
    }

    override suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse {
        val cardIssuer = receivedIsoMessage.getField48Tag(0x38)?.split("\\")?.get(0) ?: ""
        setStatusToSettle(sendMessage.tranDate, sendMessage.tranTime)
        var serviceDesc = ""
        if (receivedIsoMessage.hasField(47)) {
            if (receivedIsoMessage.getBytes(47) != null) serviceDesc =
                ISOUtil.hexString(
                    receivedIsoMessage.getBytes(
                        47
                    )
                )
        }
        //val groupVoucherData=voucherBufferParse(receivedIsoMessage.getString(60))
   //  val groupVoucherDataEncrypted=   voucherBufferParseEnc(receivedIsoMessage.getString(60))
        val voucherSerial = receivedIsoMessage.getField48Tag(0x40) ?: ""
        val pin = receivedIsoMessage.getField48Tag(0x41) ?: ""
        val data = ISOUtil.hex2byte(pin)
        val decData: ByteArray = requestDecryptData(data) ?: ByteArray(0)
        updateTransaction(
            sendMessage.tranDate,
            sendMessage.tranTime,
            (request as VoucherTransactionRequest).pan,
            cardIssuer,
            receivedIsoMessage.respCode.toString(),
            request.amount,
            receivedIsoMessage.rrn ?: "",
            serviceDesc,
            indentBytes4Decrypt(decData),
            voucherSerial,
            null,
            sendMessage.operatorCode.toInt()
        )
        return BaseTransactionResponse.VoucherTransactionResponse(
            responseCode = 0,
            responseMessage = null,
            reasonCode = null,
            rrn = receivedIsoMessage.rrn ?: "",
            trace = sendMessage.stan,
            date = sendMessage.tranDate,
            time = sendMessage.tranTime,
            issuerName = cardIssuer,
            maskedPan = sendMessage.pan,
            voucherSerial = voucherSerial,
            voucherPin = indentBytes4Decrypt(decData), voucherPINEncrypted = "",
            groupVoucherData = java.util.HashMap(), groupVoucherDataEncrypted = java.util.HashMap(),
            amount = request.amount
        )
    }

    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FaildTransactionResponse {
        if (receivedIsoMessage == null) {
            /*
             val respCode: Int,
    val respMessage: String?,
    val reasonCode: Int? = null,
    var date: String,
    var time: String,
    var trace: String,
    val cardIssuer: String? = null,
    var maskedPan: String? = null,
    var amount : String? = null,
    var rrn : String? = null
             */
            updateTransaction(
                sendMessage.tranDate,
                sendMessage.tranTime,
                (request as VoucherTransactionRequest).pan,
                "",
                "-1",
                request.amount, null, null, null, null, null, null
            )
            return FaildTransactionResponse(
                responseCode = -1,
                responseMessage = "خطا در دریافت اطلاعات",
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan.toInt(),
                cardIssuer = "",
                maskedPan = sendMessage.pan,
                amount = sendMessage.getString(4)
            )
        } else {
            if (receivedIsoMessage.respCode != 80)
                clearTransactionFromQueue(sendMessage.tranDate, sendMessage.tranTime)
            val cardIssuer = receivedIsoMessage.getField48Tag(0x38)?.split("\\")?.get(0) ?: ""
            updateTransaction(
                sendMessage.tranDate,
                sendMessage.tranTime,
                (request as VoucherTransactionRequest).pan,
                cardIssuer,
                receivedIsoMessage.respCode.toString(),
                request.amount,
                receivedIsoMessage.rrn, null, null, null, null, null
            )
            return FaildTransactionResponse(
                responseCode = receivedIsoMessage.respCode,
                responseMessage = "",
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan.toInt(),
                cardIssuer = cardIssuer,
                maskedPan = sendMessage.pan,
                amount = sendMessage.getString(4),
                rrn = receivedIsoMessage.rrn,
            )
        }

    }

    @Throws(Exception::class)
    fun voucherBufferParse(buffer: String): HashMap<String, String> {
        val voucherNo = buffer.substring(0, 2).toInt()
        var bufferStart = 2


        val voucherData = HashMap<String, String>()


        for (i in 0 until voucherNo) {
            val serialLen = buffer.substring(bufferStart, bufferStart + 2).toInt()
            val pinLen = buffer.substring(bufferStart + 2, bufferStart + 4).toInt()

            val serial = buffer.substring(bufferStart + 4, bufferStart + 4 + serialLen)

            val pin: ByteArray = ISOUtil.hex2byte(
                buffer.substring(
                    bufferStart + 4 + serialLen,
                    bufferStart + 4 + serialLen + pinLen
                )
            )
            val decData: ByteArray =
                requestDecryptData(pin)!!
            val finalPin: String = ISOUtil.bcd2str(decData, 0, pinLen, false)
            voucherData[serial] = finalPin



            bufferStart = bufferStart + 4 + serialLen + pinLen
        }

        return voucherData
    }
    @Throws(java.lang.Exception::class)
    private fun voucherBufferParseEnc(buffer: String): HashMap<String, String> {
        val voucherNo = buffer.substring(0, 2).toInt()
        var bufferStart = 2


        val voucherData = HashMap<String, String>()


        for (i in 0 until voucherNo) {
            val serialLen = buffer.substring(bufferStart, bufferStart + 2).toInt()
            val pinLen = buffer.substring(bufferStart + 2, bufferStart + 4).toInt()

            val serial = buffer.substring(bufferStart + 4, bufferStart + 4 + serialLen)

            /*byte[] pin = ByteUtil.hex2byte(buffer.substring(bufferStart+4+serialLen,bufferStart+4+serialLen+pinLen));
			byte[] decData = super.getSipaAcq().getSetting().getSecurityService().decryptData(pin);
			String finalPin = ByteUtil.bcd2str(decData,0,pinLen,false);*/
            voucherData[serial] =
                buffer.substring(bufferStart + 4 + serialLen, bufferStart + 4 + serialLen + pinLen)



            bufferStart = bufferStart + 4 + serialLen + pinLen
        }

        return voucherData
    }
}

fun indentBytes4Decrypt(values: ByteArray?): String {
    return String(
        removeZeroChareFromRight(
            values!!
        ), Charset.forName("Windows-1256")//forName("Windows-1256")
    )//
}

fun removeZeroChareFromRight(allBytes: ByteArray): ByteArray {
    var allBytes = allBytes
    var firstZero = -1
    for (idx in allBytes.indices) {
        if (allBytes[idx].equals(0)) {
            firstZero = idx
            break
        }
    }
    if (firstZero > 0) {
        val newAllBytes = ByteArray(firstZero)
        System.arraycopy(allBytes, 0, newAllBytes, 0, newAllBytes.size)
        allBytes = newAllBytes
    }
    return allBytes
}
