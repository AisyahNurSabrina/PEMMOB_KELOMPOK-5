package com.pemmob.foundu.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pemmob.foundu.ui.common.FoundUTopBar
import com.pemmob.foundu.ui.common.GradientButton
import com.pemmob.foundu.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit
) {
    var name by remember(com.pemmob.foundu.data.local.UserSession.name) {
        mutableStateOf(com.pemmob.foundu.data.local.UserSession.name)
    }

    var email by remember(com.pemmob.foundu.data.local.UserSession.email) {
        mutableStateOf(com.pemmob.foundu.data.local.UserSession.email)
    }
    
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            FoundUTopBar(title = "Profil")
        },
        containerColor = FoundUBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            // Header / Welcome
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Pengaturan Akun", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = FoundUText)
                    Text("Kelola informasi profil dan keamanan", fontSize = 14.sp, color = FoundUMuted)
                }
                
                IconButton(
                    onClick = onLogoutClick,
                    modifier = Modifier.background(Color(0xFFFEE2E2), RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color(0xFFDC2626))
                }
            }

            // Update Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = FoundUSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Informasi Profil", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = FoundUText)
                    Text("Perbarui nama dan email akun Anda", fontSize = 13.sp, color = FoundUMuted)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Nama", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = FoundUText)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FoundUPrimary, unfocusedBorderColor = Color(0xFFE5E7EB))
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text("Email", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = FoundUText)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FoundUPrimary, unfocusedBorderColor = Color(0xFFE5E7EB))
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    GradientButton(
                        text = "Simpan",
                        onClick = { /* Handle update profile */ },
                        icon = { Icon(Icons.Default.Check, null, Modifier.size(16.dp), tint = Color.White) }
                    )
                }
            }

            // Update Password Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = FoundUSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Ubah Password", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = FoundUText)
                    Text("Gunakan password yang kuat dan unik", fontSize = 13.sp, color = FoundUMuted)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Password Saat Ini", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = FoundUText)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FoundUPrimary, unfocusedBorderColor = Color(0xFFE5E7EB))
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text("Password Baru", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = FoundUText)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FoundUPrimary, unfocusedBorderColor = Color(0xFFE5E7EB))
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text("Konfirmasi Password", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = FoundUText)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FoundUPrimary, unfocusedBorderColor = Color(0xFFE5E7EB))
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    GradientButton(
                        text = "Ubah Password",
                        onClick = { /* Handle update password */ },
                        icon = { Icon(Icons.Default.Lock, null, Modifier.size(16.dp), tint = Color.White) }
                    )
                }
            }

            // Delete Account Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = FoundUSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.width(4.dp).fillMaxHeight().background(Color(0xFFEF4444))) // Red border line
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Hapus Akun", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFFDC2626))
                        Text("Setelah akun dihapus, semua data akan hilang secara permanen.", fontSize = 13.sp, color = FoundUMuted)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { showDeleteDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                        ) {
                            Icon(Icons.Default.Delete, null, Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Hapus Akun", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showDeleteDialog) {
        var deletePassword by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Konfirmasi Hapus Akun", fontWeight = FontWeight.Bold, color = Color(0xFFDC2626)) },
            text = {
                Column {
                    Text("Masukkan password Anda untuk mengkonfirmasi penghapusan akun.", color = FoundUMuted, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Password", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = FoundUText)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = deletePassword,
                        onValueChange = { deletePassword = it },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFEF4444), unfocusedBorderColor = Color(0xFFE5E7EB))
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDeleteDialog = false; onLogoutClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Hapus Akun") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Batal", color = FoundUMuted) }
            },
            containerColor = FoundUSurface
        )
    }
}
