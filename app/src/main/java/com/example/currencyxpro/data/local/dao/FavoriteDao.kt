package com.example.currencyxpro.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyxpro.data.local.entity.FavoriteCurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_currencies")
    fun getAllFavoritesFlow(): Flow<List<FavoriteCurrencyEntity>>

    @Query("SELECT * FROM favorite_currencies")
    suspend fun getAllFavorites(): List<FavoriteCurrencyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteCurrencyEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteCurrencyEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_currencies WHERE code = :code LIMIT 1)")
    fun isFavoriteFlow(code: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_currencies WHERE code = :code LIMIT 1)")
    suspend fun isFavorite(code: String): Boolean
}
