package com.example.upaymimoni.domain.model

data class Transaction(
    val fromUserId: String,
    val toUserId: String,
    val amount: Double
)