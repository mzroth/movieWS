package com.mzroth

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class TmdbResponse(
    val results: List<TmdbMovie>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TmdbMovie(
    val popularity: Double,
    val vote_count: Int,
    val poster_path: String?,
    val id: Int,
    val title: String
)
