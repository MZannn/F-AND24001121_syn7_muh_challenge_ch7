package com.example.domain.repository

import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userFlow: Flow<User?>
    fun register(user: User)
    suspend fun saveUser(user: User)
    suspend fun clearUser()
    suspend fun getUserById(id: Int): Flow<User?>
    suspend fun updateUser(user: User)
    fun login(email: String, password: String): User?
    suspend fun getLogin(): Boolean
}