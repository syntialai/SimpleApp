package com.blibli.simpleapp.data.response

import android.os.Parcelable
import com.blibli.simpleapp.data.User
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserResponse (
    val total_count: Int? = null,
    val incomplete_results: Boolean? = null,
    val items: ArrayList<User>? = null
): Parcelable