package uk.fernando.math.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*

private const val STORE_NAME = "math_data_store"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(STORE_NAME)

class PrefsStoreImpl(context: Context) : PrefsStore {

    private val dataStore = context.dataStore
    override suspend fun isFirstTime(): Boolean {
        return dataStore.data.map { prefs -> prefs[PreferencesKeys.FIRST_TIME] ?: true }.first()
    }

    override fun isDarkMode(): Flow<Boolean> {
        return dataStore.data.map { prefs -> prefs[PreferencesKeys.DARK_MODE] ?: false }
    }

    override fun allowDecimals(): Flow<Boolean> {
        return dataStore.data.map { prefs -> prefs[PreferencesKeys.ALLOW_DECIMALS] ?: false }
    }

    override fun isPremium(): Flow<Boolean> {
        return dataStore.data.map { prefs -> prefs[PreferencesKeys.PREMIUM] ?: false }
    }

    override fun notificationEnable(): Flow<Boolean> {
        return dataStore.data.map { prefs -> prefs[PreferencesKeys.NOTIFICATION] ?: true }
    }

    override suspend fun storeFirstTime(value: Boolean) {
        dataStore.edit { prefs -> prefs[PreferencesKeys.FIRST_TIME] = value }
    }

    override suspend fun storeDarkMode(value: Boolean) {
        dataStore.edit { prefs -> prefs[PreferencesKeys.DARK_MODE] = value }
    }

    override suspend fun storeAllowDecimals(value: Boolean) {
        dataStore.edit { prefs -> prefs[PreferencesKeys.ALLOW_DECIMALS] = value }
    }

    override suspend fun storePremium(value: Boolean) {
        dataStore.edit { prefs -> prefs[PreferencesKeys.PREMIUM] = value }
    }

    override suspend fun storeNotification(value: Boolean) {
        dataStore.edit { prefs -> prefs[PreferencesKeys.NOTIFICATION] = value }
    }

    private object PreferencesKeys {
        val FIRST_TIME = booleanPreferencesKey("first_time")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val ALLOW_DECIMALS = booleanPreferencesKey("allow_decimals")
        val PREMIUM = booleanPreferencesKey("premium")
        val NOTIFICATION = booleanPreferencesKey("notification")
    }
}