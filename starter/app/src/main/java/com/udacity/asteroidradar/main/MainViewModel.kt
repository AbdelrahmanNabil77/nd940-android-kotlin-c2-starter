package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.datalayer.repository.AsteroidRepository
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.utils.AppUtils
import com.udacity.asteroidradar.utils.Resource
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MainViewModel(
    val repository: AsteroidRepository,
    val app: Application
) : AndroidViewModel(app) {
    val _asteroids = MutableLiveData<Resource<List<Asteroid>>>()
    val asteroids: LiveData<Resource<List<Asteroid>>>
        get() = _asteroids


    fun getAsteroids() = viewModelScope.launch {
        safeGetAsteroids()
    }

    private fun safeGetAsteroids() {
        _asteroids.postValue(Resource.Loading())
        try {
            if (AppUtils.hasInternetConnection(app)) {
                repository.getAsteroids().enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        _asteroids.postValue(Resource.Error("${t.message}"))
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        response.body()?.let {
                            _asteroids.postValue(Resource.Success(getAsteroidsList(it)))
                        }
                    }
                })
            } else {
                _asteroids.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _asteroids.postValue(Resource.Error("Network Failure"))
                else -> _asteroids.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun getAsteroidsList(response: String): List<Asteroid> {
        val jsonObject = JSONObject(response)
        return parseAsteroidsJsonResult(jsonObject)
    }
    
}