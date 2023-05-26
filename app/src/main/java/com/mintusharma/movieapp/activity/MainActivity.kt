package com.mintusharma.movieapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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


        binding.btnSearch.setOnClickListener(View.OnClickListener {
            movieViewModel.getMovies("b9bd48a6", binding.searchEditText.text.toString())
            movieViewModel._movies.observe(this, Observer {
                updateMoviesList(it.toMutableList())
            })
        })

    }

    private fun updateMoviesList(movies: MutableList<Search>) {
        // Clear the previous movies list or handle pagination updates if needed
        var adapter = binding.movieRecyclerView.adapter as? MovieAdapter
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
                        movieViewModel.loadMoreMovies("b9bd48a6", binding.searchEditText.text.toString())
                    }
                }
            })
        } else {
            // Pagination update, append new movies to the existing list
            adapter?.addMovies(movies)
        }
    }
}