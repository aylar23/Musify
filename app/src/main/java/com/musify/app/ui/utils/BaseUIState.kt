package com.musify.app.ui.utils



data class BaseUIState<T>(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isFailure: Boolean = false,
    val data: T? = null,
){
    fun updateToLoading(): BaseUIState<T> {
        return copy(isLoading = true)
    }

    fun updateToLoaded(data: T): BaseUIState<T> {
        return copy(isSuccess = true,isLoading = false, data = data, isFailure = false)
    }

    fun updateToFailure(): BaseUIState<T> {
        return copy(isLoading = false, isFailure = true)
    }
}