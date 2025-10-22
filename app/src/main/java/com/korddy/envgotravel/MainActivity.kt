package com.korddy.envgotravel

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.korddy.envgotravel.navigations.navHost.AppNavHost
import com.korddy.envgotravel.services.api.ApiService
import com.korddy.envgotravel.services.api.RetrofitClient
import com.korddy.envgotravel.services.session.SessionCache
import com.korddy.envgotravel.ui.theme.EnvgotravelTheme

// ======================================
// 🚀 Application (inicializa o cache)
// ======================================
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SessionCache.init(this)
    }
}

// ======================================
// 🧭 MainActivity
// ======================================
class MainActivity : ComponentActivity() {

    private val api: ApiService = RetrofitClient.api

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permissions = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.FOREGROUND_SERVICE,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_CONNECT
    )

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            Log.d("Permission", "${it.key} = ${it.value}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializa Firebase (modo de teste permitido)
        runCatching {
            Firebase.auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
            FirebaseApp.initializeApp(this)
            Log.d("Firebase", "Firebase initialized successfully!")
        }.onFailure {
            Log.e("Firebase", "Error configuring Firebase", it)
        }

        requestPermissionsIfNeeded()

        // UI principal
        setContent {
            Envgotravel(api = api)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermissionsIfNeeded() {
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest)
        }
    }
}

// ======================================
// 🎨 Composable principal
// ======================================
@Composable
fun Envgotravel(api: ApiService) {
    EnvgotravelTheme {
        val isDarkMode = isSystemInDarkTheme()
        val view = LocalView.current
        val context = LocalContext.current

        SideEffect {
            val window = (context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !isDarkMode
                isAppearanceLightNavigationBars = !isDarkMode
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            color = if (isDarkMode) Color.Black else Color.White
        ) {
            val navController = rememberNavController()
            AppNavHost(
                navController = navController,
                api = api
            )
        }
    }
}