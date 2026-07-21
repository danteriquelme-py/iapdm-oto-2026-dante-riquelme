package com.example.registroempleadosdanteriquelme.data

import android.net.Uri
import androidx.annotation.DrawableRes

data class Empleado(
    val id: Long = System.nanoTime(),
    val nombreCompleto: String,
    val cargo: String,
    val departamento: String,
    val salario: String,
    val fechaContratacion: String,
    val imagenUri: Uri? = null,
    @DrawableRes val placeholderRes: Int = 0
) {
    val fotoModel: Any get() = imagenUri ?: placeholderRes
}
