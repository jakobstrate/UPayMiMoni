package com.example.upaymimoni.domain.model

data class Group(
    val id: String = "",
    var groupName: String = "",
    var groupImage: String? = null,
    var members: List<String>? = emptyList(),
    var expenses: List<String>? = emptyList()
)