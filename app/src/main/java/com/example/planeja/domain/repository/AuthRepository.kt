package com.example.planeja.domain.repository

import com.example.planeja.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String, name: String): Result<User>
    suspend fun getCurrentUser(): User?
    suspend fun logout()
    suspend fun updateUser(user: User)
    fun isUserLoggedIn(): Boolean
}