package am.gold.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import am.gold.ViewModel.AuthViewModel
import am.gold.Navigation.AppScreens // Para volver a Login

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirmar Contraseña") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // TODO: Validar campos y que contraseñas coincidan
            // Llama a tu lógica de registro (podrías moverla al ViewModel)
            // Si es exitoso:
            authViewModel.login("client", "fake_token") // Simula login post-registro
            // La navegación ocurre automáticamente
        }) {
            Text("Registrar")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { navController.navigate(AppScreens.LoginScreen.route) }) {
            Text("¿Ya tienes cuenta? Inicia Sesión")
        }
    }
}