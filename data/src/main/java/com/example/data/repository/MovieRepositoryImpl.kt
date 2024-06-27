package com.example.data.repository

import com.example.data.network.ApiService
import com.example.data.network.model.toMovie
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository


class MovieRepositoryImpl (private val apiService: ApiService) : MovieRepository {
    override suspend fun getNowPlayingMovies(): List<Movie> {
        return  apiService.getMovieNowPlaying().results.map { it.toMovie() }
    }


    override suspend fun getMovieDetail(query: String): Movie {
        return apiService.getMovieDetail(query).toMovie()
    }
}