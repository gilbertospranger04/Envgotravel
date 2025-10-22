package com.korddy.envgotravel.ui.screens.init

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.korddy.envgotravel.R
import com.korddy.envgotravel.ui.theme.EnvgotravelTheme
import kotlinx.coroutines.delay

@Composable
fun Init(navController: NavController) {
    val context = LocalContext.current
    val darkTheme = isSystemInDarkTheme()
    val colors = MaterialTheme.colorScheme

    // Lista de ícones sociais (.png transparentes)
    val socialIcons = listOf(
        R.drawable.ic_facebook,
        R.drawable.ic_instagram,
        R.drawable.ic_whatsapp,
        R.drawable.ic_x,
        R.drawable.ic_pinterest,
        R.drawable.ic_youtube,
        R.drawable.ic_linkedin,
        R.drawable.ic_tiktok,
        R.drawable.ic_discord
    )

    // URLs correspondentes
    val socialLinks = listOf(
        "https://facebook.com/envgotravel",
        "https://instagram.com/envgotravel",
        "https://wa.me/244900000000", // WhatsApp (substitui pelo número)
        "https://x.com/envgotravel",
        "https://pinterest.com/envgotravel",
        "https://youtube.com/@envgotravel",
        "https://linkedin.com/company/envgotravel",
        "https://tiktok.com/@envgotravel",
        "https://discord.gg/envgotravel"
    )

    // Animação de pulsação
    val infiniteTransition = rememberInfiniteTransition()
    val scaleAnim by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    var clickedIndex by remember { mutableStateOf(-1) }

    EnvgotravelTheme(darkTheme = darkTheme) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(10.dp))

                // Grade de ícones (3 colunas x várias linhas) sem rolagem
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    userScrollEnabled = false
                ) {
                    itemsIndexed(socialIcons) { index, icon ->
                        val isClicked = clickedIndex == index
                        IconButton(
                            onClick = {
                                clickedIndex = index
                                val url = socialLinks.getOrNull(index)
                                url?.let {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                                    context.startActivity(intent)
                                }
                            },
                            modifier = Modifier
                                .scale(if (isClicked) 0.9f else scaleAnim)
                                .clip(CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = icon),
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Logo + Slogan
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val logoRes = if (darkTheme) R.drawable.splash_logo else R.drawable.splash_icon
                    Image(
                        painter = painterResource(id = logoRes),
                        contentDescription = "Logo Envgotravel",
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .shadow(12.dp, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Envgotravel\nO seu conforto total",
                        color = colors.onBackground,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Botão principal + copyright
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = { navController.navigate("signin") },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(56.dp)
                            .shadow(10.dp, RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.onBackground,
                            contentColor = colors.background
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Começar",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = "© 2025 Envgotravel",
                        fontSize = 13.sp,
                        color = colors.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}