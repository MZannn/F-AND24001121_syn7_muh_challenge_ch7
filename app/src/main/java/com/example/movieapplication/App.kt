package com.example.movieapplication

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.data.network.di.DataModule
import com.example.domain.di.DomainModule
import com.example.movieapplication.di.AppModule.uiModule

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Log.d("App", "Starting Koin initialization")
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    DataModule.dataModule, DomainModule.dataModule, uiModule
                )
            )
        }
        Log.d("App", "Koin initialization completed")

    }
    companion object {
        var context : Context? = null
    }

}