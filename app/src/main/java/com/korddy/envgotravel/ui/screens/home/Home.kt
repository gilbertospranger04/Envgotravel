package com.korddy.envgotravel.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.korddy.envgotravel.navigations.drawer.Drawer
import com.korddy.envgotravel.navigations.bottom.bottomNavigation.BottomNavigation
import com.korddy.envgotravel.ui.screens.profile.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Drawer(
                navController = navController,
                backgroundColor = MaterialTheme.colorScheme.primary // verde
            ) {
                coroutineScope.launch { drawerState.close() }
            }
        }
    ) {
        Scaffold(
            bottomBar = { BottomNavigation(navController = navController) }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Bem-vindo ao Envgotravel",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Explore destinos, lojas e promoções.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                FloatingActionButton(
                    onClick = { coroutineScope.launch { drawerState.open() } },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart),
                    containerColor = MaterialTheme.colorScheme.primary, // verde
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Abrir menu")
                }
            }
        }
    }
}