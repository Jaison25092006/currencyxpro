package com.example.currencyxpro.di

import android.content.Context
import androidx.room.Room
import com.example.currencyxpro.data.local.AppDatabase
import com.example.currencyxpro.data.local.dao.ExchangeRateDao
import com.example.currencyxpro.data.local.dao.FavoriteDao
import com.example.currencyxpro.data.local.dao.FavoritePairDao
import com.example.currencyxpro.data.local.dao.HistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "currencyx_database"
        ).build()
    }

    @Provides
    fun provideFavoriteDao(database: AppDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    fun provideFavoritePairDao(database: AppDatabase): FavoritePairDao {
        return database.favoritePairDao()
    }

    @Provides
    fun provideHistoryDao(database: AppDatabase): HistoryDao {
        return database.historyDao()
    }

    @Provides
    fun provideExchangeRateDao(database: AppDatabase): ExchangeRateDao {
        return database.exchangeRateDao()
    }
}
