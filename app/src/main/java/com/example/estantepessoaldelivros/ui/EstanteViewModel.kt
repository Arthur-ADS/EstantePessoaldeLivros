package com.example.estantepessoaldelivros.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.estantepessoaldelivros.data.EstanteRepository
import com.example.estantepessoaldelivros.model.FiltroStatus
import com.example.estantepessoaldelivros.model.Livro
import com.example.estantepessoaldelivros.model.Ordenacao
import com.example.estantepessoaldelivros.preferencias.PreferenciasEstanteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.estantepessoaldelivros.local.AppDatabase

class EstanteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EstanteRepository(
        livroDao = AppDatabase.obterInstancia(application).livroDao(),
        preferenciasRepository = PreferenciasEstanteRepository(application)
    )

    private val _filtro = MutableStateFlow(FiltroStatus.TODOS)

    val estado: StateFlow<EstanteUiState> = combine(
        repository.observarLivros(),
        _filtro,
        repository.ordenacao
    ) { livros, filtro, ordenacao ->
        val livrosFiltrados = when (filtro) {
            FiltroStatus.TODOS -> livros
            else -> livros.filter { it.status.name == filtro.name }
        }
        EstanteUiState(
            livros = livrosFiltrados,
            filtro = filtro,
            ordenacao = ordenacao,
            carregando = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EstanteUiState()
    )

    fun aoAlterarFiltro(filtro: FiltroStatus) {
        _filtro.value = filtro
    }

    fun aoAlterarOrdenacao(ordenacao: Ordenacao) {
        viewModelScope.launch {
            repository.definirOrdenacao(ordenacao)
        }
    }

    fun aoExcluir(livro: Livro) {
        viewModelScope.launch {
            repository.excluir(livro)
        }
    }
}