package am.gold.Screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import am.gold.Navigation.AppScreens
import am.gold.R
import am.gold.ViewModel.AuthViewModel
import am.gold.ViewModel.AuthViewModelFactory
import am.gold.ViewModel.SettingsViewModel
import am.gold.ViewModel.SettingsViewModelFactory
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {

    val context = LocalContext.current
    val application = context.applicationContext as Application
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(application))
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(application))

    val username by settingsViewModel.username.collectAsState()
    val email by settingsViewModel.email.collectAsState()
    val bio by settingsViewModel.bio.collectAsState()
    val photoUri by settingsViewModel.photoUri.collectAsState()
    val receiveOffers by settingsViewModel.receiveOffers.collectAsState()
    val currentTheme by settingsViewModel.appTheme.collectAsState()
    val pushNotificationsEnabled by settingsViewModel.pushNotificationsEnabled.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                AsyncImage(
                    model = photoUri ?: R.drawable.ic_profile_placeholder,
                    contentDescription = "Foto de Perfil",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(username.ifBlank { "Usuario" }, style = MaterialTheme.typography.headlineSmall)
                    if (email.isNotBlank()) {
                        Text(email, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            if (bio.isNotBlank()) {
                Text(bio, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 8.dp))
            }
            TextButton(
                onClick = { navController.navigate(AppScreens.EditProfileScreen.route) },
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
            ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Editar Perfil")
            }

            SettingItemDivider(title = "Preferencias")

            SettingSwitchItem(
                text = "Recibir ofertas por correo",
                icon = Icons.Default.Email,
                checked = receiveOffers,
                onCheckedChange = { settingsViewModel.setReceiveOffers(it) }
            )
            SettingSwitchItem(
                text = "Activar notificaciones push",
                icon = Icons.Default.Notifications,
                checked = pushNotificationsEnabled,
                onCheckedChange = { settingsViewModel.setPushNotificationsEnabled(it) }
            )

            SettingItemDivider(title = "Apariencia")
            Column(Modifier.padding(horizontal = 16.dp)) {
                ThemeOptionRow(
                    text = "Claro",
                    selected = currentTheme == "light",
                    onClick = { settingsViewModel.setAppTheme("light") }
                )
                ThemeOptionRow(
                    text = "Oscuro",
                    selected = currentTheme == "dark",
                    onClick = { settingsViewModel.setAppTheme("dark") }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { authViewModel.logout() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Icon(Icons.Filled.ExitToApp, contentDescription = null)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Cerrar Sesión")
            }
        }
    }
}
