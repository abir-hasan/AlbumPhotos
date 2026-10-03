package com.example.albumphotos

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.plugin.module.dsl.startKoin

class AlbumPhotosApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    private fun setupKoin() {
        startKoin<AlbumPhotosKoinApp> {
            androidContext(this@AlbumPhotosApplication)
        }
    }
}
