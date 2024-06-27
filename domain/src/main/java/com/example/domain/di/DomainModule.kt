package com.example.domain.di
import com.example.domain.usecase.MovieUseCase
import org.koin.dsl.module
object DomainModule {
    val dataModule
        get() = module {

            factory { MovieUseCase(get()) }
        }
}