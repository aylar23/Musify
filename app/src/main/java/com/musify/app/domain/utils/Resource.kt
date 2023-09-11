package com.musify.app.domain.utils

/**
 * A helper class for when loading data from remote database.
 * To Clarify the current state of our loading.
 * To help what to show in the UI when load function os called
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
    class Loading<T>(isLoading: Boolean = true): Resource<T>(null)
}
