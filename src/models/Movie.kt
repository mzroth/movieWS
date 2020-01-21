package com.mzroth.models

//TODO: Look into making these private and how that works with Jackson This applies to other data classes as well.
data class Movie(
    val movie_id: Int,
    val title: String,
    val popularity_summary: String,
    val poster_image_url: String? = null
)