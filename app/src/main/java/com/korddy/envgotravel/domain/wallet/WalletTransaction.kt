package com.korddy.envgotravel.domain.wallet

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class WalletTransaction(
    val id: UUID,
    val userId: Int,
    val amount: Double,
    val transactionBy: String,
    val description: String,
    val createdAt: LocalDateTime? = null
)

