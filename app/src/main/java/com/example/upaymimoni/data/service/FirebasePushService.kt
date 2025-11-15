package com.example.upaymimoni.data.service

import com.example.upaymimoni.domain.repository.FcmTokenRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FirebasePushService : FirebaseMessagingService() {

    // We have to use field injection, as constructor injection does not work.
    private val tokenRepo: FcmTokenRepository by inject()
    private val deviceIdService: DeviceIdService by inject()
    private val auth: FirebaseAuth by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val userId = auth.currentUser?.uid ?: return
        val deviceId = deviceIdService.deviceId

        CoroutineScope(Dispatchers.IO).launch {
            tokenRepo.saveToken(userId, deviceId, token)
        }
    }
}