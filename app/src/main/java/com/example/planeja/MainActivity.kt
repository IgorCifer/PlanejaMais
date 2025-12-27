package com.example.planeja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.planeja.ui.navigation.PlanejaApp
import com.example.planeja.ui.theme.PlanejaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlanejaTheme {
                PlanejaApp()
            }
        }
    }
}

