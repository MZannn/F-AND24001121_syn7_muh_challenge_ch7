package com.example.movieapplication.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Resource
import com.example.domain.model.Movie
import com.example.domain.usecase.MovieUseCase


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {
    private val _movieResponse = MutableLiveData<List<Movie>>()
    val movieResponse: LiveData<List<Movie>> = _movieResponse

    private fun getNowPlayingMovies() {
        viewModelScope.launch {
            movieUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _movieResponse.value = result.data ?: emptyList()
                    }
                    is Resource.Error -> {
                        Log.e("MovieViewModel", "getNowPlayingMovies: ${result.message}")
                    }
                    is Resource.Loading -> {
                        Log.d("MovieViewModel", "getNowPlayingMovies: Loading")
                    }
                    else -> {}
                }
            }
        }
    }

    init {
        getNowPlayingMovies()
    }
}