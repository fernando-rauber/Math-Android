package uk.fernando.math.datastore

import kotlinx.coroutines.flow.Flow

interface PrefsStore {

    suspend fun getVersion(): Int
    fun isDarkMode(): Flow<Boolean>
    fun allowDecimals(): Flow<Boolean>
    fun isPremium(): Flow<Boolean>
    fun notificationEnable(): Flow<Boolean>

    suspend fun storeVersion(value: Int)
    suspend fun storeDarkMode(value: Boolean)
    suspend fun storeAllowDecimals(value: Boolean)
    suspend fun storePremium(value: Boolean)
    suspend fun storeNotification(value: Boolean)
}