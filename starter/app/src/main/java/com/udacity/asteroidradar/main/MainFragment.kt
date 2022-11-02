package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapters.MainRecyclerViewAdapter
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.datalayer.repository.AsteroidRepository
import com.udacity.asteroidradar.utils.DateUtils.getTodayDate
import com.udacity.asteroidradar.utils.Resource
import com.udacity.asteroidradar.utils.ViewModelFactory
import org.json.JSONObject

class MainFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val asteroidRepository=AsteroidRepository()
        val application = requireActivity().application as Application
        val viewModelProviderFactory = ViewModelFactory(asteroidRepository = asteroidRepository, application = application)
        viewModel=ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)
        viewModel.asteroids.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("nasa_tag", "data: ${it.data}")
                    it.data?.let {
                        binding.asteroidRecycler.adapter=MainRecyclerViewAdapter(it)
                    }

                }
                is Resource.Error -> {
                    Log.d("nasa_tag", "error: ${it.message}")
                }
            }
        }

        viewModel.getAsteroids()
        viewModel.getImageOTD()
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
