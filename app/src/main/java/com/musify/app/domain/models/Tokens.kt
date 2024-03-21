package com.musify.app.domain.models


data class Token(
    val access: String,
    val refresh: String,
)