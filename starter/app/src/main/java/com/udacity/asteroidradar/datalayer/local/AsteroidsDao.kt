package com.udacity.asteroidradar.datalayer.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.model.Asteroid

@Dao
interface AsteroidsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroidsList(asteroidsList: List<Asteroid>)

    @Query("DELETE FROM asteroids_table")
    suspend fun clearAllAsteroids():Int

    @Query("select * from asteroids_table ORDER BY closeApproachDate ASC")
    fun getAllAsteroids():LiveData<List<Asteroid>>
}