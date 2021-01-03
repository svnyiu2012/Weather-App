package com.example.weatherapp.ui.current_weather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentCurrentWeatherBinding
import com.example.weatherapp.data.remote.utils.Resource
import com.example.weatherapp.utils.Util.hideKeyboard
import com.example.weatherapp.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_current_weather.view.*
import java.util.*

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {
    private val viewModel: CurrentWeatherViewModel by viewModels()
    private var binding: FragmentCurrentWeatherBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()

        binding.textViewBtnSearch.setOnClickListener {
            val input = binding.editTextSearchBox.text.toString()
            val numeric = input.matches("-?\\d+(\\.\\d+)?".toRegex())
            if (numeric)
                viewModel.getWeatherByZipCode(input)
            else
                viewModel.getWeatherByCityName(input)

            binding.editTextSearchBox.hideKeyboard()
        }

        binding.btnCurrentWeatherSearchByGps.setOnClickListener {
            if (checkLocationPermission()) {
                viewModel.getWeatherByGPS()
                Toast.makeText(this.requireContext(), "Search By GPS", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.btnCurrentWeatherRecentSearches.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_to_recent_searches_fragment,
                null
            )
        )
    }

    //set observers to display weather information
    private fun setupObservers() {
        viewModel.weather.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.layoutWeatherDetails.visibility = View.VISIBLE
                    binding.displayModel = it.data

                    Glide.with(this)
                        .load("http://openweathermap.org/img/w/${it.data?.weather?.get(0)?.icon}.png")
                        .fitCenter()
                        .into(binding.imageViewCurrentWeatherIcon);

                    viewModel.updateRecentSearches(
                        binding.displayModel!!.id,
                        binding.displayModel!!.name
                    )
                }
                Resource.Status.ERROR ->
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()

                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.layoutWeatherDetails.visibility = View.GONE
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        //if no arguments, just display most recent searches weather if its not null
        if (arguments?.isEmpty == true)
            viewModel.getMostRecentSearchesWeather()
        else
            arguments?.getString("name")?.let { viewModel.getWeatherByCityName(it) }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ((checkSelfPermission(
                            this.requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) === PackageManager.PERMISSION_GRANTED)
                    ) {
                        viewModel.getWeatherByGPS()
                    }
                } else {
                    Toast.makeText(this.requireContext(), "Permission Denied", Toast.LENGTH_SHORT)
                        .show()
                }
                return
            }
        }

    }


    fun checkLocationPermission(): Boolean {
        if (checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
            ) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
            return false
        }
        return true
    }
}