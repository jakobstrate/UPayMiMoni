package com.example.upaymimoni.data.service

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.upaymimoni.domain.service.ToastService

class AndroidToastService(private val context: Context) : ToastService {
    override fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}