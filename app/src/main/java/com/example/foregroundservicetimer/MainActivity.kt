package com.example.foregroundservicetimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.example.foregroundservicetimer.databinding.ActivityMainBinding
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private lateinit var workerId: UUID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.startBtn.setOnClickListener {
            val workRequest = OneTimeWorkRequestBuilder<TimerWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
            this.workerId = workRequest.id
            WorkManager.getInstance(this).enqueue(workRequest)
        }

        _binding.stopBtn.setOnClickListener {
            WorkManager.getInstance(this).cancelWorkById(this.workerId)
        }
    }
}