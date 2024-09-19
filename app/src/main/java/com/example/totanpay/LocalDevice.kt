package com.example.totanpay

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.totanpay.data.repository.device.IDevice

val LocalDeviceManager = staticCompositionLocalOf<IDevice> {
    error("No Device Manager is implemented")
}
