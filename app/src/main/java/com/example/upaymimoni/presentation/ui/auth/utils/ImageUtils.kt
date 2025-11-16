package com.example.upaymimoni.presentation.ui.auth.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

// Top level function for creating a temporary image uri.
fun createTempImageUri(context: Context): Uri {
    val contentResolver = context.contentResolver

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.DISPLAY_NAME, "temp_profile_${System.currentTimeMillis()}.jpg")
    }

    return contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    ) ?: error("Failed to create temp image URI")
}