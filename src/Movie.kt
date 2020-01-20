package com.mzroth

data class Movie(
    val movie_id: Int,
    val title: String,
    val popularity_summary: String,
    val poster_image_url: String? = null
)