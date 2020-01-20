package com.mzroth

data class Movie(
    private val movie_id: Int,
    private val title: String,
    private val popularity_summary: String,
    private val poster_image_url: String? = null
)