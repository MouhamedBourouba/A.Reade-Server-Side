package com.example

import com.example.data.requests.SingInAuthRequest
import com.example.data.requests.SingUpAuthRequest
import com.example.data.respond.UserRequestRespond
import com.example.data.user.User
import com.example.data.user.UserDataSource
import com.example.security.hashing.HashingService
import com.example.security.token.TokenClaim
import com.example.security.token.TokenConfig
import com.example.security.token.TokenServes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.singUp(
    hashingService: HashingService, userDataSource: UserDataSource
) {
    post("singUp") {
        val request = call.receiveOrNull<SingUpAuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, "no info")
            return@post
        }

        if (request.username.isBlank() || request.password.isBlank() || request.email.isBlank()) {
            call.respond(HttpStatusCode.Conflict, "please Enter your Information")
            return@post
        }

        if (!request.email.contains("@") || !request.email.contains(".com")) {
            call.respond(HttpStatusCode.Conflict, "email is badly formatted")
            return@post
        }

        if (request.password.length < 8) {
            call.respond(HttpStatusCode.Conflict, "Password is too short")
            return@post
        }

        if (request.username.length > 25) {
            call.respond(HttpStatusCode.Conflict, "User name is too big")
            return@post
        }

        val userByEmail = userDataSource.getUserByEmail(request.email)

        val userByName = userDataSource.getUserByUserName(request.username)

        if (userByEmail != null) {
            call.respond(HttpStatusCode.Conflict, "email used By another account")
            return@post
        }

        if (userByName != null) {
            call.respond(HttpStatusCode.Conflict, "userName used By another account")
            return@post
        }

        val hashedPassword = hashingService.generateSaltedHash(request.password, 12)

        val user = User(
            userName = request.username, email = request.email, password = hashedPassword
        )

        val userCreated = userDataSource.insertUser(user)

        if (!userCreated) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        } else {
            call.respond(HttpStatusCode.OK)
            return@post
        }
    }
}

fun Route.singIn(
    hashingService: HashingService, tokenServes: TokenServes, userDataSource: UserDataSource, tokenConfig: TokenConfig
) {
    post("singIn") {
        val request = call.receiveOrNull<SingInAuthRequest>() ?: kotlin.run {
            HttpStatusCode.BadRequest
            return@post
        }

        if (request.usernameOrEmail.isBlank() || request.password.isBlank()) {
            call.respond(HttpStatusCode.Conflict, "please Enter your Information")
            return@post
        }

        if (request.usernameOrEmail.contains('@')) {

            val user = userDataSource.getUserByEmail(request.usernameOrEmail)

            if (user == null) {
                call.respond(HttpStatusCode.Conflict, "Incorrect email or Password")
                return@post
            }

            val isValidPassword = hashingService.verify(request.password, user.password)

            if (isValidPassword) {
                val token = tokenServes.generate(
                    config = tokenConfig, TokenClaim(
                        name = "username", value = user.userName
                    ), TokenClaim(
                        name = "email", value = user.email
                    )
                )
                call.respond(
                    HttpStatusCode.OK, message = token
                )
                return@post
            } else {
                call.respond(HttpStatusCode.Conflict, "Wrong password")
                return@post
            }

        } else {
            val user = userDataSource.getUserByUserName(request.usernameOrEmail)
            if (user == null) {
                call.respond(HttpStatusCode.Conflict, "Incorrect email or Password")
                return@post
            }

            val isValidPassword = hashingService.verify(request.password, user.password)

            if (isValidPassword) {
                val token = tokenServes.generate(
                    config = tokenConfig, TokenClaim(
                        name = "username", value = user.userName
                    ), TokenClaim(
                        name = "email", value = user.email
                    )
                )
                call.respond(
                    HttpStatusCode.OK, message = token
                )
                return@post
            } else {
                call.respond(HttpStatusCode.Conflict, "Wrong password")
                return@post
            }

        }
    }
}


fun Route.authenticate() {
    authenticate {
        get("Authenticate") {
            call.respond(
                HttpStatusCode.OK,
            )
            return@get
        }
    }
}

fun Route.getUserName() {
    authenticate {
        get("getUserName") {
            val principal = call.principal<JWTPrincipal>()
            val userName = principal?.getClaim("username", String::class)
            call.respond(HttpStatusCode.OK, userName.toString())
            return@get
        }
    }
}

fun Route.getUserEmail() {
    authenticate {
        get("getUserEmail") {
            val principal = call.principal<JWTPrincipal>()
            val userEmail = principal?.getClaim("email", String::class)
            call.respond(HttpStatusCode.OK, userEmail.toString())
            return@get
        }
    }
}

fun Route.getUser() {
    authenticate {
        get("getUser") {
            val principal = call.principal<JWTPrincipal>()
            val userEmail = principal?.getClaim("email", String::class)
            val userName = principal?.getClaim("username", String::class)
            call.respond(HttpStatusCode.OK, UserRequestRespond(userEmail, userName))
            return@get
        }
    }
}