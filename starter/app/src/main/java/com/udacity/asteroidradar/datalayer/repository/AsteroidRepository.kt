package com.udacity.asteroidradar.datalayer.repository

import android.util.Log
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.datalayer.local.AsteroidsDao
import com.udacity.asteroidradar.datalayer.remote.RetrofitInstance
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.utils.DateUtils.getTodayDate
import org.json.JSONObject

class AsteroidRepository(val asteroidsDao: AsteroidsDao) {
    //remote
    suspend fun getAsteroidsFromRetrofit(end_date: String? = null) =
        RetrofitInstance.api.getAsteroids(
            start_date = getTodayDate(), api_key = Constants.API_KEY, end_date = end_date)

    suspend fun getImageOfTheDay() =
        RetrofitInstance.api.getImageOfTheDay(api_key = Constants.API_KEY)

    //local
    suspend fun savedAsteroidsList() = asteroidsDao.getSavedAsteroids(getTodayDate())
    suspend fun insertAsteroids(asteroids: List<Asteroid>) =
        asteroidsDao.insertAsteroidsList(asteroids)

    suspend fun clearOldAsteroids() = asteroidsDao.clearOldAsteroids(getTodayDate())
    suspend fun todayAsteroidsList() = asteroidsDao.getTodayAsteroids(getTodayDate())

    suspend fun getTodayAsteroids() {
        try {
            val asteroidsList = getAsteroidsList(getAsteroidsFromRetrofit(end_date = getTodayDate()))
            clearOldAsteroids()
            insertAsteroids(asteroidsList)
        } catch (e: Exception) {
            Log.d("Error", "error message: ${e.message}")
        }
    }

    suspend fun getWeekAsteroids() {
        try {
            val asteroidsList = getAsteroidsList(getAsteroidsFromRetrofit())
            clearOldAsteroids()
            insertAsteroids(asteroidsList)
        } catch (e: Exception) {
            Log.d("Error", "error message: ${e.message}")
        }
    }

    private fun getAsteroidsList(response: String): List<Asteroid> {
        val jsonObject = JSONObject(response)
        return parseAsteroidsJsonResult(jsonObject)
    }
}