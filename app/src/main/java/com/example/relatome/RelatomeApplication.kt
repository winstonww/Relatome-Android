package com.example.relatome

import android.app.Application
import timber.log.Timber

class RelatomeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}