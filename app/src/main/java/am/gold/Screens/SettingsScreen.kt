package am.gold.Screens

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import am.gold.ui.components.GoldenRoseScreen
import am.gold.ui.components.GoldenSurfaceCard
import am.gold.ui.components.PrimaryButton
import coil.compose.AsyncImage

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

    GoldenRoseScreen(
        title = "Ajustes",
        subtitle = "Preferencias y cuenta"
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GoldenSurfaceCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    AsyncImage(
                        model = photoUri ?: R.drawable.ic_profile_placeholder,
                        contentDescription = "Foto de Perfil",
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(username.ifBlank { "Usuario" }, style = MaterialTheme.typography.headlineSmall)
                        if (email.isNotBlank()) {
                            Text(email, style = MaterialTheme.typography.bodyMedium)
                        }
                        if (bio.isNotBlank()) {
                            Text(bio, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                TextButton(
                    onClick = { navController.navigate(AppScreens.EditProfileScreen.route) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Editar perfil")
                }
            }

            SettingItemDivider(title = "Preferencias")

            GoldenSurfaceCard {
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
            }

            SettingItemDivider(title = "Apariencia")
            GoldenSurfaceCard {
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

            PrimaryButton(
                text = "Cerrar sesion",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                onClick = { authViewModel.logout() }
            )
        }
    }
}

@Composable
fun SettingItemDivider(title: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
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
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
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
            .padding(horizontal = 4.dp),
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
