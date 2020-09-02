package com.blibli.simpleapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// GSON
@Parcelize
data class User(
    val login: String,
    val avatar_url: String,
    val public_repos: Int,
    val followers: Int,
    val following: Int,
    val followers_url: String,
    val following_url: String
) : Parcelable

// Moshi
//@Parcelize
//@JsonClass(generateAdapter = false)
//data class User (
//    val login: String,
//    val avatar_url: String,
//    val public_repos: Int,
//    val followers: Int,
//    val following: Int,
//    val followers_url: String,
//    val following_url: String
//): Parcelable

// Jackson
//@Parcelize
//@JsonIgnoreProperties(ignoreUnknown=true)
//data class User (
//    @JsonProperty("login")
//    val login: String,
//
//    @JsonProperty("avatar_url")
//    val avatar_url: String,
//
//    @JsonProperty("public_repos")
//    val public_repos: Int,
//
//    @JsonProperty("followers")
//    val followers: Int,
//
//    @JsonProperty("following")
//    val following: Int,
//
//    @JsonProperty("followers_url")
//    val followers_url: String,
//
//    @JsonProperty("following_url")
//    val following_url: String
//): Parcelable
