package com.example.planeja.ui.ajustes

import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.planeja.BuildConfig
import com.example.planeja.data.repository.NotificationRepository
import com.example.planeja.domain.permission.NotificationPermissionManager
import com.example.planeja.ui.auth.AuthViewModel
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(
    onLogout: () -> Unit = {},
    notificationRepository: NotificationRepository,
    permissionManager: NotificationPermissionManager,
    activity: Activity,
    authViewModel: AuthViewModel,
    onNavigateToEditProfile: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showPermissionRationaleDialog by remember { mutableStateOf(false) }

    val notificationsEnabled by notificationRepository.notificationsEnabled
        .collectAsState(initial = true)
    val currentUser by authViewModel.currentUser.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val bgLight = MaterialTheme.colorScheme.background
    val textGray = MaterialTheme.colorScheme.onSurfaceVariant
    val textDark = MaterialTheme.colorScheme.onSurface

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Ajustes",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF474747)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Personalize sua experiência",
                        style = MaterialTheme.typography.bodySmall,
                        color = textGray
                    )
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgLight)
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                onClick = onNavigateToEditProfile
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    ) {
                        Box(
                            modifier = Modifier.padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Perfil",
                            style = MaterialTheme.typography.bodyMedium,
                            color = textDark
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = currentUser?.name ?: "Usuário",
                            style = MaterialTheme.typography.bodySmall,
                            color = textGray
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0x26479DFF)
                    ) {
                        Box(
                            modifier = Modifier.padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Notificações diárias",
                            style = MaterialTheme.typography.bodyMedium,
                            color = textDark
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "Receba lembretes sobre suas finanças",
                            style = MaterialTheme.typography.bodySmall,
                            color = textGray
                        )
                    }

                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { enabled ->
                                if (enabled) {
                                    if (permissionManager.isPermissionGranted()) {
                                        scope.launch {
                                            notificationRepository.setNotificationsEnabled(true)
                                        }
                                    } else {
                                        if (permissionManager.shouldShowRationale(activity)) {
                                            showPermissionRationaleDialog = true
                                        } else {
                                            permissionManager.requestPermission(activity)
                                        }
                                    }
                                } else {
                                    scope.launch {
                                        notificationRepository.setNotificationsEnabled(false)
                                    }
                                }
                            }
                        )
                    }
                }

                if (BuildConfig.DEBUG) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        ),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Science,
                                    contentDescription = "Debug",
                                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Testes de Notificação (Debug)",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }

                            Spacer(Modifier.height(12.dp))

                            OutlinedButton(
                                onClick = {
                                    if (permissionManager.isPermissionGranted()) {
                                        try {
                                            notificationRepository.testNotificationImmediately()
                                            Toast.makeText(
                                                context,
                                                "Notificação enviada!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } catch (e: Exception) {
                                            Toast.makeText(
                                                context,
                                                "Erro: ${e.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Conceda permissão primeiro",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        permissionManager.requestPermission(activity)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Testar notificação imediata")
                            }

                            Spacer(Modifier.height(8.dp))

                            OutlinedButton(
                                onClick = {
                                    if (permissionManager.isPermissionGranted()) {
                                        try {
                                            notificationRepository.scheduleTestReminder()
                                            Toast.makeText(
                                                context,
                                                "Notificação em 10s",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } catch (e: Exception) {
                                            Toast.makeText(
                                                context,
                                                "Erro: ${e.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Conceda permissão primeiro",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        permissionManager.requestPermission(activity)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Science,
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Testar WorkManager (10s)")
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showLogoutDialog = true },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0x26D4183D)
                            ) {
                                Box(
                                    modifier = Modifier.padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = "Sair",
                                        tint = Color(0xFFD4183D)
                                    )
                                }
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Sair",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = textDark
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = "Encerrar sua sessão",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = textGray
                                )
                            }
                        }
                    }
                }
            }
        }

    if (showPermissionRationaleDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionRationaleDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = { Text("Permissão de Notificações") },
            text = {
                Text(
                    "O PlanejaMais precisa de permissão para enviar notificações " +
                            "e lembrá-lo de registrar suas transações diariamente."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPermissionRationaleDialog = false
                        permissionManager.requestPermission(activity)
                    }
                ) {
                    Text("Permitir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showPermissionRationaleDialog = false }
                ) {
                    Text("Agora não")
                }
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Sair da conta") },
            text = { Text("Tem certeza que deseja sair?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Sair", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
