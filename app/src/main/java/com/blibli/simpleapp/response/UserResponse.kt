package com.blibli.simpleapp.response

import android.os.Parcelable
import com.blibli.simpleapp.data.User
import kotlinx.android.parcel.Parcelize

// GSON
@Parcelize
data class UserResponse(
    val total_count: Int? = null,
    val incomplete_results: Boolean? = null,
    val items: List<User>? = null
) : Parcelable

// Moshi
//@Parcelize
//@JsonClass(generateAdapter = false)
//data class UserResponse (
//    val total_count: Int? = null,
//    val incomplete_results: Boolean? = null,
//    val items: List<User>? = null
//): Parcelable

// Jackson
//@Parcelize
//@JsonIgnoreProperties(ignoreUnknown=true)
//data class UserResponse (
//    @JsonProperty("total_count")
//    val total_count: Int? = null,
//
//    @JsonProperty("incomplete_results")
//    val incomplete_results: Boolean? = null,
//
//    @JsonProperty("items")
//    val items: List<User>? = null
//): Parcelable

