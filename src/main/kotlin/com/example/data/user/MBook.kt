package com.example.data.user

@kotlinx.serialization.Serializable
data class MBook(
    val title: String? = null,
    val authors: String? = null,
    val isReading: Boolean? = null,
    val startReading: Long? = null,
    val rate: Double? = null,
    val currentPage: Int = 1,
    val pages: Int? = null,
    val notes: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val finishedReading: Long? = null,
    val googleBookApiId: String? = null
)
