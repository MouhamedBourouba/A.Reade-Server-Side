package com.example.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

class JwtTokenService: TokenServes {
    override fun generate(config: TokenConfig, vararg claims: TokenClaim): String {
        val generatedToken = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
        claims.forEach {
            generatedToken.withClaim(it.name, it.value)
        }

        return generatedToken.sign(Algorithm.HMAC256(config.secretKey))
    }
}