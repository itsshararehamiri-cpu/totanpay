package com.example.totanpay.data.repository.datasource.transaction.connection

import android.util.Log
import com.example.totanpay.data.repository.datasource.transaction.IsoMessage
import org.jdom.Element
import org.jpos.iso.ISOChannel
import org.jpos.iso.ISOPackager
import org.jpos.iso.channel.NACChannel
import org.jpos.iso.packager.ISO87BPackager
import org.jpos.q2.iso.ChannelAdaptor
import org.jpos.util.Logger
import org.jpos.util.SimpleLogListener

class Connection(
    private val ip: String,
    private val port: Int,
    private val packager: ISOPackager = ISO87BPackager(),
    private val header: ByteArray = byteArrayOf(0x60, 0x00, 0x00, 0x00, 0x00)
) : IConnection {
    private val TAG = "Connection"
    private var ca: ChannelAdaptor? = null
    private var sendTimeout: Int = 1000
    private var receiveTimeout: Int = 10000
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
        val channel: ISOChannel = NACChannel(ip, port, packager, header)
        try {
            ca = ChannelAdaptorWithoutQ2(channel)
            ca!!.name = "sipa"
            ca!!.persist = persist
            val logger = Logger()
            logger.addListener(SimpleLogListener())
            logger.name = "testLinkLogger"
            ca!!.logger = logger.name
            ca!!.init()
            ca!!.start()
        } catch (e: Exception) {
            Log.e(TAG, "{$ip : $port}", e)
            throw Exception("Start channel failed")
        }
    }

    override fun send(msg: IsoMessage) {
        ca!!.send(msg, sendTimeout.toLong())
    }

    override fun receive(): IsoMessage? {
        val m = IsoMessage()
        val mr = ca!!.receive(receiveTimeout.toLong())
        m.toIsoMessage(mr)
        return if (mr == null) null else m
    }

    override fun stop() {
        ca!!.stop()
        ca!!.destroy()
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