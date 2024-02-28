package com.musify.app.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.musify.app.Search
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class DataStoreUtil @Inject constructor(context: Context) {

    val dataStore = context.dataStore

    companion object {
        private val Context.dataStore: DataStore<Search> by dataStore(
            fileName = "search_store",
            serializer = SearchSerializer,
        )
    }
}

object SearchSerializer : Serializer<Search> {
    override val defaultValue: Search = Search.getDefaultInstance()

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun readFrom(input: InputStream): Search {
        try {
            return Search.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: Search, output: OutputStream) = t.writeTo(output)
}


class SearchDataStore constructor(private val myMessagesStore: DataStore<Search>) {

    private val TAG: String = "MessageDataStore"

    val searchFlow: Flow<Search> = myMessagesStore.data
        .catch { exception ->
            Log.e(TAG, exception.message.toString() )
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading sort order preferences.", exception)
                emit(Search.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun saveNewMsg(newMsg: String) {
        myMessagesStore.updateData { myMessages ->
            var existingMessages = myMessages.toBuilder().messageList.toMutableList()
            if (existingMessages.contains(newMsg)) {
                myMessages
            } else{
                existingMessages.add(0, newMsg)
                if (existingMessages.size > 10){
                    existingMessages = existingMessages.subList(0,10)
                }
                myMessages.toBuilder().clear().addAllMessage(existingMessages).build()
            }

        }
    }

    suspend fun clearAllMyMessages() {
        myMessagesStore.updateData { preferences ->
            preferences.toBuilder().clear().build()
        }
    }

    suspend fun removeMsg(msg : String) {
        myMessagesStore.updateData { myMessages ->
            val existingMessages = myMessages.toBuilder().messageList
            if (existingMessages.contains(msg)) {
                val newList = existingMessages.filter { it != msg }
                myMessages.toBuilder().clear().addAllMessage(newList).build()
            } else {
                myMessages
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SearchDataStore? = null

        fun getInstance(dataStore: DataStore<Search>): SearchDataStore {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }
                val instance = SearchDataStore(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}