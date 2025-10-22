package am.gold

import am.gold.Navigation.AppNavigation
import am.gold.ui.theme.GoldenRoseTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import am.gold.ViewModel.SettingsViewModel
import am.gold.ViewModel.SettingsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val settingsViewModel: SettingsViewModel by lazy {
            ViewModelProvider(this, SettingsViewModelFactory(application)).get(SettingsViewModel::class.java)
        }


        setContent {
            val currentThemeSetting by settingsViewModel.appTheme.collectAsState()

            val useDarkTheme = when (currentThemeSetting) {
                "dark" -> true
                "light" -> false
                else -> true
            }

            GoldenRoseTheme(darkTheme = useDarkTheme) {
                Surface(/*...*/) {
                    AppNavigation()
                }
            }
        }
    }
}