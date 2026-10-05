package com.pemmob.foundu.ui.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pemmob.foundu.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLoginScreen(
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    // Cosmic background gradient matching the web version
    val backgroundBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0C0E30),
            Color(0xFF1A1040),
            Color(0xFF2D1B69),
            Color(0xFF1A1040),
            Color(0xFF0C0E30)
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        // Floating Orbs Effect
        val infiniteTransition = rememberInfiniteTransition(label = "orbs")

        val orb1Offset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 20f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            ),
            label = "orb1"
        )

        val orb2Offset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = -30f,
            animationSpec = infiniteRepeatable(
                animation = tween(5000, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            ),
            label = "orb2"
        )

        // Orb 1 (Top Right - Purple)
        Box(
            modifier = Modifier
                .offset(x = 100.dp + orb1Offset.dp, y = (-50).dp - orb1Offset.dp)
                .size(250.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF8B5CF6).copy(alpha = 0.15f), Color.Transparent)
                    )
                )
        )

        // Orb 2 (Bottom Left - Blue)
        Box(
            modifier = Modifier
                .offset(x = (-50).dp + orb2Offset.dp, y = 100.dp - orb2Offset.dp)
                .size(200.dp)
                .align(Alignment.BottomStart)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF60A5FA).copy(alpha = 0.12f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo & Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color(0xFFA78BFA),
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "FoundU",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 3.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Platform Lost & Found Kampus",
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 1.sp,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Login Card (Glassmorphic look)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Masuk sebagai Admin",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E1B4B)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Email Field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Email", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = FoundUText)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("admin@adm.unsoed.ac.id", color = Color.Gray) },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FoundUPrimary,
                                unfocusedBorderColor = Color(0xFFE5E7EB)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Password", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = FoundUText)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("Masukkan password", color = Color.Gray) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FoundUPrimary,
                                unfocusedBorderColor = Color(0xFFE5E7EB)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Remember Me
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(checkedColor = FoundUPrimary)
                            )
                            Text("Ingat saya", fontSize = 12.sp, color = FoundUText)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    val context = androidx.compose.ui.platform.LocalContext.current

                    // Login Button (Gradient)
                    Button(
                        onClick = {
                            val success = com.pemmob.foundu.data.local.UserSession.loginAdmin(
                                email,
                                password
                            )
                            if (success) {
                                onLoginClick()
                            } else {
                                // For mockup, show a toast or error
                                android.widget.Toast.makeText(
                                    context,
                                    "Kredensial tidak valid",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(FoundUPrimary, FoundUPrimaryDark)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent // Background handled by modifier
                        ),
                        contentPadding = PaddingValues()
                    ) {
                        Text("Masuk", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Back to student login
                    Text(
                        text = "Kembali ke Login Mahasiswa",
                        fontSize = 13.sp,
                        color = FoundUPrimary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onBackClick() }
                    )
                }
            }
        }
    }
}
