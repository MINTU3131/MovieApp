package com.mintusharma.movieapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mintusharma.movieapp.ApiServices
import com.mintusharma.movieapp.OnClickListener
import com.mintusharma.movieapp.RetrofitClient
import com.mintusharma.movieapp.adapter.MovieAdapter
import com.mintusharma.movieapp.databinding.ActivityMainBinding
import com.mintusharma.movieapp.models.Search
import com.mintusharma.movieapp.repositries.MovieRepository
import com.mintusharma.movieapp.viewmodels.MovieViewModel
import com.mintusharma.movieapp.viewmodels.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieRepository = MovieRepository(RetrofitClient.getClient().create(ApiServices::class.java))
        movieViewModel = ViewModelProvider(this, ViewModelFactory(movieRepository))
            .get(MovieViewModel::class.java)

        movieViewModel._movies.observe(this, Observer {
            binding.progressbar.visibility = View.GONE
            updateMoviesList(it)
        })

        movieViewModel.errorLiveData.observe(this, Observer { errorMessage ->
            binding.progressbar.visibility = View.GONE
            if (!errorMessage.isNullOrEmpty()) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    errorMessage,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        binding.btnSearch.setOnClickListener(View.OnClickListener {
            binding.progressbar.visibility = View.VISIBLE
            movieViewModel.getMovies("b9bd48a6", binding.searchEditText.text.toString(),"Movie",1)
        })

    }

    private fun updateMoviesList(movies: List<Search>) {

        var adapter = binding.movieRecyclerView.adapter as? MovieAdapter
        val isPaginationEnabled = adapter != null

        if (!isPaginationEnabled) {
            val layoutManager = GridLayoutManager(this,2)
            binding.movieRecyclerView.layoutManager = layoutManager
            adapter = MovieAdapter(movies as ArrayList<Search>,object : OnClickListener{
                override fun onItemClickListener(position: Int) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        movies.get(position).Title.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            })
            binding.movieRecyclerView.adapter = adapter
            binding.movieRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        movieViewModel.loadMoreMovies("b9bd48a6", binding.searchEditText.text.toString(),"Movie")
                    }
                }
            })
        }
    else {
            adapter?.addMovies(movies as ArrayList<Search>)
        }
    }
}