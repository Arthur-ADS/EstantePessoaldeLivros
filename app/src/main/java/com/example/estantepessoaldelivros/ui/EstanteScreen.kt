package com.example.estantepessoaldelivros.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.estantepessoaldelivros.model.FiltroStatus
import com.example.estantepessoaldelivros.model.Livro
import com.example.estantepessoaldelivros.model.Ordenacao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstanteScreen(
    aoClicarEmLivro: (Long) -> Unit,
    aoClicarEmNovo: () -> Unit,
    viewModel: EstanteViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estante de Livros") },
                actions = {
                    IconButton(onClick = {
                        val proxima = if (estado.ordenacao == Ordenacao.TITULO)
                            Ordenacao.MAIS_RECENTES else Ordenacao.TITULO
                        viewModel.aoAlterarOrdenacao(proxima)
                    }) {
                        Text(if (estado.ordenacao == Ordenacao.TITULO) "A-Z" else "🕒")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = aoClicarEmNovo) {
                Icon(Icons.Default.Add, contentDescription = "Cadastrar livro")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            // RF07 — resumo
            val totalLidos = estado.livros.count { it.status.name == "LIDO" }
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Total: ${estado.livros.size}")
                Text("Lidos: $totalLidos")
            }

            // RF05 — filtros
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FiltroStatus.entries.forEach { filtro ->
                    FilterChip(
                        selected = estado.filtro == filtro,
                        onClick = { viewModel.aoAlterarFiltro(filtro) },
                        label = { Text(filtro.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // RF08 — estados vazios
            when {
                estado.livros.isEmpty() && estado.filtro == FiltroStatus.TODOS -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Nenhum livro cadastrado.")
                    }
                }
                estado.livros.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Nenhum livro para o filtro aplicado.")
                    }
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(estado.livros, key = { it.id }) { livro ->
                            CardLivro(livro = livro, onClick = { aoClicarEmLivro(livro.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CardLivro(livro: Livro, onClick: () -> Unit) {
    val progresso = if (livro.totalPaginas > 0)
        (livro.paginasLidas * 100) / livro.totalPaginas else 0

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(livro.titulo, style = MaterialTheme.typography.titleMedium)
            Text(livro.autor, style = MaterialTheme.typography.bodyMedium)
            Text("${livro.status.name} — $progresso%", style = MaterialTheme.typography.bodySmall)
        }
    }
}