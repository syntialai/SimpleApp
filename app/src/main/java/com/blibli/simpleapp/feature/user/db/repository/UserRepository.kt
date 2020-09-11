package com.blibli.simpleapp.feature.user.db.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.blibli.simpleapp.core.network.service.UserService
import com.blibli.simpleapp.feature.user.db.builder.RoomDB
import com.blibli.simpleapp.feature.user.db.dao.UserDAO
import com.blibli.simpleapp.feature.user.db.model.User
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private var application: Application) {

    private val userDAO = RoomDB.getDatabase(application).userDao()

    fun getUserByUsername(username: String): User? {
        return userDAO.getUserByUsername(username)
    }

    fun getUsers(): Observable<List<User>> {
        return userDAO.getUsers()
    }

//    fun addUsers(vararg users: User) {
//        userDAO.addUsers(*users)
//    }
//
    suspend fun addUser(user: User) {
        userDAO.addUser(user)
        Log.d("User", "added with username ${user.login}")
    }
//
//    fun deleteUsers(vararg users: User) {
//        userDAO.deleteUsers(*users)
//    }
}
