package com.mintusharma.movieapp.repositries

import com.mintusharma.movieapp.ApiServices
import com.mintusharma.movieapp.models.Search
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(private val apiServices: ApiServices) {

    suspend fun getMovieList(apiKey: String, searchTerm: String, type: String, page: Int, ): ArrayList<Search> {
        return withContext(Dispatchers.IO) {
            val response = apiServices.getMovies(apiKey, searchTerm, type ,page)
            (if (response.Response == "True") {
                response.Search
            } else {
                null
            })!!
        }
    }

}