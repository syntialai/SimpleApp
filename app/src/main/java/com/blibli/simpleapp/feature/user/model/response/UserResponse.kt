package com.blibli.simpleapp.feature.user.model.response

import android.os.Parcelable
import com.blibli.simpleapp.feature.user.model.User
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserResponse(
    val total_count: Int? = null,
    val incomplete_results: Boolean? = null,
    val items: ArrayList<User>? = null
) : Parcelable