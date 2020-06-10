package com.сode5150.task3_database.converters

import androidx.room.TypeConverter
import java.util.*

class DateTypeConverter {
    @TypeConverter fun fromTimestamp(value: Long?): Date? = if (value == null) null else Date(value)
    @TypeConverter fun dateToTimestamp(date: Date?): Long? = date?.time
}