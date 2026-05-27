package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.database.ExperimentResultDao
import com.example.data.local.database.YantraDatabase
import com.example.data.local.datastore.UserProfileDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideYantraDatabase(@ApplicationContext context: Context): YantraDatabase {
        return Room.databaseBuilder(
            context,
            YantraDatabase::class.java,
            "yantra.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideExperimentResultDao(database: YantraDatabase): ExperimentResultDao {
        return database.experimentResultDao()
    }

    @Provides
    @Singleton
    fun provideUserProfileDataStore(@ApplicationContext context: Context): UserProfileDataStore {
        return UserProfileDataStore(context)
    }
}
