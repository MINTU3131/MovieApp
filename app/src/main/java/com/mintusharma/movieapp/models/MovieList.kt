package com.mintusharma.movieapp.models

import com.google.gson.annotations.SerializedName

data class MovieList(@SerializedName("Search") var Search       : ArrayList<Search> = arrayListOf(),
                     @SerializedName("totalResults") var totalResults : String?           = null,
                     @SerializedName("Response") var Response     : String?           = null)
