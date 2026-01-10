package com.example.planeja.data.repository

import android.content.Context
import com.example.planeja.data.local.dao.UserDao
import com.example.planeja.data.local.entity.UserEntity
import com.example.planeja.data.mapper.toUser
import com.example.planeja.domain.model.User
import com.example.planeja.domain.repository.AuthRepository
import java.security.MessageDigest

class AuthRepositoryImpl(
    private val userDao: UserDao,
    private val context: Context
) : AuthRepository {

    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CURRENT_USER_ID = "current_user_id"
        private const val INVALID_USER_ID = -1
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            // Validações básicas
            if (email.isBlank() || password.isBlank()) {
                return Result.failure(Exception("Email e senha não podem estar vazios"))
            }

            val user = userDao.getUserByEmail(email)
            if (user == null) {
                Result.failure(Exception("Usuário não encontrado"))
            } else if (!verifyPassword(password, user.passwordHash)) {
                Result.failure(Exception("Senha incorreta"))
            } else {
                saveCurrentUserId(user.id)
                Result.success(user.toUser())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String, name: String): Result<User> {
        return try {
            // Validações
            if (email.isBlank() || password.isBlank() || name.isBlank()) {
                return Result.failure(Exception("Todos os campos são obrigatórios"))
            }

            if (!isValidEmail(email)) {
                return Result.failure(Exception("Email inválido"))
            }

            if (password.length < 6) {
                return Result.failure(Exception("A senha deve ter no mínimo 6 caracteres"))
            }

            // Verifica se email já existe
            if (userDao.getUserByEmail(email) != null) {
                return Result.failure(Exception("Email já cadastrado"))
            }

            val hashedPassword = hashPassword(password)
            val userEntity = UserEntity(
                email = email,
                passwordHash = hashedPassword,
                name = name
            )

            val userId = userDao.insertUser(userEntity)
            saveCurrentUserId(userId.toInt())
            Result.success(userEntity.copy(id = userId.toInt()).toUser())
        } catch (e: Exception) {
            Result.failure(Exception("Erro ao criar conta: ${e.message}"))
        }
    }

    override suspend fun getCurrentUser(): User? {
        val userId = prefs.getInt(KEY_CURRENT_USER_ID, INVALID_USER_ID)
        if (userId == INVALID_USER_ID) return null
        return userDao.getUserById(userId)?.toUser()
    }

    override suspend fun logout() {
        prefs.edit().remove(KEY_CURRENT_USER_ID).apply()
    }

    override fun isUserLoggedIn(): Boolean {
        return prefs.getInt(KEY_CURRENT_USER_ID, INVALID_USER_ID) != INVALID_USER_ID
    }

    private fun saveCurrentUserId(userId: Int) {
        prefs.edit().putInt(KEY_CURRENT_USER_ID, userId).apply()
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    private fun verifyPassword(password: String, hash: String): Boolean {
        return hashPassword(password) == hash
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
