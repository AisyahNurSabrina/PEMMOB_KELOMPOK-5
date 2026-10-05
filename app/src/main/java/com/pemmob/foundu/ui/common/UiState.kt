package com.pemmob.foundu.ui.common

/**
 * State generik untuk pola MVVM: setiap layar cukup nge-render 3 kondisi ini,
 * nggak perlu nebak-nebak kapan data null berarti loading vs error.
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
