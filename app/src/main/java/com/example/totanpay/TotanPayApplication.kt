package com.example.totanpay

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Singleton


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
    }
}