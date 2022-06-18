package com.example.data.user

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val userName: String,
    val email: String,
    val password: String,
    @BsonId val id: ObjectId = ObjectId()
)
