package com.pemmob.foundu.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pemmob.foundu.data.local.entity.ItemStatus
import com.pemmob.foundu.ui.common.UiState
import com.pemmob.foundu.ui.common.FoundUTopBar
import com.pemmob.foundu.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    itemId: Long,
    onBack: () -> Unit
) {
    val application = LocalContext.current.applicationContext as android.app.Application
    val viewModel: ItemDetailViewModel = viewModel(
        factory = ItemDetailViewModel.Factory(application, itemId)
    )
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { FoundUTopBar(title = "Detail Barang", showBack = true, onBack = onBack) },
        containerColor = FoundUBackground
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center), color = FoundUPrimary)
                is UiState.Error -> Text("Gagal memuat: ${state.message}", color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                is UiState.Success -> {
                    val item = state.data
                    if (item == null) {
                        Text("Barang tidak ditemukan", color = FoundUMuted, modifier = Modifier.align(Alignment.Center))
                    } else {
                        Column(
                            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Photo placeholder
                            Box(
                                Modifier.fillMaxWidth().height(240.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Brush.linearGradient(listOf(Color(0xFFEDE9FE), Color(0xFFDDD6FE), Color(0xFFC4B5FD)))),
                                Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Image, null, Modifier.size(64.dp), tint = FoundUPrimaryDark.copy(alpha = 0.35f))
                                    Spacer(Modifier.height(8.dp))
                                    Text("Tidak ada foto", color = FoundUPrimaryDark.copy(alpha = 0.4f), fontSize = 12.sp)
                                }
                            }

                            // Main info card
                            Card(
                                Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = FoundUSurface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Column(Modifier.padding(20.dp)) {
                                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                                        StatusBadge(item.status)
                                        Text(
                                            SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(Date(item.reportedAt)),
                                            fontSize = 12.sp, color = FoundUMuted
                                        )
                                    }
                                    Spacer(Modifier.height(14.dp))
                                    Text(item.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = FoundUText)
                                    Spacer(Modifier.height(8.dp))
                                    Text(item.description, style = MaterialTheme.typography.bodyLarge, color = FoundUMuted, lineHeight = 22.sp)
                                }
                            }

                            // Info detail card
                            Card(
                                Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = FoundUSurface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                    Text("Informasi Detail", fontWeight = FontWeight.Bold, color = FoundUText, fontSize = 16.sp)
                                    HorizontalDivider(color = Color(0xFFF3F4F6))
                                    InfoRow(Icons.Default.Tag, "Kategori", "Kategori #${item.categoryId ?: "-"}")
                                    InfoRow(Icons.Default.LocationOn, "Titik Pengambilan", "Titik #${item.pickupPointId ?: "-"}")
                                    if (item.locationFound.isNotBlank()) {
                                        InfoRow(Icons.Default.LocationOn, "Lokasi Penemuan", item.locationFound)
                                    }
                                    if (item.reportedBy.isNotBlank()) {
                                        InfoRow(Icons.Default.Person, "Dilaporkan Oleh", item.reportedBy)
                                    }
                                }
                            }

                            // Update status card (Only for admin)
                            if (com.pemmob.foundu.data.local.UserSession.role == "admin") {
                                Card(
                                    Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = FoundUSurface),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Column(Modifier.padding(20.dp)) {
                                        Text("Update Status", fontWeight = FontWeight.Bold, color = FoundUText, fontSize = 16.sp)
                                        Spacer(Modifier.height(12.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            ItemStatus.entries.forEach { s ->
                                                FilterChip(
                                                    selected = item.status == s,
                                                    onClick = { viewModel.updateStatus(item, s) },
                                                    label = { Text(s.displayName, fontSize = 12.sp) },
                                                    colors = FilterChipDefaults.filterChipColors(
                                                        selectedContainerColor = FoundUPrimary.copy(alpha = 0.12f),
                                                        selectedLabelColor = FoundUPrimary
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }

                                // Delete button
                                Button(
                                    onClick = { showDeleteDialog = true },
                                    Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(vertical = 14.dp)
                                ) {
                                    Icon(Icons.Default.Delete, null, Modifier.size(18.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Hapus Barang", fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Barang?", fontWeight = FontWeight.Bold, color = FoundUText) },
            text = { Text("Barang ini akan dihapus secara permanen.", color = FoundUMuted) },
            confirmButton = {
                Button(
                    onClick = {
                        val s = uiState
                        if (s is UiState.Success && s.data != null) { viewModel.deleteItem(s.data); onBack() }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Hapus") }
            },
            dismissButton = { TextButton({ showDeleteDialog = false }) { Text("Batal", color = FoundUMuted) } },
            containerColor = FoundUSurface
        )
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(40.dp).background(FoundUPrimary.copy(alpha = 0.08f), RoundedCornerShape(12.dp)),
            Alignment.Center
        ) { Icon(icon, null, Modifier.size(20.dp), tint = FoundUPrimary) }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 11.sp, color = FoundUMuted)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = FoundUText)
        }
    }
}
