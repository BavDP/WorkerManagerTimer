package com.example.foregroundservicetimer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


const val WM_NOTIFICATION_ID = 100
const val WM_CHANNEL_ID = "wm_notification_channel"

class TimerWorker(private val context: Context,
                  private val workerParams: WorkerParameters,
    private val notificationManager: NotificationManager): Worker(context, workerParams) {
    private var builder: NotificationCompat.Builder


    init {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        builder = NotificationCompat.Builder(applicationContext, WM_CHANNEL_ID)
            .setSmallIcon(R.drawable.timer)
            .setContentTitle("Worker Manager")
            .setContentText("Worker Timer")
            .setSound(null)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(WM_CHANNEL_ID,
                "wmNotificationChannel",
                NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
        }
    }
    override fun doWork(): Result {
        Log.d("", "doWork")
        createTimer(6, builder).start()
        return Result.success()
    }

    private fun  createTimer(startValue: Int, builder: NotificationCompat.Builder): Deferred<Unit> {
        return CoroutineScope(Dispatchers.Default).async {
            var i = startValue
            while (!isStopped) {
                builder.setContentTitle(i.toString())
                withContext(Dispatchers.Main) {
                    notificationManager.notify(WM_NOTIFICATION_ID, builder.build())
                }
                delay(1000)
                i--
                if (i == -1) {
                    notificationManager.cancelAll()
                    break
                }
            }
        }
    }

    override fun onStopped() {
        Log.d("", "stopped")
    }

    override fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(WM_NOTIFICATION_ID, builder.build())
    }
}