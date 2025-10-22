package com.korddy.envgotravel.navigations.bottom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.korddy.envgotravel.ui.theme.Gray

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem("search", "Procurar", Icons.Default.Search),
        BottomNavItem("marketplace", "Marketplace", Icons.Default.Storefront),
        BottomNavItem("store", "Loja", Icons.Default.Store),
        BottomNavItem("favorite", "Favoritos", Icons.Default.Favorite),
        BottomNavItem("cart", "Carrinho", Icons.Default.ShoppingCart)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selected) Gray else MaterialTheme.colorScheme.onBackground
                    )
                },
                selected = selected,
                onClick = {
                    if (!selected) navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = null,
                alwaysShowLabel = false
            )
        }
    }
}