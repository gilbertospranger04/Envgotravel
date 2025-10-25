package com.korddy.envgotravel.domain.review

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class Review(
    val id: UUID,
    val infoId: UUID,
    val reviewerId: UUID,
    val rating: Int,
    val comment: String,
    val createdAt: LocalDateTime? = null
)

