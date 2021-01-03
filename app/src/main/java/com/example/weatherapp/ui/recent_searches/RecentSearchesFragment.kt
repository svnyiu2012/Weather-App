package com.example.weatherapp.ui.recent_searches

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.data.local.entity.RecentSearchesEntry
import com.example.weatherapp.databinding.FragmentRecentSearchesBinding
import com.example.weatherapp.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentSearchesFragment : Fragment(), RecentSearchesAdapter.Callback {
    private val viewModel: RecentSearchesViewModel by viewModels()
    private var binding: FragmentRecentSearchesBinding by autoCleared()
    private lateinit var adapter: RecentSearchesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecentSearchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecentSearchesAdapter(this)
        binding.recyclerviewRecentSearchesList.adapter = adapter
        binding.btnRecentSearchesBtnBack.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_to_current_weather_fragment,
                null
            )
        )
        binding.btnRecentSearchesBtnDel.setOnClickListener {
            binding.btnRecentSearchesBtnDel.visibility = View.GONE
            binding.btnRecentSearchesBtnBack.visibility = View.GONE
            binding.layoutRecentSearchesDeleteDone.visibility = View.VISIBLE

            adapter.setDeleteMode(true)
        }

        binding.layoutRecentSearchesDeleteDone.setOnClickListener {
            binding.btnRecentSearchesBtnDel.visibility = View.VISIBLE
            binding.btnRecentSearchesBtnBack.visibility = View.VISIBLE
            binding.layoutRecentSearchesDeleteDone.visibility = View.GONE

            adapter.setDeleteMode(false)
            viewModel.deleteRecentSearches(adapter.getDeleteItemList())
        }
        setupObservers()
    }

    //set observers to display recent searches information
    private fun setupObservers() {
        viewModel.recentSearches.observe(viewLifecycleOwner, {
            adapter.setRecentSearchesList(it)
        })
    }

    override fun onItemClickListener(recentSearchesEntry: RecentSearchesEntry) {
        val bundle = bundleOf("name" to recentSearchesEntry.name)
        val navController = activity?.let { findNavController(it, R.id.nav_host_fragment) }
        navController?.navigate(R.id.action_to_current_weather_fragment, bundle)
    }
}