package com.mintusharma.movieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mintusharma.movieapp.OnClickListener
import com.mintusharma.movieapp.R
import com.mintusharma.movieapp.databinding.RowItemBinding
import com.mintusharma.movieapp.models.Search

class MovieAdapter(private var movies: ArrayList<Search>,private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = RowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)

        holder.itemView.setOnClickListener(View.OnClickListener {
            onClickListener.onItemClickListener(position)
        })
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun clearMovies() {
        movies.clear()
        notifyDataSetChanged()
    }

    fun addMovies(newMovies: ArrayList<Search>) {
        val insertPosition = movies.size
        movies.addAll(newMovies)
        notifyItemRangeInserted(insertPosition, newMovies.size)
    }

    inner class MovieViewHolder(private val binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Search) {
            binding.title.text = movie.Title
            Glide.with(binding.root.context)
                .load(movie.Poster)
                .into(binding.image)
        }
    }
}