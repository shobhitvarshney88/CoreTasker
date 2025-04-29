package com.coretasker.android.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import okhttp3.internal.userAgent

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val uid: String,
    val name:String?,
    val email:String?,
    val photoUrl:String?
)
