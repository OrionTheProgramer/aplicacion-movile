package am.gold.Screens

import android.app.Application
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import am.gold.Navigation.AppScreens
import am.gold.R
import am.gold.ViewModel.AuthViewModel
import am.gold.ViewModel.AuthViewModelFactory
import am.gold.ViewModel.SettingsViewModel
import am.gold.ViewModel.SettingsViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {

    val context = LocalContext.current
    val application = context.applicationContext as Application
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(application))
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(application))

    val username by settingsViewModel.username.collectAsState()
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
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_placeholder),
                    contentDescription = "Foto de Perfil",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(username, style = MaterialTheme.typography.headlineSmall)
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
            Column(Modifier.selectableGroup().padding(horizontal = 16.dp)) {
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
                onClick = {
                    Log.d("SettingsScreen", "Logout button clicked!")
                    authViewModel.logout()
                },
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

@Composable
fun SettingItemDivider(title: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
        )
        Divider()
    }
}

@Composable
fun SettingSwitchItem(
    text: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun SettingTextItem(text: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ThemeOptionRow(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}