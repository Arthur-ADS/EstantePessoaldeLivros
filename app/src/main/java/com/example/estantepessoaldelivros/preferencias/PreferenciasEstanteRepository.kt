package com.example.estantepessoaldelivros.preferencias

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.estantepessoaldelivros.model.Ordenacao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenciasEstanteRepository (private val context : Context) {
    private val CHAVE_ORDENACAO = stringPreferencesKey("ordenacao")

    val ordenacao : Flow<Ordenacao> = context.estanteDataStore.data
        .map { preferencias ->
            val valorSalvo = preferencias[CHAVE_ORDENACAO]
            valorSalvo?.let { Ordenacao.valueOf(it) } ?: Ordenacao.TITULO
        }

    suspend fun definirOrdenacao(ordenacao : Ordenacao){
        context.estanteDataStore.edit { preferencias ->
            preferencias[CHAVE_ORDENACAO] = ordenacao.name
        }
    }
}