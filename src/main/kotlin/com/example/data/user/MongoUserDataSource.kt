package com.example.data.user

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.replaceOne
import org.litote.kmongo.eq

class MongoUserDataSource(
    db: CoroutineDatabase
) : UserDataSource {

    private val users = db.getCollection<User>()

    override suspend fun getUserByUserName(userName: String): User? {
        return users.findOne(User::userName eq userName)
    }

    override suspend fun insertUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.findOne(User::email eq email)

    }

    override suspend fun addBook(userName: String, book: MBook): Boolean {
        val user = getUserByUserName(userName) ?: return false
        val editor = user.userBooks.add(book)
        return if (editor) {
            users.replaceOne(User::userName eq userName, user).wasAcknowledged()
        } else {
            false
        }
    }

    override suspend fun deleteBook(userName: String, bookId: String): Boolean {
        val user = getUserByUserName(userName) ?: return false
        val editor = user.userBooks.removeIf { it.googleBookApiId == bookId }
        return if (editor) {
            users.replaceOne(User::userName eq userName, user).wasAcknowledged()
        } else {
            false
        }
    }

    override suspend fun updateBook(userName: String, bookId: String, book: MBook): Boolean {
        val user = getUserByUserName(userName) ?: return false
        user.userBooks.replaceAll {
            if (it.googleBookApiId == bookId) {
                book
            } else {
                it
            }
        }
        return users.replaceOne(User::userName eq userName, user).wasAcknowledged()
    }
}