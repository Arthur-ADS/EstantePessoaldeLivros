package com.example.estantepessoaldelivros.ui

import com.example.estantepessoaldelivros.model.StatusLeitura

data class FormularioUiState(
    val id: Long? = null,
    val titulo: String = "",
    val autor: String = "",
    val totalPaginas: String = "",
    val paginasLidas: String = "",
    val status: StatusLeitura = StatusLeitura.QUERO_LER,
    val erroTitulo: String? = null,
    val erroAutor: String? = null,
    val erroTotalPaginas: String? = null,
    val erroPaginasLidas: String? = null,
    val carregando: Boolean = true,
    val salvandoOuConcluido: Boolean = false
) {
    val modoEdicao: Boolean get() = id != null
}