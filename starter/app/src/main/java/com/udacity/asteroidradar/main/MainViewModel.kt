package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.datalayer.repository.AsteroidRepository
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.ImageOfTheDay
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
    private val _asteroids = MutableLiveData<Resource<List<Asteroid>>>()
    val asteroids: LiveData<Resource<List<Asteroid>>>
        get() = _asteroids
    val _imageOTD = MutableLiveData<Resource<ImageOfTheDay>>()

    fun getAsteroids() = viewModelScope.launch {
        safeGetAsteroids()
    }

    fun getImageOTD() = viewModelScope.launch {
        safeGetImageOTD()
    }

    private fun safeGetImageOTD() {
        _imageOTD.postValue(Resource.Loading())
        try {
            if (AppUtils.hasInternetConnection(app)) {
                repository.getImageOfTheDay().enqueue(object : Callback<List<ImageOfTheDay>> {
                    override fun onFailure(call: Call<List<ImageOfTheDay>>, t: Throwable) {
                        _imageOTD.postValue(Resource.Error("${t.message}"))
                    }

                    override fun onResponse(
                        call: Call<List<ImageOfTheDay>>,
                        response: Response<List<ImageOfTheDay>>
                    ) {
                        response.body()?.let { imageObject ->
                            if (imageObject.size > 0) {
                                imageObject.get(0).mediaType?.let {
                                    if (it == "image") {
                                        _imageOTD.postValue(Resource.Success(imageObject.get(0)))
                                    }
                                }
                            }
                        }
                    }
                })
            } else {
                _imageOTD.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _imageOTD.postValue(Resource.Error("Network Failure"))
                else -> _imageOTD.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun safeGetAsteroids() {
        _asteroids.postValue(Resource.Loading())
        try {
            if (AppUtils.hasInternetConnection(app)) {
                repository.getAsteroidsFromRetrofit().enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        _asteroids.postValue(Resource.Error("${t.message}"))
                        emitDataFromDB()
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        response.body()?.let {
                            viewModelScope.launch {
                                repository.clearAsteroids()
                                repository.insertAsteroids(getAsteroidsList(it))
                                emitDataFromDB()
                            }
                        }
                    }
                })
            } else {
                _asteroids.postValue(Resource.Error("No internet connection"))
                emitDataFromDB()

            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _asteroids.postValue(Resource.Error("Network Failure"))
                else -> _asteroids.postValue(Resource.Error("Conversion Error"))
            }
            emitDataFromDB()
        }
    }

    private fun getAsteroidsList(response: String): List<Asteroid> {
        val jsonObject = JSONObject(response)
        return parseAsteroidsJsonResult(jsonObject)
    }

    private fun emitDataFromDB(){
        viewModelScope.launch {
            repository.getAsteroidsFromDB().let {
                _asteroids.postValue(Resource.Success(it))
            }
        }
    }

}