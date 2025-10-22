package com.korddy.envgotravel.domain.driver

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class DriverLocation(
    val id: Int,
    val driverId: Int,
    val intCoord: Double,   // correspondendo ao int (float8) da imagem, renomeei para intCoord para clareza
    val reqCoord: Double,   // correspondendo ao req (float8)
    val updatedAt: LocalDateTime? = null
)
