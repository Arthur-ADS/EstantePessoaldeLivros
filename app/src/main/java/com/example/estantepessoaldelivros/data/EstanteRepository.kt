package com.example.estantepessoaldelivros.data

import com.example.estantepessoaldelivros.local.LivroDao
import com.example.estantepessoaldelivros.model.Livro
import com.example.estantepessoaldelivros.model.Ordenacao
import com.example.estantepessoaldelivros.preferencias.PreferenciasEstanteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class EstanteRepository(
    private val livroDao: LivroDao,
    private val preferenciasRepository: PreferenciasEstanteRepository
) {
    val ordenacao: Flow<Ordenacao> = preferenciasRepository.ordenacao

    fun observarLivros(): Flow<List<Livro>> =
        combine(livroDao.observarTodos(), ordenacao) { livros, ordenacao ->
            when (ordenacao) {
                Ordenacao.TITULO -> livros.sortedBy { it.titulo.lowercase() }
                Ordenacao.MAIS_RECENTES -> livros.sortedByDescending { it.cadastradoEm }
            }
        }

    suspend fun definirOrdenacao(ordenacao: Ordenacao) =
        preferenciasRepository.definirOrdenacao(ordenacao)

    suspend fun inserir(livro: Livro) = livroDao.inserir(livro)
    suspend fun atualizar(livro: Livro) = livroDao.atualizar(livro)
    suspend fun excluir(livro: Livro) = livroDao.excluir(livro)
    suspend fun obterPorId(id: Long) = livroDao.obterPorId(id)
}