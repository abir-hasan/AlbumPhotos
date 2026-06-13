package com.example.albumphotos

import android.app.Application
import com.example.albumphotos.data.DataModule
import com.example.albumphotos.domain.DomainModule
import com.example.albumphotos.presentation.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class AlbumPhotosApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@AlbumPhotosApplication)
            modules(
                AppModule().module,
                DataModule().module,
                DomainModule().module,
                PresentationModule().module,
            )
        }
    }
}
