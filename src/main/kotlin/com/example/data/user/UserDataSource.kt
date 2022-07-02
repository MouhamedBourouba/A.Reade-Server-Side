package com.example.data.user

import java.awt.print.Book

interface UserDataSource {
    suspend fun getUserByUserName(userName: String): User?
    suspend fun insertUser(user: User): Boolean
    suspend fun getUserByEmail(email: String): User?
    suspend fun addBook(userName: String, bookId: String, book: MBook): Boolean
    suspend fun deleteBook(userName: String, bookId: String): Boolean
    suspend fun updateBook(userName: String, bookId: String, book: MBook): Boolean
}