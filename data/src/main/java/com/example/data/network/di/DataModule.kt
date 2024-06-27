package com.example.data.network.di

import com.example.data.local.datastore.LocalDataStore
import com.example.data.network.ApiClient
import com.example.data.repository.MovieRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.repository.MovieRepository
import com.example.domain.repository.UserRepository
import com.example.data.local.database.UserDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object DataModule {

    val dataModule
        get() = module {
            single { ApiClient.create(get()) }
            single { LocalDataStore(get()) }
            single {
                UserDatabase.getInstance(androidContext()).userDao
            }
            factory<MovieRepository>{ MovieRepositoryImpl(get()) }
            factory<UserRepository> { UserRepositoryImpl(get(), get())}
//            factory { MovieUseCase(get()) }

        }
}