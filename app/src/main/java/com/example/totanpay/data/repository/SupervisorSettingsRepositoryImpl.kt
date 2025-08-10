package com.example.totanpay.data.repository

import android.util.Log
import com.example.totanpay.data.repository.device.IDevice
import com.example.totanpay.data.util.getCurrentMinuteTime
import com.example.totanpay.data.util.toEnglishNumber
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SupervisorSettingsRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO, private val device: IDevice
) : SupervisorSettingsRepository {

    override suspend fun supervisorPasswordIsValid(pass: String): Boolean {
        return withContext(ioDispatcher) {
            val serial = device.getSerial()
            val currentMinute = getCurrentMinuteTime()

            Log.d("TAG", "supervisorPasswordIsValid: dd$pass")
            Log.d("TAG", "supervisorPasswordIsValid: dd${serial.takeLast(2)}${currentMinute}")

            pass.toEnglishNumber() == "${serial.takeLast(2)}$currentMinute".toEnglishNumber()
        }
    }
}