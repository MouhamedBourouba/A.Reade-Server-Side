package com.example.security.token

interface TokenServes {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String
}