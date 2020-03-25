package com.letusneil.twichinola.data

data class AccessToken(
    val expires_at: String,
    val mobile_restricted: Boolean,
    val sig: String,
    val token: String
)