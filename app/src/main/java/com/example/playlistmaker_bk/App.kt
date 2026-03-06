package com.example.playlistmaker_bk

import android.app.Application
import com.example.playlistmaker_bk.di.dataModule
import com.example.playlistmaker_bk.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, viewModelModule)
        }
    }
}