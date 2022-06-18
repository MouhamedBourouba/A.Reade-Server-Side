package com.example.security.hashing

import org.apache.commons.codec.digest.DigestUtils

class SHA256HashingService: HashingService {
    override fun generateSaltedHash(value: String, saltLength: Int): String {
        return DigestUtils.sha256Hex(value)
    }

    override fun verify(value: String, hash: String): Boolean {
        return DigestUtils.sha256Hex(value) == hash
    }
}