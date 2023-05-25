package com.mintusharma.movieapp.repositries

import com.mintusharma.movieapp.ApiServices
import com.mintusharma.movieapp.models.Search
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(private val apiServices: ApiServices) {

    suspend fun getMovieList(
        apiKey: String,
        searchTerm: String,
        page: Int,
    ): List<Search> {
        return withContext(Dispatchers.IO) {
            val response = apiServices.getMovies(apiKey, searchTerm, page)
            if (response.isSuccessful) {
                return@withContext response.body()?.Search ?: emptyList()
            } else {
                throw Exception("Failed to fetch movies")
            }
        }
    }

}