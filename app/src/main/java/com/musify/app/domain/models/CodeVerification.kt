package com.musify.app.domain.models


data class CodeVerification (
    val phone: String,
    val otp: String,
)