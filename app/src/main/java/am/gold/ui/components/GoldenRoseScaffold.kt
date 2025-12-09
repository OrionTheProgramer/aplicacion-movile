package am.gold.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import am.gold.ui.theme.DarkBackground
import am.gold.ui.theme.DarkSurface
import am.gold.ui.theme.GoldenAccent
import am.gold.ui.theme.ValorantRed
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoldenRoseScreen(
    title: String? = null,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: (@Composable () -> Unit)? = null,
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            DarkBackground,
            DarkSurface.copy(alpha = 0.92f)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                if (title != null) {
                    CenterAlignedTopAppBar(
                        title = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(title, style = MaterialTheme.typography.titleLarge)
                                if (subtitle != null) {
                                    Text(
                                        subtitle,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            onBack?.let {
                                IconButton(onClick = it) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "Volver"
                                    )
                                }
                            }
                        },
                        actions = actions,
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            },
            floatingActionButton = floatingActionButton,
            snackbarHost = snackbarHost
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                content = content
            )
        }
    }
}

@Composable
fun GoldenSurfaceCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    supportingText: String? = null,
    trailing: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (title != null || trailing != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        title?.let { Text(it, style = MaterialTheme.typography.titleLarge) }
                        supportingText?.let {
                            Text(
                                it,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    trailing?.invoke()
                }
            }
            content()
        }
    }
}

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = ValorantRed,
            contentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun PillBadge(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = GoldenAccent.copy(alpha = 0.16f),
        border = ButtonDefaults.outlinedButtonBorder
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = GoldenAccent,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GoldenRoseScreenFullPreview() {
    GoldenRoseScreen(
        title = "Marketplace",
        subtitle = "Claves de skins listas para usar",
        onBack = {}
        ,
        actions = {
            Text("Acción", color = MaterialTheme.colorScheme.primary)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Text("+")
            }
        }) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Item 1")
            Text("Item 2")
            Text("Item 3")
        }
    }
}






