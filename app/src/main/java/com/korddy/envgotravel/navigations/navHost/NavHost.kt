package com.korddy.envgotravel.navigations.navHost

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.korddy.envgotravel.services.api.ApiService

// Screens principais
import com.korddy.envgotravel.ui.screens.init.Init
import com.korddy.envgotravel.ui.screens.signin.Signin
import com.korddy.envgotravel.ui.screens.signinpw.SigninPw
import com.korddy.envgotravel.ui.screens.signup.Signup
import com.korddy.envgotravel.ui.screens.home.Home
import com.korddy.envgotravel.ui.screens.profile.Profile
import com.korddy.envgotravel.ui.screens.editProfile.EditProfile

// 2FA
import com.korddy.envgotravel.ui.screens.auth2Factory.Setup2factory
import com.korddy.envgotravel.ui.screens.auth2Factoryverify.Verify2factory

// ViewModels
import com.korddy.envgotravel.ui.screens.signin.SigninViewModel
import com.korddy.envgotravel.ui.screens.confirmation.Confirmation
import com.korddy.envgotravel.ui.screens.confirmation.ConfirmationViewModel
import com.korddy.envgotravel.ui.screens.profile.ProfileViewModel
import com.korddy.envgotravel.ui.screens.editProfile.EditProfileViewModel
import com.korddy.envgotravel.ui.screens.auth2Factory.Setup2factoryViewModel
import com.korddy.envgotravel.ui.screens.auth2Factoryverify.Verify2factoryViewModel

// Bottom e Drawer
import com.korddy.envgotravel.navigations.bottom.*
import com.korddy.envgotravel.ui.screens.paymentmethods.PaymentMethods
import com.korddy.envgotravel.ui.screens.discounts.Discounts
import com.korddy.envgotravel.ui.screens.history.History
import com.korddy.envgotravel.ui.screens.addresses.Addresses
import com.korddy.envgotravel.ui.screens.support.Support
import com.korddy.envgotravel.ui.screens.security.Security
import com.korddy.envgotravel.ui.screens.driverearn.DriverEarn

// Extras (Bottom)
import com.korddy.envgotravel.ui.screens.search.Search
import com.korddy.envgotravel.ui.screens.marketplace.Marketplace
import com.korddy.envgotravel.ui.screens.store.Store
import com.korddy.envgotravel.ui.screens.favorite.Favorite
import com.korddy.envgotravel.ui.screens.cart.Cart

@Composable
fun AppNavHost(
    navController: NavHostController,
    api: ApiService
) {
    // ViewModels criados com remember para persistirem entre recomposições
    val signinViewModel = remember { SigninViewModel(api) }
    val confirmationViewModel = remember { ConfirmationViewModel(api) }
    val profileViewModel = remember { ProfileViewModel() }
    val editProfileViewModel = remember { EditProfileViewModel() }
    val setup2FAViewModel = remember { Setup2factoryViewModel(api) }
    val verify2FAViewModel = remember { Verify2factoryViewModel(api) }

    NavHost(
        navController = navController,
        startDestination = "init"
    ) {
        composable("init") { Init(navController) }

        composable("signin") { Signin(navController, signinViewModel) }

        composable(
            route = "confirmation/{formattedPhone}",
            arguments = listOf(navArgument("formattedPhone") { type = NavType.StringType })
        ) {
            val phone = it.arguments?.getString("formattedPhone") ?: ""
            Confirmation(navController, phone, confirmationViewModel)
        }

        composable(
            route = "signinpw/{formattedPhone}",
            arguments = listOf(navArgument("formattedPhone") { type = NavType.StringType })
        ) {
            val phone = it.arguments?.getString("formattedPhone") ?: ""
            SigninPw(navController, phone)
        }

        composable(
            route = "signup/{formattedPhone}",
            arguments = listOf(navArgument("formattedPhone") { type = NavType.StringType })
        ) {
            val phone = it.arguments?.getString("formattedPhone") ?: ""
            Signup(navController, phone)
        }

        composable("home") { Home(navController, profileViewModel) }

        // PERFIL — sem callbacks extras
        composable("profile") {
            Profile(navController = navController, viewModel = profileViewModel)
        }

        // EDITAR PERFIL — sem parâmetros desnecessários
        composable("editprofile") {
            EditProfile(navController = navController, viewModel = editProfileViewModel)
        }

        // 2FA
        composable("setup2fa") { Setup2factory(navController, setup2FAViewModel) }
        composable("verify2fa") { Verify2factory(navController, verify2FAViewModel) }

        // Bottom navigation
        composable("search") { Search() }
        composable("marketplace") { Marketplace() }
        composable("store") { Store() }
        composable("favorite") { Favorite() }
        composable("cart") { Cart() }

        // Drawer navigation
        composable("paymentmethods") { PaymentMethods() }
        composable("discounts") { Discounts() }
        composable("history") { History() }
        composable("addresses") { Addresses() }
        composable("support") { Support() }
        composable("security") { Security() }
        composable("driverearn") { DriverEarn() }

        // Rotas placeholder (temporárias)
        composable("businessaccount") { TextScreen("Conta empresarial") }
        composable("settings") { TextScreen("Definições") }
        composable("information") { TextScreen("Informações") }
        composable("map") { TextScreen("Mapa") }
    }
}

// Fallback para telas não implementadas
@Composable
private fun TextScreen(text: String) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}