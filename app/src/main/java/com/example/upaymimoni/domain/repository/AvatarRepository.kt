package com.example.upaymimoni.domain.repository

import android.net.Uri

interface AvatarRepository {
    suspend fun getRandomAvatarUrl(seed: String): String
}