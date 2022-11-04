package com.udacity.asteroidradar.datalayer.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.model.Asteroid

@Dao
interface AsteroidsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroidsList(asteroidsList: List<Asteroid>)

    @Query("DELETE FROM asteroids_table where closeApproachDate<:date")
    suspend fun clearOldAsteroids(date:String):Int

    @Query("select * from asteroids_table where closeApproachDate>=:date ORDER BY closeApproachDate ASC")
    suspend fun getSavedAsteroids(date:String):List<Asteroid>

    @Query("select * from asteroids_table where closeApproachDate=:date ORDER BY closeApproachDate ASC")
    suspend fun getTodayAsteroids(date:String):List<Asteroid>
}