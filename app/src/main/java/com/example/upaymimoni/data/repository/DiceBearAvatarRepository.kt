package com.example.upaymimoni.data.repository

import com.example.upaymimoni.domain.repository.AvatarRepository

class DiceBearAvatarRepository() : AvatarRepository{
    val api: String = "https://api.dicebear.com/9.x/bottts-neutral/svg"
    override suspend fun getRandomAvatarUrl(seed: String): String {
        return "$api?seed=$seed"
    }
}