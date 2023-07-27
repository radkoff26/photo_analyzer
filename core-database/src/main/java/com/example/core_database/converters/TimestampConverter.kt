package com.example.core_database.converters

import androidx.room.TypeConverter
import java.sql.Timestamp

class TimestampConverter {

    @TypeConverter
    fun timestampToLong(timestamp: Timestamp): Long {
        return timestamp.time
    }

    @TypeConverter
    fun longToTimestamp(millis: Long): Timestamp {
        return Timestamp(millis)
    }
}