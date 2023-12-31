package com.example.base.modules

import android.content.Context
import androidx.room.Room
import com.example.core_database.ApplicationDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): ApplicationDatabase =
        Room.databaseBuilder(
            context,
            ApplicationDatabase::class.java,
            ApplicationDatabase.DATABASE_NAME
        ).build()
}