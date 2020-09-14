package com.blibli.simpleapp.feature.user.db.dao

import androidx.room.*
import com.blibli.simpleapp.feature.user.db.model.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM user_table ORDER BY username ASC")
    fun getUsers(): List<User>

    @Query("SELECT * FROM user_table WHERE username = :username")
    fun getUserByUsername(username: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUsers(vararg users: User)

    @Insert
    fun addUser(user: User)

    @Query("DELETE FROM user_table WHERE username = :username")
    fun deleteUserByUsername(username: String)

    @Delete
    fun deleteUsers(vararg users: User)
}
