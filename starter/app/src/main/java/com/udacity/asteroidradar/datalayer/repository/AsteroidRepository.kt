package com.udacity.asteroidradar.datalayer.repository

import com.udacity.asteroidradar.datalayer.remote.RetrofitInstance
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.utils.DateUtils.getTodayDate

class AsteroidRepository {
    //remote
    fun getAsteroids()= RetrofitInstance.api.getAsteroids(start_date = getTodayDate(),Constants.API_KEY)
    fun getImageOfTheDay()= RetrofitInstance.api.getImageOfTheDay(start_date = getTodayDate(), api_key = Constants.API_KEY)
}