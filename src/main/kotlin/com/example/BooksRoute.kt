package com.example

import com.example.data.user.MBook
import com.example.data.user.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.addBook(
    userDataSource: UserDataSource
) {
    put("userBooks/addBook/{userName}") {
        val userName = call.parameters["userName"].toString()
        val book = call.receiveOrNull<MBook>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@put
        }
        val isSuccess = userDataSource.addBook(userName,book)
        if (isSuccess) {
            call.respond(HttpStatusCode.OK)
            return@put
        } else {
            call.respond(HttpStatusCode.Conflict)
            return@put
        }
    }
}

fun Route.deleteBook(
    userDataSource: UserDataSource
) {
    delete("userBooks/delete/{userName}/{bookId}") {
        val userName = call.parameters["userName"].toString()
        val bookId = call.parameters["bookId"].toString()
        val isSuccess = userDataSource.deleteBook(userName, bookId)
        if (isSuccess) {
            call.respond(HttpStatusCode.OK)
            return@delete
        } else {
            call.respond(HttpStatusCode.Conflict)
            return@delete
        }
    }
}

fun Route.updateBook(
    userDataSource: UserDataSource
) {
    put("userBooks/update/{userName}/{bookId}") {
        val userName = call.parameters["userName"].toString()
        val bookId = call.parameters["bookId"].toString()
        val book = call.receiveOrNull<MBook>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@put
        }
        val isSuccess = userDataSource.updateBook(userName, bookId, book)

        if (isSuccess) {
            call.respond(HttpStatusCode.OK)
            return@put
        } else {
            call.respond(HttpStatusCode.Conflict)
            return@put
        }
    }
}
















































