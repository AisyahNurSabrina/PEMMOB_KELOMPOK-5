package com.pemmob.foundu.ui.item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.foundu.data.local.AppDatabase
import com.pemmob.foundu.data.local.entity.Item
import com.pemmob.foundu.data.local.entity.ItemStatus
import com.pemmob.foundu.ui.common.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application) {

    private val itemDao = AppDatabase.getInstance(application).itemDao()

    val uiState: StateFlow<UiState<List<Item>>> = itemDao.getAll()
        .map<List<Item>, UiState<List<Item>>> { UiState.Success(it) }
        .catch { e -> emit(UiState.Error(e.message ?: "Gagal memuat data barang")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    fun addItem(
        title: String,
        description: String,
        status: ItemStatus,
        categoryId: Long?,
        pickupPointId: Long?,
        locationFound: String = ""
    ) {
        viewModelScope.launch {
            itemDao.insert(
                Item(
                    title = title,
                    description = description,
                    status = status,
                    categoryId = categoryId,
                    pickupPointId = pickupPointId,
                    locationFound = locationFound
                )
            )
        }
    }

    fun updateStatus(item: Item, newStatus: ItemStatus) {
        viewModelScope.launch {
            itemDao.insert(item.copy(status = newStatus))
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch { itemDao.delete(item) }
    }
}
