package com.sidv.boundservicebinder.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class TimerService : Service() {

    private val binder = TimerBinder()
    private var elapsedTime = 0 // Số giây trôi qua
    private var job: Job? = null
    private val coroutineScope =
        CoroutineScope(Dispatchers.Main) // Chạy trên Main thread để cập nhật UI
    private val connectedScreens = mutableListOf<String>()

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("TimerService", "Service Created")
        startTimer() // 🚀 Bắt đầu đếm thời gian ngay khi service được tạo
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d(
            "TimerService1", "onBind() - Một màn hình đã kết nối. Số lượng: ${connectedScreens.size}"
        )
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(
            "TimerService1",
            "onUnbind() - Không còn màn hình nào kết nối. Số lượng: ${connectedScreens.size}"
        )
        return super.onUnbind(intent)
//        return true ~ Chat bao phai return true moi chay vao onRebind

    }

    override fun onRebind(intent: Intent?) {
        Log.d(
            "TimerService1",
            "onRebind() - Một màn hình đã kết nối lại. Số lượng: ${connectedScreens.size}"
        )
        super.onRebind(intent)
    }

    override fun onDestroy() {
        Log.d("TimerService", "Service Destroyed")
        super.onDestroy()
    }

    private fun startTimer() {
        job?.cancel() // Hủy job cũ nếu có
        job = coroutineScope.launch {
            while (isActive) {
                delay(1000)
                elapsedTime++
            }
        }
    }

    fun increaseConnectionCount(screenName: String) {
        if (!connectedScreens.contains(screenName)) {
            connectedScreens.add(screenName)
        }
        Log.d(
            "TimerService",
            "increaseConnectionCount() - $screenName kết nối. Hiện tại: $connectedScreens"
        )
    }

    fun decreaseConnectionCount(screenName: String) {
        connectedScreens.remove(screenName)
        Log.d(
            "TimerService",
            "decreaseConnectionCount() - $screenName ngắt kết nối. Hiện tại: $connectedScreens"
        )
    }

    fun getElapsedTime(): Int {
        Log.d("TimerService", "getElapsedTime() called - elapsedTime: $elapsedTime")
        return elapsedTime
    }

}
