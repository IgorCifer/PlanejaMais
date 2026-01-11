package com.example.planeja.domain.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Gerenciador de permissões para notificações.
 *
 * Responsabilidades:
 * - Verificar se permissão está concedida
 * - Verificar se deve mostrar rationale (explicação)
 * - Solicitar permissão
 * - Abrir configurações do app se necessário
 *
 * @property context Contexto da aplicação
 */
class NotificationPermissionManager(private val context: Context) {

    companion object {
        /**
         * Request code para identificar o resultado da solicitação de permissão.
         * Usado em onRequestPermissionsResult()
         */
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001

        /**
         * Permissão de notificações (Android 13+)
         */
        private const val NOTIFICATION_PERMISSION = Manifest.permission.POST_NOTIFICATIONS
    }

    /**
     * Verifica se a permissão de notificações está concedida.
     *
     * @return true se concedida, false caso contrário
     *
     * Comportamento por versão Android:
     * - Android 12 e inferior: sempre retorna true (não precisa permissão)
     * - Android 13+: verifica se POST_NOTIFICATIONS está concedida
     */
    fun isPermissionGranted(): Boolean {
        // Android 12 (API 32) e inferior não precisa de permissão runtime
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }

        return ContextCompat.checkSelfPermission(
            context,
            NOTIFICATION_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Verifica se precisa mostrar explicação (rationale) ao usuário.
     *
     * @param activity Activity que está solicitando a permissão
     * @return true se deve mostrar explicação, false caso contrário
     *
     * Retorna true quando:
     * - Usuário negou a permissão anteriormente (mas não marcou "não perguntar novamente")
     * - É a segunda vez que está solicitando a permissão
     */
    fun shouldShowRationale(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return false
        }

        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            NOTIFICATION_PERMISSION
        )
    }

    /**
     * Solicita a permissão de notificações ao usuário.
     *
     * @param activity Activity que está solicitando a permissão
     *
     * Mostra o dialog padrão do sistema solicitando permissão.
     * O resultado será recebido em onRequestPermissionsResult()
     */
    fun requestPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(NOTIFICATION_PERMISSION),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    /**
     * Abre as configurações do app para o usuário conceder permissão manualmente.
     *
     * Usado quando:
     * - Usuário negou permanentemente (marcou "não perguntar novamente")
     * - Permissão foi negada múltiplas vezes
     */
    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}
