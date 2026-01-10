package com.example.planeja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.planeja.ui.auth.AuthViewModel
import com.example.planeja.ui.auth.AuthViewModelFactory
import com.example.planeja.ui.navigation.PlanejaApp
import com.example.planeja.ui.theme.PlanejaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val app = application as PlanejaApp
        val authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(app.container.authRepository)
        )[AuthViewModel::class.java]

        setContent {
            PlanejaTheme {
                PlanejaApp(authViewModel = authViewModel)
            }
        }
    }
}
