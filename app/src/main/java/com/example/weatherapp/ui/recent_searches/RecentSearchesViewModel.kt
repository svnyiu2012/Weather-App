package com.example.weatherapp.ui.recent_searches

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.repository.RecentSearchesRepository
import com.example.weatherapp.utils.Coroutines

class RecentSearchesViewModel @ViewModelInject constructor(
    private val recentSearchesRepository: RecentSearchesRepository,
) : ViewModel() {

    //get recent searches in db
    val recentSearches = recentSearchesRepository.getRecentSearchesList()

    //delete recent searches in db
    fun deleteRecentSearches(idList: List<String>) {
        Coroutines.io {
            recentSearchesRepository.deleteRecentSearches(idList)
        }
    }
}