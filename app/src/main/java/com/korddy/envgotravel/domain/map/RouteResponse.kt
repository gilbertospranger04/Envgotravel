// domain/map/RouteResponse.kt
package com.korddy.envgotravel.domain.map

data class RouteResponse(
    val polyline: List<Point>
)

data class Point(
    val lat: Double,
    val lng: Double
)