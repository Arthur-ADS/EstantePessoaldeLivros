package com.example.estantepessoaldelivros.ui

import com.example.estantepessoaldelivros.model.FiltroStatus
import com.example.estantepessoaldelivros.model.Livro
import com.example.estantepessoaldelivros.model.Ordenacao

data class EstanteUiState(
    val livros: List<Livro> = emptyList(),
    val filtro: FiltroStatus = FiltroStatus.TODOS,
    val ordenacao: Ordenacao = Ordenacao.TITULO,
    val carregando: Boolean = true
)
