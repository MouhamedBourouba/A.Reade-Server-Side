package com.example.data.respond

@kotlinx.serialization.Serializable
data class UserRequestRespond(
    val userEmail: String?,
    val userName: String?
)