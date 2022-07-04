package com.example.plugins

import com.example.*
import com.example.data.user.UserDataSource
import com.example.security.hashing.HashingService
import com.example.security.token.TokenConfig
import com.example.security.token.TokenServes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenConfig: TokenConfig,
    tokenServes: TokenServes
) {

    routing {
        singIn(hashingService, tokenServes, userDataSource, tokenConfig)
        singUp(hashingService, userDataSource)
        authenticate()
        getUserEmail()
        getUser(userDataSource)
        updateBook(userDataSource)
        deleteBook(userDataSource)
        addBook(userDataSource)
        getUserName()
    }
}
