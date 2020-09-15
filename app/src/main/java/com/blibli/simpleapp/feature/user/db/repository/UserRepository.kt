package com.blibli.simpleapp.feature.user.db.repository

import android.app.Application
import android.util.Log
import com.blibli.simpleapp.core.di.scope.UserScope
import com.blibli.simpleapp.core.network.service.UserService
import com.blibli.simpleapp.feature.user.db.builder.RoomDB
import com.blibli.simpleapp.feature.user.db.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@UserScope
class UserRepository @Inject constructor(
    application: Application,
    private var userService: UserService
) {

    private val userDAO = RoomDB.getDatabase(application).userDao()

    @ExperimentalCoroutinesApi
    suspend fun showUser(username: String) = flow {
        var user: User? = getUserByUsername(username)

        if (user == null) {
            user = fetchUserByUsername(username).toUserModule()
            addUser(user)
        }

        emit(user)
    }.flowOn((Dispatchers.IO))


    @ExperimentalCoroutinesApi
    suspend fun getUsers() =
        flow {
            emit(userDAO.getUsers().map {
                it.toUserModule()
            } as ArrayList<com.blibli.simpleapp.feature.user.model.User>)
        }.flowOn(Dispatchers.IO)

    suspend fun fetchUsers(query: String, page: Int, perPage: Int) =
        flow {
            emit(userService.getUsers(query, page, perPage))
        }

    suspend fun fetchUserByUsernameAndCategory(
        username: String,
        category: String,
        page: Int,
        perPage: Int
    ) = flow {
        emit(
            userService.getUserByUsernameAndCategory(
                username,
                category,
                page,
                perPage
            )
        )
    }

    private suspend fun fetchUserByUsername(username: String) =
        userService.getUserByUsername(username)

    private fun getUserByUsername(username: String) =
        userDAO.getUserByUsername(username)

    private fun addUser(user: User) {
        userDAO.addUser(user)
        Log.d("User", "added with username ${user.login}")
    }

    private fun com.blibli.simpleapp.feature.user.model.User.toUserModule(): User {

        return User(
            null,
            this.login,
            this.avatar_url,
            this.public_repos,
            this.followers,
            this.following,
            this.followers_url,
            this.following_url
        )
    }

    private fun User.toUserModule(): com.blibli.simpleapp.feature.user.model.User {

        return com.blibli.simpleapp.feature.user.model.User(
            this.login,
            this.avatar_url,
            this.public_repos,
            this.followers,
            this.following,
            this.followers_url,
            this.following_url
        )
    }

    fun addUsers(vararg users: User) {
        userDAO.addUsers(*users)
    }

    fun deleteUsers(vararg users: User) {
        userDAO.deleteUsers(*users)
    }
}
