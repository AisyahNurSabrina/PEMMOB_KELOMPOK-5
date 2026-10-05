package com.pemmob.foundu.data.local.dao

import androidx.room.*
import com.pemmob.foundu.data.local.entity.PickupPoint
import kotlinx.coroutines.flow.Flow

@Dao
interface PickupPointDao {
    @Query("SELECT * FROM pickup_points ORDER BY name ASC")
    fun getAll(): Flow<List<PickupPoint>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pickupPoint: PickupPoint): Long

    @Update
    suspend fun update(pickupPoint: PickupPoint): Int

    @Delete
    suspend fun delete(pickupPoint: PickupPoint): Int
}
