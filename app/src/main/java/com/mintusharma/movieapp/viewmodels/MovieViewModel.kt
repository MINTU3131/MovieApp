package com.mintusharma.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintusharma.movieapp.models.Search
import com.mintusharma.movieapp.repositries.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    val _movies = MutableLiveData<ArrayList<Search>>()
    val errorLiveData = MutableLiveData<String>()

    private var currentPage = 1

    fun getMovies(apiKey: String, searchTerm: String, type: String, page: Int) {
        viewModelScope.launch {
            try {
                val movies = movieRepository.getMovieList(apiKey, searchTerm,type,page)
                if (movies != null) {
                    _movies.value = movies
                } else {
                    errorLiveData.value = "No movies found"
                }
            } catch (e: Exception) {
                errorLiveData.value = e.message
            }
        }
    }

    fun loadMoreMovies(apiKey: String, searchTerm: String, type: String) {
        viewModelScope.launch {
            try {
                val nextPage = currentPage + 1
                val result = movieRepository.getMovieList(apiKey, searchTerm,type, nextPage)
                currentPage = nextPage
                _movies.value = _movies.value?.plus(result) as ArrayList<Search>?
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

//    companion object {
//        const val PAGE_SIZE = 10
//    }
}