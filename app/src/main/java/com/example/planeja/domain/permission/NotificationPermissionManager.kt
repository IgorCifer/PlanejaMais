package com.example.planeja.domain.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class NotificationPermissionManager(private val context: Context) {

    companion object {
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001

        private const val NOTIFICATION_PERMISSION = Manifest.permission.POST_NOTIFICATIONS
    }
    fun isPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }

        return ContextCompat.checkSelfPermission(
            context,
            NOTIFICATION_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }
    fun shouldShowRationale(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return false
        }

        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            NOTIFICATION_PERMISSION
        )
    }
    fun requestPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(NOTIFICATION_PERMISSION),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }
    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}
