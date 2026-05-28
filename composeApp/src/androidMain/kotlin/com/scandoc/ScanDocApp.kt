package com.scandoc

import android.app.Application
import com.scandoc.core.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ScanDocApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ScanDocApp)
            modules(appModule)
        }
    }
}
