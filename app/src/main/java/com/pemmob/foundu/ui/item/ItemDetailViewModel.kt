package com.pemmob.foundu.ui.item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewModelScope
import com.pemmob.foundu.data.local.AppDatabase
import com.pemmob.foundu.data.local.entity.Item
import com.pemmob.foundu.ui.common.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemDetailViewModel(
    application: Application,
    itemId: Long
) : AndroidViewModel(application) {

    private val itemDao = AppDatabase.getInstance(application).itemDao()

    val uiState: StateFlow<UiState<Item?>> = itemDao.getById(itemId)
        .map<Item?, UiState<Item?>> { UiState.Success(it) }
        .catch { e -> emit(UiState.Error(e.message ?: "Gagal memuat detail barang")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    fun deleteItem(item: Item) {
        viewModelScope.launch { itemDao.delete(item) }
    }

    fun updateStatus(item: Item, newStatus: com.pemmob.foundu.data.local.entity.ItemStatus) {
        viewModelScope.launch { itemDao.insert(item.copy(status = newStatus)) }
    }

    class Factory(
        private val application: Application,
        private val itemId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ItemDetailViewModel(application, itemId) as T
        }
    }
}
