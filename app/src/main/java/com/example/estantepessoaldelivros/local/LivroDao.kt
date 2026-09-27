package com.example.estantepessoaldelivros.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.estantepessoaldelivros.model.Livro
import com.example.estantepessoaldelivros.model.StatusLeitura
import kotlinx.coroutines.flow.Flow

@Dao
interface LivroDao {
    @Query("SELECT * FROM livros ORDER by titulo ASC")
    fun observarTodos() : Flow<List<Livro>>

    @Query("SELECT * FROM livros WHERE status = :status ORDER by titulo ASC")
    fun observarPorStatus(status : StatusLeitura) : Flow<List<Livro>>

    @Query("SELECT * FROM livros WHERE id = :id")
    suspend fun obterPorId(id : Long) : Livro?

    @Insert
    suspend fun inserir(livro : Livro) : Long

    @Update
    suspend fun atualizar(livro : Livro)

    @Delete
    suspend fun excluir(livro : Livro)
}