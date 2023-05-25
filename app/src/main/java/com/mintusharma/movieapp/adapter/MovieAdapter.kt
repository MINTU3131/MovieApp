package com.mintusharma.movieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mintusharma.movieapp.R
import com.mintusharma.movieapp.models.Search

class MovieAdapter(private val movies: List<Search>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun addMovies(newMovies: List<Search>) {
        val insertPosition = movies.size
        movies.addAll(newMovies)
        notifyItemRangeInserted(insertPosition, newMovies.size)
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
//        private val yearTextView: TextView = itemView.findViewById(R.id.yearTextView)

        fun bind(movie: Search) {
//            titleTextView.text = movie.title
//            yearTextView.text = movie.year
        }
    }
}