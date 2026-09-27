package com.example.estantepessoaldelivros

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.estantepessoaldelivros.ui.EstanteScreen
import com.example.estantepessoaldelivros.ui.FormularioScreen
import com.example.estantepessoaldelivros.ui.RotaEstante
import com.example.estantepessoaldelivros.ui.RotaFormulario

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = RotaEstante) {
                        composable<RotaEstante> {
                            EstanteScreen(
                                aoClicarEmLivro = { id ->
                                    navController.navigate(RotaFormulario(livroId = id))
                                },
                                aoClicarEmNovo = {
                                    navController.navigate(RotaFormulario())
                                }
                            )
                        }
                        composable<RotaFormulario> { backStackEntry ->
                            val rota: RotaFormulario = backStackEntry.toRoute()
                            FormularioScreen(
                                livroId = rota.livroId,
                                aoConcluir = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}