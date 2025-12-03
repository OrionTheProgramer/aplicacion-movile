package am.gold.Screens

import am.gold.Navigation.AppScreens
import am.gold.ViewModel.AuthViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import am.gold.ui.components.GoldenRoseScreen
import am.gold.ui.components.GoldenSurfaceCard
import am.gold.ui.components.PrimaryButton
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val uiState by authViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.error) {
        uiState.error?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
                authViewModel.clearError()
            }
        }
    }

    GoldenRoseScreen(
        title = "Crear cuenta",
        subtitle = "Conecta con el ecosistema Golden Rose",
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        GoldenSurfaceCard(
            title = "Registrarse",
            supportingText = "Los datos se envian al microservicio de autenticacion/usuarios."
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nombre de usuario") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Filled.Badge, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electronico") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contrasena") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar contrasena") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
                PrimaryButton(
                    text = if (uiState.isLoading) "Registrando..." else "Crear cuenta",
                    modifier = Modifier.fillMaxWidth(),
                    enabled = username.isNotBlank() && email.isNotBlank() &&
                        password.isNotBlank() && password == confirmPassword && !uiState.isLoading
                ) {
                    authViewModel.register(username.trim(), email.trim(), password.trim())
                }
                TextButton(
                    onClick = { navController.navigate(AppScreens.LoginScreen.route) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Ya tengo cuenta")
                }
            }
        }
    }
}
