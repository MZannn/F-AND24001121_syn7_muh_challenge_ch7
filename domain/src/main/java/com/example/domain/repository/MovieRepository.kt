package com.example.domain.repository

import com.example.domain.model.Movie

interface MovieRepository {
    suspend fun getNowPlayingMovies(): List<Movie>

    suspend fun getMovieDetail(query: String): Movie
}