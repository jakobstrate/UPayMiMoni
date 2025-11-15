package com.example.upaymimoni.domain.model.factory

import com.example.upaymimoni.domain.model.BaseNotificationBody

object NotificationFactory {
    /**
     * Constructs the basic pay me my money request.
     *
     * @param senderName The full name who is requesting that the notification be sent.
     * @param amount The amount which the user owes, should add the currency as well.
     */
    fun paymentRequestNotification(senderName: String, amount: String): BaseNotificationBody {
        return BaseNotificationBody(
            title = "You owe money",
            message = "$senderName has requested that you promptly send them their $amount."
        )
    }
}