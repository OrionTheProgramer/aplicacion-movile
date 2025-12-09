package am.gold.Screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import am.gold.ViewModel.SettingsViewModel
import am.gold.ViewModel.SettingsViewModelFactory
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(application))

    val username by settingsViewModel.username.collectAsState()
    val email by settingsViewModel.email.collectAsState()
    val bio by settingsViewModel.bio.collectAsState()
    val photoUri by settingsViewModel.photoUri.collectAsState()

    var nameState = remember { mutableStateOf(TextFieldValue(username)) }
    var emailState = remember { mutableStateOf(TextFieldValue(email)) }
    var bioState = remember { mutableStateOf(TextFieldValue(bio)) }
    var photoState = remember { mutableStateOf(TextFieldValue(photoUri ?: "")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                label = { Text("Nombre de usuario") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = bioState.value,
                onValueChange = { bioState.value = it },
                label = { Text("Descripcion") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            OutlinedTextField(
                value = photoState.value,
                onValueChange = { photoState.value = it },
                label = { Text("URL de foto de perfil") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    settingsViewModel.updateProfile(
                        nameState.value.text,
                        emailState.value.text,
                        bioState.value.text,
                        photoState.value.text.ifBlank { null }
                    )
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }
        }
    }
}
