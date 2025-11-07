package com.example.upaymimoni.domain.model

import android.net.Uri

data class User(
    val id: String,
    val profilePictureUrl: Uri?,
    val displayName: String?,
    val phoneNumber: String?,
    val email: String?,
    val groups: List<String>?
)
