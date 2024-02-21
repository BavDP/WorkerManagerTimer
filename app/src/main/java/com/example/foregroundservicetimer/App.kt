package com.example.foregroundservicetimer

import android.app.Application
import android.app.NotificationManager
import androidx.work.Configuration

class App: Application(), androidx.work.Configuration.Provider {
    override val workManagerConfiguration: Configuration
        get() {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            return Configuration.Builder().setWorkerFactory(TimerWorkerFactory(notificationManager)).build()
        }
    }