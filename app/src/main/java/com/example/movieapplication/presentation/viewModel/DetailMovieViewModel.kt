package com.example.movieapplication.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Resource
import com.example.domain.model.Movie
import com.example.domain.usecase.MovieUseCase

import kotlinx.coroutines.launch

class DetailMovieViewModel(private val movieUseCase: MovieUseCase):ViewModel() {
    private var _movieDetailResponse = MutableLiveData<Movie?>()
    val movieDetailResponse: MutableLiveData<Movie?> get() = _movieDetailResponse

    fun getMovieDetail(id: String) {
        viewModelScope.launch {
            movieUseCase(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _movieDetailResponse.value = result.data
                    }
                    is Resource.Error -> {
                        Log.e("MovieViewModel", "getMovieDetail: ${result.message}")
                    }
                    is Resource.Loading -> {
                        Log.d("MovieViewModel", "getMovieDetail: Loading")
                    }
                    else -> {}
                }
            }
        }
    }

}