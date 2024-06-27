package com.example.data.network.model

import com.example.domain.model.Movie
import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    val belongs_to_collection: BelongsToCollection,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

fun MovieDetailResponse.toMovie(): Movie {
    return Movie(
        posterPath = posterPath,
        id = id,
        overview = overview,
        title = title
    )
}