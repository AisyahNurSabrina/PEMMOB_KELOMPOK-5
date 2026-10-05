package com.pemmob.foundu.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pemmob.foundu.data.local.AppDatabase
import com.pemmob.foundu.data.local.entity.Category
import com.pemmob.foundu.data.local.entity.Item
import com.pemmob.foundu.data.local.entity.PickupPoint
import com.pemmob.foundu.data.local.entity.ItemStatus
import com.pemmob.foundu.ui.common.UiState
import com.pemmob.foundu.ui.common.FoundUTopBar
import com.pemmob.foundu.ui.common.GradientButton
import com.pemmob.foundu.ui.common.PageHeader
import com.pemmob.foundu.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(
    onItemClick: (Item) -> Unit = {},
    viewModel: ItemViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedFilter by rememberSaveable { mutableStateOf<String?>(null) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    // Ambil kategori dan titik pengambilan dari database supaya ID yang
    // disimpan ke Item selalu merupakan ID yang benar-benar ada di Room.
    val context = LocalContext.current
    val database = remember(context) { AppDatabase.getInstance(context) }
    val categories by database.categoryDao().getAll().collectAsState(initial = emptyList())
    val pickupPoints by database.pickupPointDao().getAll().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            FoundUTopBar(title = "Barang Temuan")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = FoundUPrimary,
                contentColor = FoundUSurface,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Laporkan Barang")
            }
        },
        containerColor = FoundUBackground
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            PageHeader(
                title = "Barang Temuan",
                subtitle = "Daftar barang yang ditemukan di area kampus"
            )

            // Search & Filter Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = FoundUSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Ketik nama barang...", color = FoundUMuted) },
                        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = FoundUMuted) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FoundUPrimary,
                            unfocusedBorderColor = Color(0xFFE5E7EB)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Filter Status", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = FoundUMuted)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilterChip(
                            selected = selectedFilter == null,
                            onClick = { selectedFilter = null },
                            label = { Text("Semua", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FoundUPrimary.copy(alpha = 0.12f),
                                selectedLabelColor = FoundUPrimary
                            )
                        )
                        ItemStatus.entries.forEach { status ->
                            FilterChip(
                                selected = selectedFilter == status.name,
                                onClick = { selectedFilter = status.name },
                                label = { Text(status.displayName, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = FoundUPrimary.copy(alpha = 0.12f),
                                    selectedLabelColor = FoundUPrimary
                                )
                            )
                        }
                    }
                }
            }

            when (val state = uiState) {
                is UiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(color = FoundUPrimary)
                }
                is UiState.Error -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("Gagal memuat: ${state.message}", color = MaterialTheme.colorScheme.error)
                }
                is UiState.Success -> {
                    val filtered = state.data.filter {
                        (selectedFilter == null || it.status.name == selectedFilter) &&
                                (searchQuery.isBlank() || it.title.contains(searchQuery, ignoreCase = true))
                    }
                    if (filtered.isEmpty()) {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier.size(80.dp)
                                        .background(FoundUPrimary.copy(alpha = 0.08f), RoundedCornerShape(20.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Outlined.Search, null, Modifier.size(40.dp), tint = FoundUPrimary.copy(alpha = 0.5f))
                                }
                                Spacer(Modifier.height(12.dp))
                                Text("Belum ada barang temuan", fontWeight = FontWeight.Bold, color = FoundUText)
                                Text("Jadilah yang pertama melaporkan!", color = FoundUMuted, fontSize = 13.sp)
                            }
                        }
                    } else {
                        LazyColumn(
                            Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filtered, key = { it.id }) { item ->
                                ItemCard(item = item, onClick = { onItemClick(item) })
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddItemDialog(
            categories = categories,
            pickupPoints = pickupPoints,
            onDismiss = { showAddDialog = false },
            onSave = { title, description, status, locationFound, categoryId, pickupPointId ->
                viewModel.addItem(title, description, status, categoryId, pickupPointId, locationFound)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun ItemCard(item: Item, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = FoundUSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column {
            // Foto / Placeholder
            Box(
                modifier = Modifier.fillMaxWidth().height(180.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(Brush.linearGradient(colors = listOf(Color(0xFFEDE9FE), Color(0xFFDDD6FE), Color(0xFFC4B5FD)))),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Image, null, Modifier.size(56.dp), tint = FoundUPrimaryDark.copy(alpha = 0.35f))
            }

            Column(modifier = Modifier.padding(14.dp)) {
                // Status + Time row
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    StatusBadge(item.status)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Tag, null, Modifier.size(12.dp), tint = FoundUMuted)
                        Spacer(Modifier.width(3.dp))
                        Text(
                            SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(Date(item.reportedAt)),
                            fontSize = 11.sp, color = FoundUMuted
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))
                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = FoundUText)
                Spacer(Modifier.height(4.dp))
                Text(item.description, style = MaterialTheme.typography.bodySmall, color = FoundUMuted, maxLines = 2, overflow = TextOverflow.Ellipsis)

                Spacer(Modifier.height(10.dp))

                // Category + Location badges (matching web exactly)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    WebBadge(text = "Kategori", bgColor = BadgeCategoryBg, textColor = BadgeCategoryText, icon = Icons.Default.Tag)
                    if (item.locationFound.isNotBlank()) {
                        WebBadge(text = item.locationFound, bgColor = BadgeLocationBg, textColor = BadgeLocationText, icon = Icons.Default.LocationOn)
                    } else {
                        WebBadge(text = "Lokasi", bgColor = BadgeLocationBg, textColor = BadgeLocationText, icon = Icons.Default.LocationOn)
                    }
                }

                Spacer(Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFFF3F4F6))
                Spacer(Modifier.height(10.dp))

                // Footer: reporter + detail button
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(24.dp).background(FoundUPrimary.copy(alpha = 0.1f), RoundedCornerShape(6.dp)),
                            Alignment.Center
                        ) {
                            Text(if (item.reportedBy.isNotEmpty()) item.reportedBy.first().uppercase() else "U",
                                fontSize = 11.sp, fontWeight = FontWeight.Bold, color = FoundUPrimary)
                        }
                        Spacer(Modifier.width(6.dp))
                        Text(if (item.reportedBy.isNotBlank()) item.reportedBy else "Anonim", fontSize = 12.sp, color = FoundUMuted)
                    }
                    OutlinedButton(
                        onClick = {},
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = FoundUPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FoundUPrimary.copy(alpha = 0.3f))
                    ) {
                        Text("Detail", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.width(4.dp))
                        Icon(Icons.Default.Tag, null, Modifier.size(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun WebBadge(
    text: String,
    bgColor: Color,
    textColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier.background(bgColor, RoundedCornerShape(20.dp)).padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, Modifier.size(11.dp), tint = textColor)
        Spacer(Modifier.width(4.dp))
        Text(text, fontSize = 11.sp, color = textColor, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun StatusBadge(status: ItemStatus) {
    val (bgColor, textColor) = when (status) {
        ItemStatus.tersedia -> BadgeTersediaBg to BadgeTersediaText
        ItemStatus.diverifikasi -> BadgeDiverifikasiBg to BadgeDiverifikasiText
        ItemStatus.terklaim -> BadgeTerklaimBg to BadgeTerklaimText
    }
    Box(Modifier.background(bgColor, RoundedCornerShape(20.dp)).padding(horizontal = 10.dp, vertical = 4.dp)) {
        Text(status.displayName, fontSize = 11.sp, color = textColor, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TagChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        modifier = Modifier.background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp)).padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, Modifier.size(12.dp), tint = FoundUMuted)
        Spacer(Modifier.width(4.dp))
        Text(text, fontSize = 11.sp, color = FoundUMuted, fontWeight = FontWeight.Medium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(
    categories: List<Category>,
    pickupPoints: List<PickupPoint>,
    onDismiss: () -> Unit,
    onSave: (String, String, ItemStatus, String, Long?, Long?) -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var locationFound by rememberSaveable { mutableStateOf("") }
    var statusName by rememberSaveable { mutableStateOf(ItemStatus.tersedia.name) }
    // Kalau tabel masih kosong, tetap tampilkan pilihan seperti UI lama.
    // Karena tidak ada row database yang sesuai, ID yang dikirim akan null
    // sehingga foreign key Room tidak akan gagal.
    val categoryLabels = if (categories.isNotEmpty()) {
        categories.map { it.name }
    } else {
        listOf("Elektronik", "Dokumen", "Aksesori", "Lainnya")
    }
    val pickupPointLabels = if (pickupPoints.isNotEmpty()) {
        pickupPoints.map { it.name }
    } else {
        listOf("Pos Gerbang Utama", "Pos Fakultas A", "Pos Perpustakaan")
    }

    var selectedCategory by rememberSaveable { mutableStateOf(0) }
    var selectedPickupPoint by rememberSaveable { mutableStateOf(0) }

    val safeCategoryIndex = selectedCategory.coerceIn(0, categoryLabels.lastIndex.coerceAtLeast(0))
    val safePickupPointIndex = selectedPickupPoint.coerceIn(0, pickupPointLabels.lastIndex.coerceAtLeast(0))
    var showCategoryMenu by remember { mutableStateOf(false) }
    var showPickupMenu by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Laporkan Barang Temuan", fontWeight = FontWeight.Bold, color = FoundUText, fontSize = 18.sp) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(14.dp)) {

                // Nama Barang
                Column {
                    Text("Nama Barang *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = FoundUText)
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(title, { title = it }, Modifier.fillMaxWidth(), placeholder = { Text("Contoh: Dompet Hitam", color = Color.Gray) },
                        shape = RoundedCornerShape(10.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FoundUPrimary, unfocusedBorderColor = Color(0xFFE5E7EB)))
                }

                // Deskripsi
                Column {
                    Text("Deskripsi *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = FoundUText)
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(description, { description = it }, Modifier.fillMaxWidth(), placeholder = { Text("Jelaskan ciri-ciri barang", color = Color.Gray) },
                        shape = RoundedCornerShape(10.dp), minLines = 3, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FoundUPrimary, unfocusedBorderColor = Color(0xFFE5E7EB)))
                }

                // Kategori
                Column {
                    Text("Kategori *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = FoundUText)
                    Spacer(Modifier.height(6.dp))
                    ExposedDropdownMenuBox(showCategoryMenu, { showCategoryMenu = it }) {
                        OutlinedTextField(categoryLabels[safeCategoryIndex], {}, readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(showCategoryMenu) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FoundUPrimary, unfocusedBorderColor = Color(0xFFE5E7EB)))
                        ExposedDropdownMenu(showCategoryMenu, { showCategoryMenu = false }) {
                            categoryLabels.forEachIndexed { i, label ->
                                DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        selectedCategory = i
                                        showCategoryMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Titik Pengambilan
                Column {
                    Text("Titik Pengambilan *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = FoundUText)
                    Spacer(Modifier.height(6.dp))
                    ExposedDropdownMenuBox(showPickupMenu, { showPickupMenu = it }) {
                        OutlinedTextField(pickupPointLabels[safePickupPointIndex], {}, readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(showPickupMenu) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FoundUPrimary, unfocusedBorderColor = Color(0xFFE5E7EB)))
                        ExposedDropdownMenu(showPickupMenu, { showPickupMenu = false }) {
                            pickupPointLabels.forEachIndexed { i, label ->
                                DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        selectedPickupPoint = i
                                        showPickupMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Lokasi Penemuan
                Column {
                    Text("Lokasi Penemuan *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = FoundUText)
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(locationFound, { locationFound = it }, Modifier.fillMaxWidth(), placeholder = { Text("Contoh: Depan Gedung FT", color = Color.Gray) },
                        shape = RoundedCornerShape(10.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FoundUPrimary, unfocusedBorderColor = Color(0xFFE5E7EB)))
                }

                // Status
                Column {
                    Text("Status", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = FoundUText)
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ItemStatus.entries.forEach { s ->
                            FilterChip(statusName == s.name, { statusName = s.name }, { Text(s.displayName, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = FoundUPrimary.copy(alpha = 0.12f), selectedLabelColor = FoundUPrimary))
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        // Pakai ID asli dari database. Jangan lagi memakai
                        // index + 1 karena belum tentu cocok dengan primary key.
                        val categoryId = categories.getOrNull(safeCategoryIndex)?.id
                        val pickupPointId = pickupPoints.getOrNull(safePickupPointIndex)?.id

                        onSave(
                            title,
                            description,
                            ItemStatus.valueOf(statusName),
                            locationFound,
                            categoryId,
                            pickupPointId
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Box(Modifier.background(Brush.linearGradient(listOf(FoundUPrimary, FoundUPrimaryDark)), RoundedCornerShape(10.dp)).padding(horizontal = 20.dp, vertical = 10.dp)) {
                    Text("Simpan", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = { TextButton(onDismiss) { Text("Batal", color = FoundUMuted) } },
        containerColor = FoundUSurface
    )
}
