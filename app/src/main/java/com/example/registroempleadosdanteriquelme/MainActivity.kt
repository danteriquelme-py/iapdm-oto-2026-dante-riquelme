package com.example.registroempleadosdanteriquelme

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.registroempleadosdanteriquelme.data.Empleado
import com.example.registroempleadosdanteriquelme.ui.theme.AppTheme

val fotosPerros = listOf(
    R.drawable.pet1,
    R.drawable.pet3
)

val fotosGatos = listOf(
    R.drawable.pet2,
    R.drawable.pet4
)

val imagenesAyuda = listOf(
    R.drawable.ayuda1,
    R.drawable.ayuda2,
    R.drawable.ayuda3,
    R.drawable.ayuda4,
    R.drawable.ayuda5
)

fun placeholderPara(indice: Int): Int =
    if (indice % 2 == 0) fotosGatos.random() else fotosPerros.random()

enum class Pantalla { Lista, About, Ayuda }

class MainActivity : ComponentActivity() {

    private val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(tag, "onCreate")
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EmpleadosApp()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(tag, "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.i(tag, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(tag, "onDestroy")
    }
}

@Composable
fun EmpleadosApp() {
    val empleados = remember { mutableStateListOf<Empleado>() }
    var pantalla by remember { mutableStateOf(Pantalla.Lista) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var contadorAltas by remember { mutableStateOf(0) }

    when (pantalla) {
        Pantalla.Lista -> ListaEmpleadosScreen(
            empleados = empleados,
            onAgregarClick = { mostrarDialogo = true },
            onEliminar = { empleados.remove(it) },
            onAbrirAbout = { pantalla = Pantalla.About },
            onAbrirAyuda = { pantalla = Pantalla.Ayuda }
        )

        Pantalla.About -> AboutScreen(onVolver = { pantalla = Pantalla.Lista })
        Pantalla.Ayuda -> AyudaScreen(onVolver = { pantalla = Pantalla.Lista })
    }

    if (mostrarDialogo) {
        AgregarEmpleadoDialog(
            onDismiss = { mostrarDialogo = false },
            onAgregar = { nuevo ->
                empleados.add(nuevo.copy(placeholderRes = placeholderPara(contadorAltas)))
                contadorAltas++
                mostrarDialogo = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaEmpleadosScreen(
    empleados: List<Empleado>,
    onAgregarClick: () -> Unit,
    onEliminar: (Empleado) -> Unit,
    onAbrirAbout: () -> Unit,
    onAbrirAyuda: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Empleados") },
                actions = {
                    IconButton(onClick = onAbrirAyuda) {
                        Icon(Icons.Default.HelpOutline, contentDescription = "Ayuda")
                    }
                    IconButton(onClick = onAbrirAbout) {
                        Icon(Icons.Default.Info, contentDescription = "Acerca de")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregarClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar empleado")
            }
        }
    ) { innerPadding ->
        if (empleados.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Todavía no hay empleados cargados.\nTocá el botón + para agregar el primero.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 12.dp)
            ) {
                items(empleados, key = { it.id }) { empleado ->
                    EmpleadoItem(empleado = empleado, onEliminar = { onEliminar(empleado) })
                }
            }
        }
    }
}

@Composable
fun EmpleadoItem(
    empleado: Empleado,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Foto al costado del nombre
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = empleado.fotoModel,
                    contentDescription = "Foto de ${empleado.nombreCompleto}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
                Spacer(Modifier.width(14.dp))
                Text(
                    text = empleado.nombreCompleto,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { DatoChip("Cargo", empleado.cargo) }
                item { DatoChip("Departamento", empleado.departamento) }
                item { DatoChip("Salario", empleado.salario) }
                item { DatoChip("Fecha de Contratación", empleado.fechaContratacion) }
            }

            HorizontalDivider()

            OutlinedButton(
                onClick = onEliminar,
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.Delete, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Eliminar")
            }
        }
    }
}

@Composable
fun DatoChip(etiqueta: String, valor: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Text(text = etiqueta, style = MaterialTheme.typography.labelSmall)
            Text(
                text = valor.ifBlank { "-" },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun AgregarEmpleadoDialog(
    onDismiss: () -> Unit,
    onAgregar: (Empleado) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var cargo by remember { mutableStateOf("") }
    var departamento by remember { mutableStateOf("") }
    var salario by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    // Selector de imagen de la galería. No necesita permisos: el sistema
    // devuelve una URI con acceso temporal de lectura.
    val selectorImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) imagenUri = uri
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(24.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 620.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Nuevo empleado",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                // Foto circular: se toca para subir una imagen
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable { selectorImagen.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (imagenUri != null) {
                        AsyncImage(
                            model = imagenUri,
                            contentDescription = "Foto seleccionada",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "Subir foto",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
                Text(
                    text = "Opcional. Si no se sube una foto, se asigna una al azar.",
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = nombre, onValueChange = { nombre = it },
                    label = { Text("Nombre completo") },
                    singleLine = true, modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cargo, onValueChange = { cargo = it },
                    label = { Text("Cargo") },
                    singleLine = true, modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = departamento, onValueChange = { departamento = it },
                    label = { Text("Departamento") },
                    singleLine = true, modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = salario, onValueChange = { salario = it },
                    label = { Text("Salario (en Gs.)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = fecha, onValueChange = { fecha = it },
                    label = { Text("Fecha de contratación") },
                    placeholder = { Text("DD/MM/AAAA") },
                    singleLine = true, modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (nombre.isNotBlank()) {
                                onAgregar(
                                    Empleado(
                                        nombreCompleto = nombre.trim(),
                                        cargo = cargo.trim(),
                                        departamento = departamento.trim(),
                                        salario = salario.trim(),
                                        fechaContratacion = fecha.trim(),
                                        imagenUri = imagenUri
                                    )
                                )
                            }
                        }
                    ) { Text("Guardar") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onVolver: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Acerca de") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Desarrollado por Dante Riquelme, para el Examen Final de Aplicación Android de Registro de Productos " +
                        "Introducción a la Programación para Dispositivos Móviles.\n\n" +
                        "©Julio 2026",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyudaScreen(onVolver: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ayuda") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        // TODO (Dante): reemplazá estos pasos por tu guía paso a paso.
        val pasos = listOf(
            "Para agregar un nuevo empleado, se debe utilizar el botón +, que se encuentra en la esquina inferior derecha.",
            "Luego, se deben completar los datos del formulario en la ventana emergente.",
            "Para agregar una foto debemos hacer click en el placeholder circular para subir una imagen (opcional).",
            "Por último, utilizamos \"Guardar\" para agregarlo a la lista.",
            "Si queremos eliminar un Empleado cargado, basta con utilizar el botón \"Eliminar\" de cada tarjeta para borrarlo."
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "¿Cómo usar la app?",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            pasos.forEachIndexed { indice, paso ->
                Text(
                    text = "${indice + 1}. $paso",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Image(
                    painter = painterResource(imagenesAyuda[indice]),
                    contentDescription = "Imagen del paso ${indice + 1}",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }
    }
}
