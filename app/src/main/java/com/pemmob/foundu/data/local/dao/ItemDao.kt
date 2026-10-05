package com.pemmob.foundu.data.local.dao

import androidx.room.*
import com.pemmob.foundu.data.local.entity.Item
import com.pemmob.foundu.data.local.entity.ItemStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM items ORDER BY reportedAt DESC")
    fun getAll(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE status = :status ORDER BY reportedAt DESC")
    fun getByStatus(status: ItemStatus): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE id = :id")
    fun getById(id: Long): Flow<Item?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item): Long

    @Update
    suspend fun update(item: Item): Int

    @Delete
    suspend fun delete(item: Item): Int
}
