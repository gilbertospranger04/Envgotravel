package com.korddy.envgotravel.maps.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapEnvgotravel(
    modifier: Modifier = Modifier,
    userLocation: LatLng?,
    destination: LatLng?,
    polylinePoints: List<LatLng>,
    cameraPositionState: CameraPositionState
) {
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true)
    ) {
        userLocation?.let {
            Marker(state = MarkerState(it), title = "Aqui")
        }

        destination?.let {
            Marker(state = MarkerState(it), title = "Destino")
            Polyline(points = polylinePoints)
        }
    }
}