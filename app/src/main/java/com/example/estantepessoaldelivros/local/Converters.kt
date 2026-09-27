package com.example.estantepessoaldelivros.local

import androidx.room.TypeConverter
import com.example.estantepessoaldelivros.model.StatusLeitura

class Converters {
    @TypeConverter
    fun fromStatusLeitura(status : StatusLeitura) : String = status.name

    @TypeConverter
    fun toStatusLeitura(valor : String) : StatusLeitura = StatusLeitura.valueOf(valor)
}