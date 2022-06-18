package com.example

import com.example.data.user.MongoUserDataSource
import com.example.plugins.configureMonitoring
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.security.hashing.SHA256HashingService
import com.example.security.token.JwtTokenService
import com.example.security.token.TokenConfig
import io.ktor.server.application.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    val mongoDbPassword = System.getenv("PW")
    val mongoDbName = "AReader"

    val db = KMongo.createClient(
        ("mongodb+srv://AReader:$mongoDbPassword@cluster0.iss4xf3.mongodb.net/$mongoDbName?retryWrites=true&w=majority")
    ).coroutine
        .getDatabase(mongoDbName)

    val hashingService = SHA256HashingService()
    val userDataSource = MongoUserDataSource(db)
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        expiresIn = System.currentTimeMillis() + 365L * 60 * 60 * 24,
        audience = environment.config.property("jwt.audience").getString(),
        secretKey = System.getenv("secret")
    )
    val tokenServes = JwtTokenService()


    configureRouting(
        hashingService,
        userDataSource,
        tokenConfig,
        tokenServes
    )
    configureSerialization()
    configureMonitoring()
    configureSecurity(tokenConfig)
}
