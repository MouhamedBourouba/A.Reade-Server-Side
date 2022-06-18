package com.example.security.hashing

interface HashingService {
    fun generateSaltedHash(value: String, saltLength: Int = 24): String
    fun verify(value: String, hash: String): Boolean
}