package com.example.data.repository

import com.example.data.local.database.dao.UserDao
import com.example.data.local.datastore.LocalDataStore
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val localDataStore: LocalDataStore
) : UserRepository {

    override val userFlow: Flow<User?> = localDataStore.getUser()

    override fun register(user: User) {
        userDao.insert(user)
    }

    override suspend fun saveUser(user: User) {
        localDataStore.setUserLogin(user)
    }

    override suspend fun clearUser() {
        localDataStore.clearUser()
    }

    override suspend fun getUserById(id: Int): Flow<User?> {
        return flow {
            emit(userDao.getUserById(id))
        }
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(
            username = user.username,
            fullname = user.fullname!!,
            birthdate = user.birthdate!!,
            address = user.address!!,
            photo = user.photo!!,
            id = user.id!!
        )
        saveUser(user)
    }

    override fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }

    override suspend fun getLogin(): Boolean {
        return localDataStore.getLogin().first()
    }
}