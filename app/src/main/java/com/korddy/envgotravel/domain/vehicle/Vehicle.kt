package com.korddy.envgotravel.domain.vehicle

import com.google.gson.annotations.SerializedName
import com.korddy.envgotravel.domain.user.User
import java.util.UUID

data class Vehicle(
    val id: UUID,
    val driver: User,
    val brand: String,
    val model: String,
    @SerializedName("plate_number") val plateNumber: String,
    val color: String
)

