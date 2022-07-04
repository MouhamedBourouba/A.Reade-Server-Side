package com.example.data.user

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@kotlinx.serialization.Serializable
data class User(
    val userName: String,
    val email: String,
    val userBooks: ArrayList<MBook> = ArrayList(),
    val password: String,
)
