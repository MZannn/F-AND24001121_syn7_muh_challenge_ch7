import com.example.data.network.ApiService
import com.example.data.network.model.BelongsToCollection
import com.example.data.network.model.Dates
import com.example.data.network.model.Genre
import com.example.data.network.model.MovieDetailResponse
import com.example.data.network.model.MovieResponse
import com.example.data.network.model.ProductionCompany
import com.example.data.network.model.ProductionCountry
import com.example.data.network.model.Result
import com.example.data.network.model.SpokenLanguage
import com.example.data.repository.MovieRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class MovieRepositoryImplTest {

    private lateinit var apiService: ApiService
    private lateinit var movieRepositoryImpl: MovieRepositoryImpl

    @Before
    fun setup() {
        apiService = mock(ApiService::class.java)
        movieRepositoryImpl = MovieRepositoryImpl(apiService)
    }

    @Test
    suspend fun getNowPlayingMovies() {
        val mockResponse = MovieResponse(
            dates = Dates("2024-06-30", "2024-06-01"), page = 1, results = listOf(
                Result(
                    adult = false,
                    backdropPath = "/backdrop.jpg",
                    genreIds = listOf(18, 28),
                    id = 123,
                    originalLanguage = "en",
                    originalTitle = "Original Title",
                    overview = "Overview of the movie",
                    popularity = 123.45,
                    posterPath = "/poster.jpg",
                    releaseDate = "2024-06-30",
                    title = "Title of the Movie",
                    video = false,
                    voteAverage = 8.5,
                    voteCount = 100
                )
            ), totalPages = 1, totalResults = 1
        )

        runBlocking {
            `when`(apiService.getMovieNowPlaying()).thenReturn(mockResponse)
        }

        val result = movieRepositoryImpl.getNowPlayingMovies()

        assertEquals(1, result.size)
        assertEquals("Title of the Movie", result[0].title)
        assertEquals("/poster.jpg", result[0].posterPath)
        assertEquals(123, result[0].id)
        assertEquals("Overview of the movie", result[0].overview)
    }

    @Test
    suspend fun getMovieDetail() {
        val mockResponse = MovieDetailResponse(
            adult = false,
            backdropPath = "/backdrop.jpg",
            belongs_to_collection = BelongsToCollection(
                backdrop_path = "/backdrop.jpg",
                id = 123,
                name = "Collection Name",
                poster_path = "/poster.jpg"
            ),
            budget = 1000000,
            genres = listOf(
                Genre(
                    id = 123, name = "Genre"
                ), Genre(
                    id = 456, name = "Genre 2"
                )
            ),
            homepage = "https://example.com",
            id = 123,
            imdb_id = "tt1234567",
            original_language = "en",
            original_title = "Original Title",
            overview = "Overview of the movie",
            popularity = 123.45,
            posterPath = "/poster.jpg",
            production_companies = listOf(
                ProductionCompany(
                    id = 123,
                    logo_path = "/logo.jpg",
                    name = "Production Company",
                    origin_country = "US"
                ), ProductionCompany(
                    id = 456,
                    logo_path = "/logo2.jpg",
                    name = "Production Company 2",
                    origin_country = "US"
                )
            ),
            production_countries = listOf(
                ProductionCountry(
                    iso_3166_1 = "US",
                    name = "United States of America",
                ), ProductionCountry(
                    iso_3166_1 = "GB",
                    name = "United Kingdom",
                )
            ),
            release_date = "2024-06-30",
            revenue = 2000000,
            runtime = 120,
            spoken_languages = listOf(
                SpokenLanguage(
                    english_name = "English", iso_639_1 = "en", name = "English"
                ), SpokenLanguage(
                    english_name = "Spanish", iso_639_1 = "es", name = "Spanish"
                )
            ),
            status = "Released",
            tagline = "Tagline of the movie",
            title = "Title of the Movie",
            video = false,
            vote_average = 8.5,
            vote_count = 100,
            origin_country = listOf("US", "GB", "CA")
        )

        runBlocking {
            `when`(apiService.getMovieDetail("123")).thenReturn(mockResponse)
        }

        val result = movieRepositoryImpl.getMovieDetail("123")

        assertEquals(mockResponse.title, result.title)
        assertEquals(mockResponse.posterPath, result.posterPath)
        assertEquals(mockResponse.id, result.id)
        assertEquals(mockResponse.overview, result.overview)
    }
}
