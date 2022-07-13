package uk.fernando.math.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val STORE_NAME = "math_game_data_store"

val Context.gameDataStore: DataStore<Preferences> by preferencesDataStore(STORE_NAME)

class GamePrefsStoreImpl(context: Context) : GamePrefsStore {

    private val dataStore = context.gameDataStore

    override fun getOperators(): Flow<List<Int>> {
        return dataStore.data.map { prefs ->
            val operators = prefs[PreferencesKeys.OPERATORS]
            if (operators == null)
                listOf(1, 2, 3, 4)
            if (operators != null && operators.isNotEmpty())
                operators.split("*").map { it.toInt() }
            else
                emptyList()
        }
    }

    override fun quantity(): Flow<Int> {
        return dataStore.data.map { prefs -> prefs[PreferencesKeys.QUANTITY] ?: 10 }
    }

    override fun isMultipleChoice(): Flow<Boolean> {
        return dataStore.data.map { prefs -> prefs[PreferencesKeys.MULTIPLE_CHOICE] ?: true }
    }

    override fun difficulty(): Flow<Int> {
        return dataStore.data.map { prefs -> prefs[PreferencesKeys.DIFFICULTY] ?: 1 }
    }

    override suspend fun storeOperator(value: List<Int>) {
        dataStore.edit { prefs -> prefs[PreferencesKeys.OPERATORS] = value.map { it }.joinToString(separator = "*") }
    }

    override suspend fun storeQuantity(value: Int) {
        dataStore.edit { prefs -> prefs[PreferencesKeys.QUANTITY] = value }
    }

    override suspend fun storeMultipleChoice(value: Boolean) {
        dataStore.edit { prefs -> prefs[PreferencesKeys.MULTIPLE_CHOICE] = value }
    }

    override suspend fun storeDifficulty(value: Int) {
        dataStore.edit { prefs -> prefs[PreferencesKeys.DIFFICULTY] = value }
    }

    private object PreferencesKeys {
        val OPERATORS = stringPreferencesKey("operator")
        val QUANTITY = intPreferencesKey("quantity")
        val MULTIPLE_CHOICE = booleanPreferencesKey("multiple_choice")
        val DIFFICULTY = intPreferencesKey("difficulty")
    }
}