package com.example.data.user

interface UserDataSource {
    suspend fun getUserByUserName(userName: String): User?
    suspend fun insertUser(user: User): Boolean
    suspend fun getUserByEmail(email: String): User?
}