package com.example.movieapplication.di

import com.example.movieapplication.presentation.viewModel.AuthViewModel
import com.example.movieapplication.presentation.viewModel.DetailMovieViewModel
import com.example.movieapplication.presentation.viewModel.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val uiModule
        get() = module {
            viewModel { AuthViewModel(get()) }
            viewModel { MovieViewModel(get()) }
            viewModel { DetailMovieViewModel(get()) }
        }
}