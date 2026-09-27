package com.example.estantepessoaldelivros.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.estantepessoaldelivros.model.StatusLeitura

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    livroId: Long?,
    aoConcluir: () -> Unit,
    viewModel: FormularioViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = FormularioViewModelFactory(
            application = androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application,
            livroId = livroId
        )
    )
) {
    val estado by viewModel.estado

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (estado.modoEdicao) "Editar livro" else "Novo livro") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = estado.titulo,
                onValueChange = viewModel::aoAlterarTitulo,
                label = { Text("Título") },
                isError = estado.erroTitulo != null,
                supportingText = { estado.erroTitulo?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = estado.autor,
                onValueChange = viewModel::aoAlterarAutor,
                label = { Text("Autor") },
                isError = estado.erroAutor != null,
                supportingText = { estado.erroAutor?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = estado.totalPaginas,
                onValueChange = viewModel::aoAlterarTotalPaginas,
                label = { Text("Total de páginas") },
                isError = estado.erroTotalPaginas != null,
                supportingText = { estado.erroTotalPaginas?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = estado.paginasLidas,
                onValueChange = viewModel::aoAlterarPaginasLidas,
                label = { Text("Páginas lidas") },
                isError = estado.erroPaginasLidas != null,
                supportingText = { estado.erroPaginasLidas?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Situação", style = MaterialTheme.typography.labelLarge)
            Row {
                StatusLeitura.entries.forEach { status ->
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        RadioButton(
                            selected = estado.status == status,
                            onClick = { viewModel.aoAlterarStatus(status) }
                        )
                        Text(status.name)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.salvar(aoConcluir = aoConcluir) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar")
            }

            if (estado.modoEdicao) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { viewModel.excluir(aoConcluir = aoConcluir) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Excluir")
                }
            }
        }
    }
}