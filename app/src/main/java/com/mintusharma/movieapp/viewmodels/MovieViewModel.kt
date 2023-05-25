package com.mintusharma.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintusharma.movieapp.models.Search
import com.mintusharma.movieapp.repositries.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<Search>>()
    val movies: LiveData<List<Search>> get() = _movies

    private var currentPage = 1

    fun getMovies(apiKey: String, searchTerm: String) {
        viewModelScope.launch {
            try {
                val result = movieRepository.getMovieList(apiKey, searchTerm, 1)
                currentPage = 1
                _movies.value = result
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun loadMoreMovies(apiKey: String, searchTerm: String) {
        viewModelScope.launch {
            try {
                val nextPage = currentPage + 1
                val result = movieRepository.getMovieList(apiKey, searchTerm, nextPage)
                currentPage = nextPage
                _movies.value = _movies.value.orEmpty() + result
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}