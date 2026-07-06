package ru.kubsu.market.ui.cringe

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

private const val USER_PREFERENCES_NAME = "user_preferences"

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    val accessToken: Flow<String?> = dataStore.data.map { pref ->
        pref[ACCESS_TOKEN_KEY]
    }

    val refreshToken: Flow<String?> = dataStore.data.map { pref ->
        pref[REFRESH_TOKEN_KEY]
    }

    suspend fun saveTokens(access: String, refresh: String) {
        dataStore.edit { pref ->
            pref[ACCESS_TOKEN_KEY] = access
            pref[REFRESH_TOKEN_KEY] = refresh
        }
    }

    suspend fun clearTokens() {
        dataStore.edit { pref ->
            pref.remove(ACCESS_TOKEN_KEY)
            pref.remove(REFRESH_TOKEN_KEY)
        }
    }

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }
}
