package com.example.totanpay.data.repository.datasource.transaction.connection

import com.example.totanpay.data.repository.datasource.model.Log
import org.jpos.core.Configuration
import org.jpos.core.ConfigurationException
import org.jpos.iso.BaseChannel
import org.jpos.iso.ISOChannel
import org.jpos.iso.ISOException
import org.jpos.iso.ISOMsg
import org.jpos.iso.ISOPackager
import org.jpos.iso.ISOUtil
import org.jpos.util.LogEvent
import org.jpos.util.Logger
import java.io.EOFException
import java.io.IOException
import java.io.InterruptedIOException
import java.net.ServerSocket
import java.net.SocketException


/*
 * jPOS Project [http://jpos.org]
 * Copyright (C) 2000-2014 Alejandro P. Revilla
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Talks with TCP based NACs
 * Sends [LEN][TPDU][ISOMSG]
 * (len=2 bytes network byte order)
 *
 * @author Alejandro P. Revilla
 * @version $Revision$ $Date$
 * @see ISOMsg
 *
 * @see ISOException
 *
 * @see ISOChannel
 */
open class NacChannel2 : BaseChannel {
    /**
     * Public constructor
     */
    var tpduSwap: Boolean = true

    constructor() : super()

    /**
     * Construct client ISOChannel
     * @param host  server TCP Address
     * @param port  server port number
     * @param p     an ISOPackager
     * @param TPDU  an optional raw header (i.e. TPDU)
     * @see ISOPackager
     */
    constructor(host: String?, port: Int, p: ISOPackager?, TPDU: ByteArray?) : super(
        host,
        port,
        p
    ) {
        this.header = TPDU
    }

    /**
     * Construct server ISOChannel
     * @param p     an ISOPackager
     * @param TPDU  an optional raw header (i.e. TPDU)
     * @exception IOException on error
     * @see ISOPackager
     */
    constructor(p: ISOPackager?, TPDU: ByteArray?) : super(p) {
        this.header = TPDU
    }

    /**
     * constructs server ISOChannel associated with a Server Socket
     * @param p     an ISOPackager
     * @param TPDU  an optional raw header (i.e. TPDU)
     * @param serverSocket where to accept a connection
     * @exception IOException on error
     * @see ISOPackager
     */
    constructor(p: ISOPackager?, TPDU: ByteArray?, serverSocket: ServerSocket?) : super(
        p,
        serverSocket
    ) {
        this.header = TPDU
    }

    @Throws(IOException::class)
    override fun sendMessageLength(len: Int) {
        serverOut.write(len shr 8)
        serverOut.write(len)
    }

    @Throws(IOException::class, ISOException::class)
    override fun getMessageLength(): Int {
        val b = ByteArray(2)
        serverIn.readFully(b, 0, 2)
        return (((b[0].toInt()) and 0xFF) shl 8) or ((b[1].toInt()) and 0xFF)
    }

    @Throws(IOException::class)
    override fun sendMessageHeader(m: ISOMsg, len: Int) {
        var h = m.getHeader()
        if (h != null) {
            if (tpduSwap && h.size == 5) {
                // swap src/dest address
                val tmp = ByteArray(2)
                System.arraycopy(h, 1, tmp, 0, 2)
                System.arraycopy(h, 3, h, 1, 2)
                System.arraycopy(tmp, 0, h, 3, 2)
            }
        } else h = header
        if (h != null) serverOut.write(h)
    }

    /**
     * New QSP compatible signature (see QSP's ConfigChannel)
     * @param header String as seen by QSP
     */
    override fun setHeader(header: String) {
        super.setHeader(ISOUtil.str2bcd(header, false))
    }

    @Throws(ConfigurationException::class)
    override fun setConfiguration(cfg: Configuration) {
        super.setConfiguration(cfg)
        tpduSwap = cfg.getBoolean("tpdu-swap", true)
    }


}