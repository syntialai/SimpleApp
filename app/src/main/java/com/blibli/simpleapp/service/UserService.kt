package com.blibli.simpleapp.service

import com.blibli.simpleapp.data.User
import com.blibli.simpleapp.response.UserResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    @GET("search/users")
    fun getUsers(@Query("q") q: String): Observable<UserResponse>

    @GET("users/{username}")
    fun getUserByUsername(@Path("username") username: String): Observable<User>

    @GET("users/{username}/{category}")
    fun getUserByUsernameAndCategory(
        @Path("username") username: String,
        @Path("category") category: String
    ): Observable<ArrayList<User>>
}
