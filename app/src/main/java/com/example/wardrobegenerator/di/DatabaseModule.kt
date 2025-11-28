package com.example.wardrobegenerator.di

import android.content.Context
import androidx.room.Room
import com.example.wardrobegenerator.data.local.WardrobeDatabase
import com.example.wardrobegenerator.data.local.dao.ClothingItemDao
import com.example.wardrobegenerator.data.local.dao.OutfitDao
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
    fun provideWardrobeDatabase(@ApplicationContext context: Context): WardrobeDatabase {
        return Room.databaseBuilder(
            context,
            WardrobeDatabase::class.java,
            WardrobeDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideClothingItemDao(database: WardrobeDatabase): ClothingItemDao {
        return database.clothingItemDao()
    }

    @Provides
    @Singleton
    fun provideOutfitDao(database: WardrobeDatabase): OutfitDao {
        return database.outfitDao()
    }
}
