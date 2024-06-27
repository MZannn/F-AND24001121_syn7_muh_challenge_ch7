package com.example.data.repository

import MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import com.example.data.local.database.dao.UserDao
import com.example.data.local.datastore.LocalDataStore
import com.example.domain.model.User


@ExperimentalCoroutinesApi
class UserRepositoryImplTest {

    private lateinit var userDao: UserDao
    private lateinit var localDataStore: LocalDataStore
    private lateinit var userRepository: UserRepositoryImpl

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        userDao = mock(UserDao::class.java)
        localDataStore = mock(LocalDataStore::class.java)
        userRepository = UserRepositoryImpl(userDao, localDataStore)
    }

    @Test
    fun register() {
        val user = User(id = 1, username = "testUser", fullname = "Test User", email = "Test Email", password = "testPassword", birthdate = "01-01-1990", address = "Test Address", photo = "Test Photo")
        userRepository.register(user)

        verify(userDao).insert(user)
    }

    @Test
    fun saveUser() = runBlockingTest {
        val user = User(id = 1, username = "testUser", fullname = "Test User", email = "Test Email", password = "testPassword", birthdate = "01-01-1990", address = "Test Address", photo = "Test Photo")
        userRepository.saveUser(user)

        verify(localDataStore).setUserLogin(user)
    }

    @Test
    fun clearUser() = runBlockingTest {
        userRepository.clearUser()

        verify(localDataStore).clearUser()
    }

    @Test
    fun getUserById() = runBlockingTest {
        val user = User(id = 1, username = "testUser", fullname = "Test User", email = "Test Email", password = "testPassword", birthdate = "01-01-1990", address = "Test Address", photo = "Test Photo")
        `when`(userDao.getUserById(1)).thenReturn(user)

        val result = userRepository.getUserById(1).first()

        assert(result == user)
    }

    @Test
    fun updateUser() = runBlockingTest {
        val user = User(id = 1, username = "testUser", fullname = "Test User", email = "Test Email", password = "testPassword", birthdate = "01-01-1990", address = "Test Address", photo = "Test Photo")
        userRepository.updateUser(user)

        verify(userDao).updateUser(user.username, user.fullname!!, user.birthdate!!, user.address!!, user.photo!!, user.id!!)
        verify(localDataStore).setUserLogin(user)
    }

    @Test
    fun login() {
        val user = User(id = 1, username = "testUser", fullname = "Test User", email = "Test Email", password = "testPassword", birthdate = "01-01-1990", address = "Test Address", photo = "Test Photo")
        `when`(userDao.login("testEmail", "testPassword")).thenReturn(user)

        val result = userRepository.login("testEmail", "testPassword")

        assert(result == user)
    }

    @Test
    fun getLogin() = runBlockingTest {
        `when`(localDataStore.getLogin()).thenReturn(flow { emit(true) })

        val result = userRepository.getLogin()

        assert(result)
    }
}
