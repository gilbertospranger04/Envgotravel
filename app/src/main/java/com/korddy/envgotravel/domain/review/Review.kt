package com.korddy.envgotravel.domain.review

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Review(
    val id: Int,
    val infoId: Int,
    val reviewerId: Int,
    val rating: Int,
    val comment: String,
    val createdAt: LocalDateTime? = null
)
