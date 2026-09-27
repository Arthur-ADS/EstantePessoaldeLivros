package com.example.estantepessoaldelivros.preferencias

import androidx.datastore.preferences.preferencesDataStore

val android.content.Context.estanteDataStore by preferencesDataStore(
    name = "estante_preferencias"
)