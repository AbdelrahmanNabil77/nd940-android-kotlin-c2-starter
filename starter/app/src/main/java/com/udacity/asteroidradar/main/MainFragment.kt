package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapters.MainRecyclerViewAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.datalayer.local.AsteroidsDatabase
import com.udacity.asteroidradar.datalayer.repository.AsteroidRepository
import com.udacity.asteroidradar.utils.ViewModelFactory

class MainFragment : Fragment() {
    lateinit var binding:FragmentMainBinding
    lateinit var viewModel: MainViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        val asteroidsDao=AsteroidsDatabase.getInstance(requireContext()).asteroidsDao
        val asteroidRepository=AsteroidRepository(asteroidsDao)
        val application = requireActivity().application as Application
        val viewModelProviderFactory = ViewModelFactory(asteroidRepository = asteroidRepository, application = application)
        viewModel=ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)
        binding.statusLoadingWheel.show()
        viewModel.asteroidsGeneralList.observe(viewLifecycleOwner){
            binding.asteroidRecycler.adapter=MainRecyclerViewAdapter(it,
                MainRecyclerViewAdapter.AsteroidListener {
                    val action=MainFragmentDirections.actionShowDetail(it)
                    findNavController().navigate(action)
                })
        }
        viewModel.isLoading.observe(viewLifecycleOwner){
            when(it){
                true->binding.statusLoadingWheel.show()
                false->binding.statusLoadingWheel.hide()
            }
        }
        viewModel.getTodayAsteroids()
        viewModel.getImageOfTheDay()
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.title){
            getString(R.string.next_week_asteroids)->{
                viewModel.getWeekAsteroids()
            }
            getString(R.string.today_asteroids)->{
                viewModel.getTodayAsteroids()
            }
            getString(R.string.saved_asteroids)->{
                viewModel.getSavedAsteroids()
            }
        }
        return true
    }
    fun View.show(){
        this.visibility=View.VISIBLE
    }
    fun View.hide(){
        this.visibility=View.GONE
    }
}
