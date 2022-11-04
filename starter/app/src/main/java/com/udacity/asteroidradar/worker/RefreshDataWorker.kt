package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.datalayer.local.AsteroidsDatabase
import com.udacity.asteroidradar.datalayer.repository.AsteroidRepository

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
    override suspend fun doWork(): Result {
        val asteroidsDao=AsteroidsDatabase.getInstance(applicationContext).asteroidsDao
        val repository=AsteroidRepository(asteroidsDao)
        return try {
            repository.getTodayAsteroids()
            Result.success()
        }catch (e:Exception){
            Result.retry()
        }
    }

}