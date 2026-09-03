package com.example.totanpay.data.repository.datasource.transaction.connection

import android.util.Log
import com.example.totanpay.data.repository.datasource.transaction.IsoMessage
import org.jpos.iso.ISOPackager
import org.jpos.iso.ISOUtil
import org.jpos.iso.channel.NACChannel
import org.jpos.iso.packager.ISO87BPackager
import org.jpos.util.Logger
import org.jpos.util.SimpleLogListener

private const val TAG = "Connection"
class Connection : IConnection {
    private lateinit var ip: String
    private var port: Int = -1
    private val packager: ISOPackager = ISO87BPackager()
  //  private val header: ByteArray = byteArrayOf(0x60, 0x01, 0x2D, 0x00, 0x00)
    private lateinit var channel: NacChannel2
    override fun init(ip: String, port: Int,nii: String) {//
        val nii_ = ByteUtil.padleft(nii.trim().toInt().toString(16), 4, '0')
        var header=ByteUtil.hex2byte("60" + nii_ + "0000")
        this.ip = ip
        this.port = port
        channel = NacChannel2(ip, port, packager, header)
    }

    override fun start() {
        val RECONNECT_DELAY = 1000L
        val IN_SPACE_KEY = "TestSpace-send"
        val OUT_SPACE_KEY = "TestSpace-receive"
        try {
            channel.name = "sipa"
            channel.packager = packager
            val logger = Logger()
            logger.addListener(SimpleLogListener())
            logger.name = "testLinkLogger"
            channel.setLogger(logger, "POS")
           channel.timeout=60000
            channel.connect()
        } catch (e: Exception) {
            Log.e(TAG, "start: ${e.cause}")
            Log.e(TAG, "start: ${e.message}")
            Log.e(TAG, "{$ip : $port}", e)
            throw Exception("Start channel failed",e)
        }
    }
    override fun send(msg: IsoMessage) {
        try {
            val rawData = msg.pack()
            Log.i(TAG, "RAW DATA (HEX): ${ISOUtil.hexString(rawData)}")
            channel.send(msg)
        } catch (e: Exception) {
            throw Exception("Start channel failed",e)
        }
    }
    override fun receive(): IsoMessage? {
        val m = IsoMessage()

        val mr = channel.receive()
        m.toIsoMessage(mr)
        return if (mr == null) null else m
    }
    override fun stop() {
        channel.disconnect()
    }
    override fun close() {
        stop()
    }
}