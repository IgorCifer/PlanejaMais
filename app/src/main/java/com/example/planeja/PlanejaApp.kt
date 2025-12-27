package com.example.planeja

import android.app.Application
import com.example.planeja.data.AppContainer

class PlanejaApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
