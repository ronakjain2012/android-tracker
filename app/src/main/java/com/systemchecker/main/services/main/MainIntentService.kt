package com.systemchecker.main.services.main

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.*
import androidx.core.app.JobIntentService

class MainIntentService : JobIntentService() {

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    private val TAG = "MainIntentService"

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onHandleWork(intent: Intent) {
        // Service has started and new work has been received
        // Do work in here
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "Service onStartCommand " + startId)
        mHandler = Handler()
        mRunnable = Runnable { tasks() }
        mHandler.postDelayed(mRunnable, 5000)
        return Service.START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "Service is destroyed...")
    }


    private fun tasks() {
        Log.i(TAG, "Running...")
        mHandler.postDelayed(mRunnable, 5000)
    }
}