package com.example.upaymimoni.data.service

import android.content.Context
import java.util.UUID
import androidx.core.content.edit

class DeviceIdService(context: Context) {
    private val preferences = context.getSharedPreferences("device_id_prefs", Context.MODE_PRIVATE)

    val deviceId: String by lazy {
        preferences.getString("device_id", null) ?: UUID.randomUUID().toString().also {
            preferences.edit { putString("device_id", it) }
        }
    }
}