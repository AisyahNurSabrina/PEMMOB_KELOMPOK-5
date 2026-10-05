package com.pemmob.foundu.ui.pickuppoint

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.foundu.data.local.AppDatabase
import com.pemmob.foundu.data.local.entity.PickupPoint
import com.pemmob.foundu.ui.common.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PickupPointViewModel(application: Application) : AndroidViewModel(application) {

    private val pickupPointDao = AppDatabase.getInstance(application).pickupPointDao()

    val uiState: StateFlow<UiState<List<PickupPoint>>> = pickupPointDao.getAll()
        .map<List<PickupPoint>, UiState<List<PickupPoint>>> { UiState.Success(it) }
        .catch { e -> emit(UiState.Error(e.message ?: "Gagal memuat titik ambil")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    fun addPickupPoint(name: String, location: String) {
        if (name.isBlank() || location.isBlank()) return
        viewModelScope.launch {
            pickupPointDao.insert(PickupPoint(name = name.trim(), location = location.trim()))
        }
    }

    fun deletePickupPoint(pickupPoint: PickupPoint) {
        viewModelScope.launch { pickupPointDao.delete(pickupPoint) }
    }
}
