package com.udacity.asteroidradar.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.datalayer.repository.AsteroidRepository
import com.udacity.asteroidradar.main.MainViewModel

class ViewModelFactory(
    val asteroidRepository: AsteroidRepository,
    val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(asteroidRepository,application) as T
        }
        else {
            throw IllegalArgumentException("ViewModel Not Found")
        }

    }
}