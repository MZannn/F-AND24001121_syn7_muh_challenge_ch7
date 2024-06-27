package com.example.data.network

import com.example.data.network.model.MovieDetailResponse
import com.example.data.network.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("movie/now_playing")
    suspend fun getMovieNowPlaying(): MovieResponse

    @GET("movie/{movieId}")
    suspend fun getMovieDetail(
        @Path("movieId") movieId: String
    ): MovieDetailResponse
}