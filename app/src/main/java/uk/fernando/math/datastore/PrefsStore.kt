package uk.fernando.math.datastore

import kotlinx.coroutines.flow.Flow

interface PrefsStore {

    suspend fun isFirstTime(): Boolean
    fun isDarkMode(): Flow<Boolean>
    fun allowDecimals(): Flow<Boolean>
    fun isPremium(): Flow<Boolean>

    suspend fun storeFirstTime(value: Boolean)
    suspend fun storeDarkMode(value: Boolean)
    suspend fun storeAllowDecimals(value: Boolean)
    suspend fun storePremium(value: Boolean)
}