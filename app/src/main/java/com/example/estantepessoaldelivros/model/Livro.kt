package com.example.estantepessoaldelivros.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "livros")
data class Livro(
    @PrimaryKey(autoGenerate = true) val id : Long = 0L,
    val titulo : String,
    val autor : String,
    val totalPaginas : Int,
    val paginasLidas : Int,
    val status : StatusLeitura = StatusLeitura.QUERO_LER,
    @ColumnInfo(name = "cadastrado_em")
    val cadastradoEm : Long = System.currentTimeMillis()
)
