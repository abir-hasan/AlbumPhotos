package com.example.albumphotos

import com.example.albumphotos.data.DataModule
import com.example.albumphotos.domain.DomainModule
import com.example.albumphotos.presentation.PresentationModule
import org.koin.core.annotation.KoinApplication

@KoinApplication(
    modules = [
        AppModule::class,
        DataModule::class,
        DomainModule::class,
        PresentationModule::class,
    ],
)
object AlbumPhotosKoinApp
