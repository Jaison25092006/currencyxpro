package com.example.currencyxpro.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyxpro.data.local.entity.FavoritePairEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePairDao {
    @Query("SELECT * FROM favorite_pairs")
    fun getAllFavoritePairsFlow(): Flow<List<FavoritePairEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritePair(pair: FavoritePairEntity)

    @Delete
    suspend fun deleteFavoritePair(pair: FavoritePairEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_pairs WHERE baseCurrency = :base AND targetCurrency = :target LIMIT 1)")
    fun isFavoritePairFlow(base: String, target: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_pairs WHERE baseCurrency = :base AND targetCurrency = :target LIMIT 1)")
    suspend fun isFavoritePair(base: String, target: String): Boolean
}
