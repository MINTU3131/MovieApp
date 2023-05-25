package com.mintusharma.movieapp

import com.mintusharma.movieapp.models.MovieList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("/")
    suspend fun getMovies(
        @Query("apikey") apiKey: String,
        @Query("s") searchTerm: String,
        @Query("page") page: Int
    ): Response<MovieList>

}