package com.example.upaymimoni.domain.model

data class User(
    val id: String,
    val profilePictureUrl: String?,
    val displayName: String?,
    val phoneNumber: String?,
    val email: String?,
    val groups: List<String>?
)
