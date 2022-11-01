package com.udacity.asteroidradar.datalayer.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("neo/rest/v1/feed")
    fun getAsteroids(@Query("start_date") start_date: String,
                     @Query("api_key") api_key: String): Call<String>
}