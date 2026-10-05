package com.pemmob.foundu.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pemmob.foundu.ui.common.UiState
import com.pemmob.foundu.ui.common.FoundUTopBar
import com.pemmob.foundu.ui.common.PageHeader
import androidx.compose.runtime.saveable.rememberSaveable
import com.pemmob.foundu.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(viewModel: CategoryViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var newCategoryName by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { FoundUTopBar(title = "Kategori") },
        containerColor = FoundUBackground
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            PageHeader(title = "Kategori", subtitle = "Kelola kategori barang temuan")

            // Input Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = FoundUSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Tambah Kategori Baru", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = FoundUText)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = newCategoryName,
                            onValueChange = { newCategoryName = it },
                            placeholder = { Text("Nama Kategori", color = Color.Gray) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FoundUPrimary,
                                unfocusedBorderColor = Color(0xFFE5E7EB)
                            )
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                if (newCategoryName.isNotBlank()) {
                                    viewModel.addCategory(newCategoryName)
                                    newCategoryName = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Box(
                                Modifier.background(
                                    Brush.linearGradient(listOf(FoundUPrimary, FoundUPrimaryDark)),
                                    RoundedCornerShape(10.dp)
                                ).padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Add, null, Modifier.size(16.dp), tint = Color.White)
                                    Spacer(Modifier.width(4.dp))
                                    Text("Tambah", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }

            // List
            when (val state = uiState) {
                is UiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(color = FoundUPrimary)
                }
                is UiState.Error -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("Gagal memuat: ${state.message}", color = MaterialTheme.colorScheme.error)
                }
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    Modifier.size(80.dp).background(FoundUPrimary.copy(alpha = 0.08f), RoundedCornerShape(20.dp)),
                                    Alignment.Center
                                ) { Icon(Icons.Default.Tag, null, Modifier.size(40.dp), tint = FoundUPrimary.copy(alpha = 0.5f)) }
                                Spacer(Modifier.height(12.dp))
                                Text("Belum ada kategori", fontWeight = FontWeight.Bold, color = FoundUText)
                            }
                        }
                    } else {
                        // Table-like card matching web
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = FoundUSurface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column {
                                // Table header
                                Row(
                                    Modifier.fillMaxWidth()
                                        .background(Brush.linearGradient(listOf(Color(0xFFF5F3FF), Color(0xFFEDE9FE))))
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    Arrangement.SpaceBetween
                                ) {
                                    Text("NAMA", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = FoundUMuted, letterSpacing = 0.5.sp)
                                    Text("AKSI", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = FoundUMuted, letterSpacing = 0.5.sp)
                                }

                                LazyColumn {
                                    items(state.data, key = { it.id }) { category ->
                                        Row(
                                            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                                            Arrangement.SpaceBetween, Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    Modifier.size(32.dp).background(BadgeCategoryBg, RoundedCornerShape(8.dp)),
                                                    Alignment.Center
                                                ) { Icon(Icons.Default.Tag, null, Modifier.size(16.dp), tint = BadgeCategoryText) }
                                                Spacer(Modifier.width(10.dp))
                                                Text(category.name, fontWeight = FontWeight.Medium, color = FoundUText)
                                            }
                                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                IconButton(
                                                    onClick = { viewModel.deleteCategory(category) },
                                                    modifier = Modifier.size(32.dp)
                                                        .background(Color(0xFFFEE2E2), RoundedCornerShape(8.dp))
                                                ) {
                                                    Icon(Icons.Default.Delete, "Hapus", Modifier.size(16.dp), tint = Color(0xFFDC2626))
                                                }
                                            }
                                        }
                                        HorizontalDivider(color = Color(0xFFF3F4F6))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
