package com.example.planeja.domain.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.planeja.ui.notification.PlanejaNotificationManager

class DailyReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val notificationManager = PlanejaNotificationManager(applicationContext)
            notificationManager.showDailyReminder()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "daily_reminder_work"
    }
}
