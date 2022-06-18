package com.example.data.requests

@kotlinx.serialization.Serializable
data class SingUpAuthRequest(
    val username: String,
    val email: String,
    val password: String
)

@kotlinx.serialization.Serializable
data class SingInAuthRequest(
    val usernameOrEmail: String,
    val password: String
)
