package com.example.totanpay.data.repository.datasource.transaction.connection

import android.util.Log
import com.example.totanpay.data.repository.datasource.transaction.IsoMessage
import org.jdom.Element
import org.jpos.iso.Channel
import org.jpos.iso.ISOChannel
import org.jpos.iso.ISOPackager
import org.jpos.iso.channel.NACChannel
import org.jpos.iso.packager.ISO87BPackager
import org.jpos.q2.iso.ChannelAdaptor
import org.jpos.util.Logger
import org.jpos.util.SimpleLogListener

class Connection : IConnection {
    private val TAG = "Connection"

    // private var ca: Channel? = null
    private var sendTimeout: Int = 1000
    private var receiveTimeout: Int = 10000
    private lateinit var ip: String
    private var port: Int = -1
    private val packager: ISOPackager = ISO87BPackager()
    private val header: ByteArray = byteArrayOf(0x60, 0x00, 0x00, 0x00, 0x00)
    private lateinit var channel: ISOChannel
    override fun init(ip: String, port: Int) {
        this.ip = ip
        this.port = port
        channel = NACChannel(ip, port, packager, header)
    }

    override fun start() {
        val RECONNECT_DELAY = 1000L
        val IN_SPACE_KEY = "TestSpace-send"
        val OUT_SPACE_KEY = "TestSpace-receive"
        val persist = Element("channel-adaptor")
        persist.addContent(Element("space").addContent("transient:TestLink"))
        persist.addContent(Element("in").addContent(IN_SPACE_KEY))
        persist.addContent(Element("out").addContent(OUT_SPACE_KEY))
        persist.addContent(Element("reconnect-delay").addContent(RECONNECT_DELAY.toString()))
        persist.addContent(Element("wait-for-workers-on-stop").addContent("yes"))
        try {
//            ca = NACChannel()
//                //ChannelAdaptorWithoutQ2(channel)
            channel!!.name = "sipa"
            channel!!.packager = packager
            val logger = Logger()
            logger.addListener(SimpleLogListener())
            logger.name = "testLinkLogger"
            channel.connect()
//            channel!!.logger = logger.name
//            channel!!.init()
//            channel!!.start()
            // channel.
        } catch (e: Exception) {
            Log.e(TAG, "start: ${e.cause}")
            Log.e(TAG, "start: ${e.message}")
            Log.e(TAG, "{$ip : $port}", e)
            throw Exception("Start channel failedyy")
        }
    }

    override fun send(msg: IsoMessage) {
        try {
            channel.send(msg)
        } catch (e: Exception) {
            Log.d(TAG, "cause: ${e.cause}")
            Log.d(TAG, "message: ${e.message}")
            throw Exception("Start channel failed2")
        }
        //!!.send(msg, sendTimeout.toLong())
    }

    override fun receive(): IsoMessage? {
        val m = IsoMessage()
        val mr = channel!!.receive()
        m.toIsoMessage(mr)
        return if (mr == null) null else m
    }

    override fun stop() {
        channel.disconnect()
//        channel!!.stop()
//        channel!!.destroy()
//        channel.s
    }

    override fun close() {
        stop()
    }

    private class ChannelAdaptorWithoutQ2(private val channel: ISOChannel) : ChannelAdaptor() {
        override fun initChannel(): ISOChannel {
            return channel
        }
    }
}