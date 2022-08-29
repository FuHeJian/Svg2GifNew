package com.fhj.test2

import android.app.Application

class MyApplication: Application() {
    val application = this
    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler(GlobalCrashCatch.apply {
            _init(application)
        })
    }
}