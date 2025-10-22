package com.korddy.envgotravel.domain.vehicle

import com.google.gson.annotations.SerializedName
import com.korddy.envgotravel.domain.user.User

data class Vehicle(
    val id: Int,
    val driver: User,
    val brand: String,
    val model: String,
    @SerializedName("plate_number") val plateNumber: String,
    val color: String
)
