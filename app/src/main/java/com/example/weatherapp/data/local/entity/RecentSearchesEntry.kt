package com.example.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_searches_table")
data class RecentSearchesEntry(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val lastAccessDateTime: Long
)
