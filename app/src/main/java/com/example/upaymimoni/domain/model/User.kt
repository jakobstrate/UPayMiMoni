package com.example.upaymimoni.domain.model

data class User(
    val id: String = "",
    val profilePictureUrl: String? = null,
    val displayName: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val groups: List<String> = emptyList()
)
