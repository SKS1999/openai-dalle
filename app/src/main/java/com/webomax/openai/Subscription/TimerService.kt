package com.webomax.openai.Subscription

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

class MyService: Service() {

    private var TAG = "MyService"


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    val timer = object : CountDownTimer(86400000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            // Send a broadcast to the fragment every second
            val intent = Intent("timer_tick")
            sendBroadcast(intent)
        }

        override fun onFinish() {
            // Send a broadcast to the fragment when the timer finishes
            val intent = Intent("timer_finished")
            sendBroadcast(intent)
        }

//       fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//            Log.d(TAG, "onStartCommand")
//            timer.start()
//            return START_STICKY
//        }
//
//        fun onDestroy() {
//            Log.d(TAG, "onDestroy")
//            timer.cancel()
//        }

    }
}