package com.example.totanpay.common

import android.content.Context
import android.graphics.Bitmap
import com.example.totanpay.data.repository.DeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface PrintableViewModel {
    val deviceRepository:DeviceRepository

    fun print(
        bitmap: Bitmap,
        context: Context,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
        deviceRepository.print(bitmap, context, onSuccess, onFailed)
        }
    }
}