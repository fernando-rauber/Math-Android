package uk.fernando.math.datastore

import kotlinx.coroutines.flow.Flow

interface PrefsStore {

    suspend fun isFirstTime(): Boolean
    fun isDarkMode(): Flow<Boolean>
    suspend fun allowDecimals(): Boolean
    suspend fun isPremium(): Boolean

    suspend fun storeFirstTime(value: Boolean)
    suspend fun storeDarkMode(value: Boolean)
    suspend fun storeAllowDecimals(value: Boolean)
    suspend fun storePremium(value: Boolean)
}