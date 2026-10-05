package com.pemmob.foundu.ui.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.foundu.data.local.AppDatabase
import com.pemmob.foundu.data.local.entity.Category
import com.pemmob.foundu.ui.common.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val categoryDao = AppDatabase.getInstance(application).categoryDao()

    val uiState: StateFlow<UiState<List<Category>>> = categoryDao.getAll()
        .map<List<Category>, UiState<List<Category>>> { UiState.Success(it) }
        .catch { e -> emit(UiState.Error(e.message ?: "Gagal memuat kategori")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    fun addCategory(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch { categoryDao.insert(Category(name = name.trim())) }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch { categoryDao.delete(category) }
    }
}
