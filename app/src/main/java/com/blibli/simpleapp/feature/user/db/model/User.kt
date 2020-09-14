package com.blibli.simpleapp.feature.user.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "username")
    val login: String? = null,

    val avatar_url: String? = null,
    val public_repos: Int? = null,
    val followers: Int? = null,
    val following: Int? = null,
    val followers_url: String? = null,
    val following_url: String? = null
)
