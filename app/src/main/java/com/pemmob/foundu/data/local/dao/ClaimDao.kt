package com.pemmob.foundu.data.local.dao

import androidx.room.*
import com.pemmob.foundu.data.local.entity.Claim
import kotlinx.coroutines.flow.Flow

@Dao
interface ClaimDao {
    @Query("SELECT * FROM claims WHERE itemId = :itemId ORDER BY claimedAt DESC")
    fun getForItem(itemId: Long): Flow<List<Claim>>

    @Query("SELECT * FROM claims ORDER BY claimedAt DESC")
    fun getAll(): Flow<List<Claim>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(claim: Claim): Long

    @Update
    suspend fun update(claim: Claim): Int

    @Delete
    suspend fun delete(claim: Claim): Int
}
