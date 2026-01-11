package com.example.planeja

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.planeja.domain.permission.NotificationPermissionManager
import com.example.planeja.ui.auth.AuthViewModel
import com.example.planeja.ui.auth.AuthViewModelFactory
import com.example.planeja.ui.navigation.PlanejaApp
import com.example.planeja.ui.theme.PlanejaTheme

class MainActivity : ComponentActivity() {

    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _ ->
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestNotificationPermissionIfNeeded()

        val app = application as PlanejaApp
        val authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(app.container.authRepository)
        )[AuthViewModel::class.java]

        val permissionManager = NotificationPermissionManager(this)

        setContent {
            PlanejaTheme {
                PlanejaApp(
                    authViewModel = authViewModel,
                    permissionManager = permissionManager,
                    activity = this
                )
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS

            val alreadyGranted = ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED

            if (!alreadyGranted) {
                notificationPermissionLauncher.launch(permission)
            }
        }
    }
}

