package com.example.totanpay

import android.app.Application
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.totanpay.data.worker.LogUploadWorker
import com.example.totanpay.util.PermissionUtil
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit


@HiltAndroidApp
class TotanPayApplication : Application() {
    companion object {
        private var mInstance: TotanPayApplication? = null

        @Synchronized
        fun getInstance(): TotanPayApplication? {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= 23) {
            val requestPermissionCount: List<String> = PermissionUtil.getRequestPermissionList(
                this, PermissionUtil.Permissions
            )
            if ((requestPermissionCount != null && requestPermissionCount.isNotEmpty())
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager())
            ) {
                val dialogIntent = Intent(
                    applicationContext,
                    MainActivity::class.java
                )
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                applicationContext.startActivity(dialogIntent)
            }
        }
        setupLogback()
       // scheduleLogUploadWorker()
    }

    private fun scheduleLogUploadWorker() {
        val logUploadRequest = PeriodicWorkRequestBuilder<LogUploadWorker>(1, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(logUploadRequest)
    }

    private fun setupLogback() {
        try {
//            val context1 = LoggerFactory.getILoggerFactory() as LoggerContext
//            val inputStream: InputStream = assets.open("logback.xml")
//            context1.reset() // Reset previous configuration
//            ch.qos.logback.classic.joran.JoranConfigurator().apply {
//                context = context1
//                doConfigure(inputStream) // Configure Logback with the XML file
//            }
        } catch (e: Exception) {
            Log.e("TAG", "setupLogback cause: ${e.cause}")
            Log.e("TAG", "setupLogback message: ${e.message}")

            e.printStackTrace()
        }
    }
}