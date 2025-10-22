package com.korddy.envgotravel.domain.ride

import com.google.gson.annotations.SerializedName
import com.korddy.envgotravel.domain.user.User
import java.time.LocalDateTime

data class Ride(
    val id: Int,
    val passenger: User,
    val driver: User?,
    @SerializedName("pickup_location") val pickupLocation: String,
    @SerializedName("pickup_lat") val pickupLat: Double?,
    @SerializedName("pickup_lng") val pickupLng: Double?,
    @SerializedName("dropoff_location") val dropoffLocation: String,
    @SerializedName("dropoff_lat") val dropoffLat: Double?,
    @SerializedName("dropoff_lng") val dropoffLng: Double?,
    @SerializedName("distance_km") val distanceKm: Double?,
    @SerializedName("duration_min") val durationMin: Double?,
    val status: RideStatus,
    @SerializedName("cancel_reason") val cancelReason: String?,
    @SerializedName("requested_at") val requestedAt: LocalDateTime?,
    @SerializedName("completed_at") val completedAt: LocalDateTime?
)
