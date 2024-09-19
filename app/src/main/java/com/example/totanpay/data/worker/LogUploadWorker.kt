package com.example.totanpay.data.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class LogUploadWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val logFile = File(applicationContext.filesDir, "logs/app.log") // Point to the correct log file path
        if (logFile.exists()) {
            val logs = logFile.readText()
            if (uploadLogs(logs)) {
                logFile.delete() // Delete the file after uploading
                return Result.success()
            }
        }
        return Result.retry()
    }

    private fun uploadLogs(logs: String): Boolean {
        return try {
            val url = URL("https://your-server.com/upload-logs")  // Replace with your server's URL
            (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                doOutput = true
                outputStream.write(logs.toByteArray())
            }.responseCode == HttpURLConnection.HTTP_OK
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
