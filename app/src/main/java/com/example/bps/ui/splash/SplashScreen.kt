package com.example.meteosense.ui.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bps.R
import com.example.bps.theme.Blue600 // Sesuaikan dengan warna tema Anda
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // 1. State untuk animasi skala (0f -> 1f)
    val scale = remember {
        Animatable(0f)
    }

    // 2. Jalankan animasi dan navigasi saat layar dibuat
    LaunchedEffect(key1 = true) {
        // Animasi membesar dengan efek memantul (Overshoot)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800, // Durasi animasi 0.8 detik
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )

        // Tahan sebentar (Total loading 2 detik)
        delay(2000L)

        // Pindah ke Beranda
        navController.navigate("beranda") {
            // Hapus Splash dari tumpukan navigasi agar tombol Back tidak kembali ke Splash
            popUpTo("splash") { inclusive = true }
        }
    }

    // 3. Tampilan UI Splash
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue600), // Background biru sesuai tema
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Logo dengan modifier scale yang dianimasikan
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher), // Logo aplikasi Anda
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp)
                    .scale(scale.value) // Terapkan animasi skala di sini
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nama Aplikasi (Opsional)
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.scale(scale.value)
            )
        }
    }
}