package com.example.weatherapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp.data.local.entity.RecentSearchesEntry

@Dao
interface RecentSearchesDao {

    @Query("select * from recent_searches_table ORDER BY lastAccessDateTime DESC LIMIT 1")
    fun getMostRecentSearches(): RecentSearchesEntry

    @Query("select * from recent_searches_table ORDER BY lastAccessDateTime DESC")
    fun getRecentSearches(): LiveData<List<RecentSearchesEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRecentSearches(weatherRecentSearches: RecentSearchesEntry)

    @Query("delete from recent_searches_table where id in (:idList)")
    suspend fun deleteRecentSearches(idList: List<String>)
}