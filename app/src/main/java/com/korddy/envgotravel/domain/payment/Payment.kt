package com.korddy.envgotravel.domain.payment

import com.google.gson.annotations.SerializedName
import com.korddy.envgotravel.domain.ride.Ride
import java.time.LocalDateTime

data class Payment(
    val id: Int,
    val ride: Ride,
    val amount: Double,
    val status: PaymentStatus,
    @SerializedName("payment_method") val paymentMethod: PaymentMethod,
    @SerializedName("created_at") val createdAt: LocalDateTime
)
