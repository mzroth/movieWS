package com.mzroth.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Configuration(
    val images: Image
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Image(
    val base_url: String
)