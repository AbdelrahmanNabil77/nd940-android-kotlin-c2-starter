package com.udacity.asteroidradar.datalayer.remote

import com.udacity.asteroidradar.model.ImageOfTheDay
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") start_date: String,
        @Query("api_key") api_key: String,
        @Query("end_date") end_date: String?=null
    ): String

    @GET("planetary/apod")
    suspend fun getImageOfTheDay(@Query("api_key") api_key: String): ImageOfTheDay
}