package com.example.estantepessoaldelivros.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.estantepessoaldelivros.data.EstanteRepository
import com.example.estantepessoaldelivros.local.AppDatabase
import com.example.estantepessoaldelivros.model.Livro
import com.example.estantepessoaldelivros.model.StatusLeitura
import com.example.estantepessoaldelivros.preferencias.PreferenciasEstanteRepository
import kotlinx.coroutines.launch

class FormularioViewModel(
    application: Application,
    private val livroId: Long?
) : AndroidViewModel(application) {

    private val repository = EstanteRepository(
        livroDao = AppDatabase.obterInstancia(application).livroDao(),
        preferenciasRepository = PreferenciasEstanteRepository(application)
    )

    var estado = androidx.compose.runtime.mutableStateOf(FormularioUiState(id = livroId))
        private set

    init {
        if (livroId != null) {
            viewModelScope.launch {
                val livro = repository.obterPorId(livroId)
                if (livro != null) {
                    estado.value = estado.value.copy(
                        id = livro.id,
                        titulo = livro.titulo,
                        autor = livro.autor,
                        totalPaginas = livro.totalPaginas.toString(),
                        paginasLidas = livro.paginasLidas.toString(),
                        status = livro.status,
                        carregando = false
                    )
                }
            }
        } else {
            estado.value = estado.value.copy(carregando = false)
        }
    }

    fun aoAlterarTitulo(valor: String) {
        estado.value = estado.value.copy(titulo = valor, erroTitulo = null)
    }

    fun aoAlterarAutor(valor: String) {
        estado.value = estado.value.copy(autor = valor, erroAutor = null)
    }

    fun aoAlterarTotalPaginas(valor: String) {
        estado.value = estado.value.copy(totalPaginas = valor, erroTotalPaginas = null)
    }

    fun aoAlterarPaginasLidas(valor: String) {
        estado.value = estado.value.copy(paginasLidas = valor, erroPaginasLidas = null)
    }

    fun aoAlterarStatus(valor: StatusLeitura) {
        val novoEstado = if (valor == StatusLeitura.LIDO) {
            estado.value.copy(status = valor, paginasLidas = estado.value.totalPaginas)
        } else {
            estado.value.copy(status = valor)
        }
        estado.value = novoEstado
    }

    private fun validar(): Boolean {
        val atual = estado.value
        var valido = true
        var erroTitulo: String? = null
        var erroAutor: String? = null
        var erroTotalPaginas: String? = null
        var erroPaginasLidas: String? = null

        if (atual.titulo.isBlank()) {
            erroTitulo = "Título não pode ser vazio"; valido = false
        }
        if (atual.autor.isBlank()) {
            erroAutor = "Autor não pode ser vazio"; valido = false
        }

        val total = atual.totalPaginas.toIntOrNull()
        if (total == null || total <= 0) {
            erroTotalPaginas = "Informe um número inteiro maior que zero"; valido = false
        }

        val lidas = atual.paginasLidas.toIntOrNull()
        if (lidas == null || lidas < 0) {
            erroPaginasLidas = "Informe um número inteiro maior ou igual a zero"; valido = false
        } else if (total != null && lidas > total) {
            erroPaginasLidas = "Não pode ser maior que o total de páginas"; valido = false
        }

        estado.value = atual.copy(
            erroTitulo = erroTitulo,
            erroAutor = erroAutor,
            erroTotalPaginas = erroTotalPaginas,
            erroPaginasLidas = erroPaginasLidas
        )
        return valido
    }

    fun salvar(aoConcluir: () -> Unit) {
        if (!validar()) return

        viewModelScope.launch {
            val atual = estado.value
            val totalPaginas = atual.totalPaginas.toInt()
            var paginasLidas = atual.paginasLidas.toInt()
            var status = atual.status

            if (paginasLidas == totalPaginas) {
                status = StatusLeitura.LIDO
            }

            val livro = Livro(
                id = atual.id ?: 0L,
                titulo = atual.titulo.trim(),
                autor = atual.autor.trim(),
                totalPaginas = totalPaginas,
                paginasLidas = paginasLidas,
                status = status
            )

            if (atual.modoEdicao) {
                repository.atualizar(livro)
            } else {
                repository.inserir(livro)
            }

            aoConcluir()
        }
    }

    fun excluir(aoConcluir: () -> Unit) {
        val id = estado.value.id ?: return
        viewModelScope.launch {
            val livro = repository.obterPorId(id)
            if (livro != null) {
                repository.excluir(livro)
            }
            aoConcluir()
        }
    }
}

class FormularioViewModelFactory(
    private val application: Application,
    private val livroId: Long?
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FormularioViewModel(application, livroId) as T
    }
}