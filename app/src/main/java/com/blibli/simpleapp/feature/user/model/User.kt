package com.blibli.simpleapp.feature.user.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val login: String? = null,
    val avatar_url: String? = null,
    val public_repos: Int? = null,
    val followers: Int? = null,
    val following: Int? = null,
    val followers_url: String? = null,
    val following_url: String? = null
) : Parcelable
