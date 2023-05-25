package com.mintusharma.movieapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mintusharma.movieapp.ApiServices
import com.mintusharma.movieapp.R
import com.mintusharma.movieapp.RetrofitClient
import com.mintusharma.movieapp.adapter.MovieAdapter
import com.mintusharma.movieapp.databinding.ActivityMainBinding
import com.mintusharma.movieapp.models.Search
import com.mintusharma.movieapp.repositries.MovieRepository
import com.mintusharma.movieapp.viewmodels.MovieViewModel
import com.mintusharma.movieapp.viewmodels.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var movieViewModel: MovieViewModel
    private var isLoading = false
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieRepository = MovieRepository(RetrofitClient.getClient().create(ApiServices::class.java))
        movieViewModel = ViewModelProvider(this, ViewModelFactory(movieRepository))
            .get(MovieViewModel::class.java)

        movieViewModel.getMovies("YOUR_API_KEY", "SEARCH_TERM")

        movieViewModel.movies.observe(this, Observer {

            val isEndOfList = it.size < MovieViewModel.PAGE_SIZE

            if (isEndOfList && !isLoading) {
                // Reached the end of the list, trigger loading more movies
                isLoading = true
                movieViewModel.loadMoreMovies("YOUR_API_KEY", "SEARCH_TERM")
            }

        })
    }

    private fun updateMoviesList(movies: List<Search>) {
        // Clear the previous movies list or handle pagination updates if needed
        val adapter = binding.movieRecyclerView.adapter as? MovieAdapter
        val isPaginationEnabled = adapter != null

        if (!isPaginationEnabled) {
            // Initial loading or new search, set up the adapter
            val layoutManager = LinearLayoutManager(this)
            binding.movieRecyclerView.layoutManager = layoutManager
            adapter = MovieAdapter(movies)
            binding.movieRecyclerView.adapter = adapter
            binding.movieRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        // Reached the end of the list, trigger loading more movies
                        movieViewModel.loadMoreMovies("YOUR_API_KEY", "SEARCH_TERM")
                    }
                }
            })
        } else {
            // Pagination update, append new movies to the existing list
            adapter.addMovies(movies)
        }
    }
}