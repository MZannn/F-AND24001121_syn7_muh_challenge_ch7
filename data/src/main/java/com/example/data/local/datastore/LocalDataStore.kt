package com.example.data.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.model.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalDataStore(val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
    private val USER_KEY = stringPreferencesKey("user_key") //null
    private val IS_LOGIN_KEY = booleanPreferencesKey("is_login_key") //false
    private val gson = Gson()


    fun getLogin(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_LOGIN_KEY] ?: false
        }
    }

    suspend fun setUserLogin(user: User) {
        val json = gson.toJson(user)
        context.dataStore.edit { preferences ->
            preferences[USER_KEY] = json
            preferences[IS_LOGIN_KEY] = true
        }
    }

    fun getUser(): Flow<User?> {
        return context.dataStore.data.map { preferences ->
            Log.d("MyDataStore", "getUser: ${preferences[USER_KEY]}")
            preferences[USER_KEY]?.let { json ->
                gson.fromJson(json, User::class.java)
            }
        }
    }

    suspend fun clearUser() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_KEY)
        }
    }

    suspend fun isLoggedIn(): Boolean {
        val preferences = context.dataStore.data.first()
        val isLoginKey = preferences[IS_LOGIN_KEY] ?: true
        val userKey = preferences[USER_KEY]

        // Check if user_key is not null and isLoginKey is true
        return userKey != null && isLoginKey

    }


}