package com.example.currencyxpro.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyxpro.data.local.entity.ConversionHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM conversion_history ORDER BY timestamp DESC")
    fun getAllHistoryFlow(): Flow<List<ConversionHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: ConversionHistoryEntity)

    @Delete
    suspend fun deleteHistory(history: ConversionHistoryEntity)

    @Query("DELETE FROM conversion_history")
    suspend fun clearAllHistory()
}
