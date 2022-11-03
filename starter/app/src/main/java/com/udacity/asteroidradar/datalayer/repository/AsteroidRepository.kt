package com.udacity.asteroidradar.datalayer.repository

import com.udacity.asteroidradar.datalayer.local.AsteroidsDao
import com.udacity.asteroidradar.datalayer.remote.RetrofitInstance
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.utils.DateUtils.getTodayDate

class AsteroidRepository(val asteroidsDao: AsteroidsDao) {
    //remote
    fun getAsteroidsFromRetrofit()= RetrofitInstance.api.getAsteroids(start_date = getTodayDate(),Constants.API_KEY)
    fun getImageOfTheDay()= RetrofitInstance.api.getImageOfTheDay(start_date = getTodayDate(), api_key = Constants.API_KEY)

    //local
    suspend fun getAsteroidsFromDB()= asteroidsDao.getAllAsteroids()
    suspend fun insertAsteroids(asteroids:List<Asteroid>)=asteroidsDao.insertAsteroidsList(asteroids)
    suspend fun clearAsteroids()=asteroidsDao.clearAllAsteroids()
}