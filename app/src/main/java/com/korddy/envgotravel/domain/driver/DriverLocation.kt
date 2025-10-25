package com.korddy.envgotravel.domain.driver

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class DriverLocation(
    val id: UUID,
    val driverId: UUID,
    val intCoord: Double,   // correspondendo ao int (float8) da imagem, renomeei para intCoord para clareza
    val reqCoord: Double,   // correspondendo ao req (float8)
    val updatedAt: LocalDateTime? = null
)
