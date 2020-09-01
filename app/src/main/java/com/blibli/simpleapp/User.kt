package com.blibli.simpleapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    val username: String,
    val image: String
): Parcelable
