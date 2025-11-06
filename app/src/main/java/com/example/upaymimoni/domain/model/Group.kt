package com.example.upaymimoni.domain.model

data class Group(
    val id: String,
    var groupName: String,
    var groupImage: String?,
    var members: List<String>?,
    var expenses: List<String>?
)