package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.datalayer.repository.AsteroidRepository
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.ImageOfTheDay
import com.udacity.asteroidradar.utils.AppUtils
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(
    val repository: AsteroidRepository,
    val app: Application
) : AndroidViewModel(app) {
    val imageOTD = MutableLiveData<ImageOfTheDay>()
    val asteroidsGeneralList=MutableLiveData<List<Asteroid>>()
    val isLoading=MutableLiveData<Boolean>()

    fun getWeekAsteroids() = viewModelScope.launch {
        isLoading.postValue(true)
        repository.getWeekAsteroids()
        val asteroidsList=repository.savedAsteroidsList()
        asteroidsGeneralList.postValue(asteroidsList)
        isLoading.postValue(false)
    }

    fun getTodayAsteroids() = viewModelScope.launch {
        isLoading.postValue(true)
        repository.getTodayAsteroids()
        val asteroidsList=repository.todayAsteroidsList()
        asteroidsGeneralList.postValue(asteroidsList)
        isLoading.postValue(false)
    }

    fun getSavedAsteroids() = viewModelScope.launch {
        isLoading.postValue(true)
        val asteroidsList=repository.savedAsteroidsList()
        asteroidsGeneralList.postValue(asteroidsList)
        isLoading.postValue(false)
    }


    fun getImageOfTheDay() = viewModelScope.launch {
        safeGetImageOTD()
    }

    suspend private fun safeGetImageOTD() {
        try {
            if (AppUtils.hasInternetConnection(app)) {
                try {
                    repository.getImageOfTheDay().let { imageObject ->
                        imageObject.media_type?.let {
                            if (it == "image") {
                                imageOTD.postValue(imageObject)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Error","Error: ${e.message}")
                }
            } else {
                Log.d("Error","Error: No internet connection")
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> Log.d("Error","Error: Network failure")
                else -> Log.d("Error","Error: Conversion error")
            }
        }
    }

}