package com.korddy.envgotravel.ui.screens.map

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.korddy.envgotravel.maps.map.MapEnvgotravel
import kotlinx.coroutines.tasks.await

suspend fun getCurrentLocation(context: Context): LatLng? {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    return try {
        val location = fusedLocationClient.lastLocation.await()
        location?.let { LatLng(it.latitude, it.longitude) }
    } catch (_: SecurityException) {
        null
    }
}

@SuppressLint("MissingPermission")
@Composable
fun MapScreen() {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var destination by remember { mutableStateOf<LatLng?>(null) }
    var polylinePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    LaunchedEffect(Unit) {
        getCurrentLocation(context)?.let {
            userLocation = it
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        MapEnvgotravel(
            modifier = Modifier.fillMaxSize(),
            userLocation = userLocation,
            destination = destination,
            polylinePoints = polylinePoints,
            cameraPositionState = cameraPositionState
        )
    }
}