package com.example.albumphotos.data

import com.example.albumphotos.data.album.network.AlbumService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@ComponentScan
class DataModule {

    @Factory
    fun provideLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    @Factory
    fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Single
    fun provideRetrofit(client: OkHttpClient) = Retrofit.Builder()
        .baseUrl(BuildConfig.AlbumBaseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Single
    fun albumAPI(retrofit: Retrofit): AlbumService {
        return retrofit.create(AlbumService::class.java)
    }
}
