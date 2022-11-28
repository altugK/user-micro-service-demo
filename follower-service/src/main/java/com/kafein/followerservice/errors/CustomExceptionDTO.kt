package com.kafein.followerservice.errors

import java.util.Date

data class CustomExceptionDTO @JvmOverloads constructor(
    val message: String,
    val status: Int,
    val error: String,
    val path: String,
    val timestamp: Date
)