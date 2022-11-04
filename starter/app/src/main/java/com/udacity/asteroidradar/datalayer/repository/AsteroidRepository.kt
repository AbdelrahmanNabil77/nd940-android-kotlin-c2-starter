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
    suspend fun getAsteroidsFromRetrofit()= RetrofitInstance.api.getAsteroids(start_date = getTodayDate(),Constants.API_KEY)
    suspend fun getImageOfTheDay()= RetrofitInstance.api.getImageOfTheDay(api_key = Constants.API_KEY)

    //local
    val asteroidsList= asteroidsDao.getAllAsteroids()
    suspend fun insertAsteroids(asteroids:List<Asteroid>)=asteroidsDao.insertAsteroidsList(asteroids)
    suspend fun clearAsteroids()=asteroidsDao.clearAllAsteroids()

    suspend fun getAsteroids(){
        try {
            val asteroidsList=getAsteroidsList(getAsteroidsFromRetrofit())
            clearAsteroids()
            insertAsteroids(asteroidsList)
        }catch (e:Exception){
            Log.d("Error","error message: ${e.message}")
        }
    }
    private fun getAsteroidsList(response: String): List<Asteroid> {
        val jsonObject = JSONObject(response)
        return parseAsteroidsJsonResult(jsonObject)
    }
}