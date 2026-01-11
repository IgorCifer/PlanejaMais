package com.example.planeja.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.*
import com.example.planeja.data.local.preferences.NotificationPreferences
import com.example.planeja.domain.worker.DailyReminderWorker
import kotlinx.coroutines.flow.Flow
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import com.example.planeja.ui.notification.PlanejaNotificationManager
class NotificationRepository(private val context: Context) {

    private val preferences = NotificationPreferences(context)

    val notificationsEnabled: Flow<Boolean> = preferences.notificationsEnabled

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        preferences.setNotificationsEnabled(enabled)

        if (enabled) {
            scheduleDailyReminder()
        } else {
            cancelDailyReminder()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleDailyReminder() {
        val currentTime = LocalDateTime.now()
        val notificationTime = LocalTime.of(20,0)

        var scheduledTime = currentTime.with(notificationTime)


        if (scheduledTime <= currentTime) {
            scheduledTime = scheduledTime.plusDays(1)
        }

        val initialDelay = Duration.between(currentTime, scheduledTime).seconds

        val workRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setInitialDelay(initialDelay, TimeUnit.SECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DailyReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun cancelDailyReminder() {
        WorkManager.getInstance(context)
            .cancelUniqueWork(DailyReminderWorker.WORK_NAME)
    }

    fun testNotificationImmediately() {
        val notificationManager = PlanejaNotificationManager(context)
        notificationManager.showDailyReminder()
    }

    fun scheduleTestReminder() {
        val workRequest = OneTimeWorkRequestBuilder<DailyReminderWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "test_reminder_work",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}
