package com.blibli.simpleapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    val login: String,
    val avatar_url: String,
    val public_repos: Int,
    val followers: Int,
    val following: Int,
    val followers_url: String,
    val following_url: String
): Parcelable
