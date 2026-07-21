package com.example.registroempleadosdanteriquelme.data

import android.net.Uri
import androidx.annotation.DrawableRes

/**
 * Modelo de datos de un empleado.
 *
 * - [imagenUri]: foto que el usuario subió desde la galería (puede ser null).
 * - [placeholderRes]: imagen de respaldo que se muestra cuando no se sube foto.
 */
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
    /** Devuelve lo que hay que mostrar: la foto subida o, si no hay, el placeholder. */
    val fotoModel: Any get() = imagenUri ?: placeholderRes
}
