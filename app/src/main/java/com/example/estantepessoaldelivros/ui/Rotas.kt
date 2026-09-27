package com.example.estantepessoaldelivros.ui

import kotlinx.serialization.Serializable

@Serializable
object RotaEstante

@Serializable
data class RotaFormulario (val livroId : Long? = null)