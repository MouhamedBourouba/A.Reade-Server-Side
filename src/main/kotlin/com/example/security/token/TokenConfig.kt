package com.example.security.token

data class TokenConfig(
    val issuer: String,
    val expiresIn: Long,
    val audience: String,
    val secretKey: String,
)
