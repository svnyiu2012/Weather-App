package com.example.weatherapp.data.repository

import com.example.weatherapp.data.local.entity.RecentSearchesEntry
import com.example.weatherapp.data.local.RecentSearchesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecentSearchesRepository @Inject constructor(
    private val localDataSource: RecentSearchesDao
) {
    fun getRecentSearchesList() = localDataSource.getRecentSearches()

    fun getMostRecentSearches() = localDataSource.getMostRecentSearches()

    suspend fun updateRecentSearches(recentSearchesEntry: RecentSearchesEntry) {
        return withContext(Dispatchers.IO) {
            localDataSource.updateRecentSearches(recentSearchesEntry)
        }
    }

    suspend fun deleteRecentSearches(idList: List<String>) {
        return withContext(Dispatchers.IO) {
            localDataSource.deleteRecentSearches(idList)
        }
    }
}