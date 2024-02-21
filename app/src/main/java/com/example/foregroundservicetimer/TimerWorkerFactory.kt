package com.example.foregroundservicetimer

import android.app.NotificationManager
import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import java.lang.RuntimeException

class TimerWorkerFactory(private val notificationManager: NotificationManager): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        if (workerClassName == TimerWorker::class.java.name) {
            return TimerWorker(appContext, workerParameters, notificationManager)
        } else {
            throw RuntimeException("unsupported worker class!")
        }
    }
}