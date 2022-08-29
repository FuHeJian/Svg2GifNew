package com.fhj.test2

import android.app.Application
import android.widget.Toast

object GlobalCrashCatch:Thread.UncaughtExceptionHandler{
    lateinit var mApplication: Application
    fun _init(application: Application){
        mApplication = application
    }
    override fun uncaughtException(t: Thread, e: Throwable) {
        try {
            Toast.makeText(mApplication.applicationContext, "程序出错:"+e.message, Toast.LENGTH_SHORT).show()
        }catch (e:Exception){

        }
    }
}