package com.example.estantepessoaldelivros.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.estantepessoaldelivros.model.Livro

@Database(entities = [Livro::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun livroDao() : LivroDao

    companion object {
        @Volatile
        private var INSTANCIA: AppDatabase? = null

        fun obterInstancia(context: Context): AppDatabase {
            return INSTANCIA ?: synchronized(this) {
                INSTANCIA ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "estantePessoalDeLivros.db"
                ).build().also { INSTANCIA = it }
            }
        }
    }
}