package com.korddy.envgotravel.domain.wallet

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class WalletTransaction(
    val id: Int,
    val userId: Int,
    val amount: Double,
    val transactionBy: String,
    val description: String,
    val createdAt: LocalDateTime? = null
)
