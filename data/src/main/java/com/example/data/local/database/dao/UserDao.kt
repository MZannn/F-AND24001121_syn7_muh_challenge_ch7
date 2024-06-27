package com.example.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.domain.model.User

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM user")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    fun login(email: String, password: String): User?

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id: Int): User

    @Query("UPDATE user SET username=:username, fullname=:fullname, birthdate=:birthdate, photo=:photo, address=:address WHERE id=:id")
    fun updateUser(username: String, fullname: String, birthdate: String, address: String, photo:String, id: Int)
}