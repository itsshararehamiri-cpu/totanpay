package com.example.totanpay.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.slf4j.LoggerFactory

class DeviceEventsReceiver : BroadcastReceiver() {

    private val logger = LoggerFactory.getLogger(DeviceEventsReceiver::class.java)

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                logger.info("Device turned on")
            }
            Intent.ACTION_SHUTDOWN -> {
                logger.info("Device shutting down")
            }
        }
    }
}
