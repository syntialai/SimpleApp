package com.blibli.simpleapp.core.network.service

import com.blibli.simpleapp.feature.user.model.User
import com.blibli.simpleapp.feature.user.model.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    @GET("search/users")
    suspend fun getUsers(
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): UserResponse

    @GET("users/{username}")
    suspend fun getUserByUsername(@Path("username") username: String): User

    @GET("users/{username}/{category}")
    suspend fun getUserByUsernameAndCategory(
        @Path("username") username: String,
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): ArrayList<User>
}
