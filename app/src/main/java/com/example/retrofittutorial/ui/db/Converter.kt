package com.example.retrofittutorial.ui.db

import androidx.room.TypeConverter
import com.example.retrofittutorial.ui.model.Source

class Converter {
    @TypeConverter
    fun fromSource(source: Source): String? {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}