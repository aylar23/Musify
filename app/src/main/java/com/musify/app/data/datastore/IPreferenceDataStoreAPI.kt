package com.musify.app.data.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow


interface IPreferenceDataStoreAPI {
     fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T>
    suspend fun <T> getFirstPreference(key: Preferences.Key<T>,defaultValue: T):T
    suspend fun <T> putPreference(key: Preferences.Key<T>,value:T)
    suspend fun <T> removePreference(key: Preferences.Key<T>)
    suspend fun <T> clearAllPreference()
}

object PreferenceDataStoreConstants {
    val USER_ID_KEY = stringPreferencesKey("USER_ID")
    val TOKEN_KEY = stringPreferencesKey("TOKEN")
    val PHONE_KEY = stringPreferencesKey("PHONE")
    val LANGUAGE_KEY = stringPreferencesKey("LANGUAGE")
}