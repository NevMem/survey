package com.nevmem.survey.push.service.push

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import com.nevmem.survey.push.service.data.PushDataService

class PushServiceImpl(
    private val pushDataService: PushDataService,
) : PushService {
    private val messaging: FirebaseMessaging
        get() = FirebaseMessaging.getInstance()

    override suspend fun broadcastAll(title: String, message: String): Long {
        val tokens = pushDataService.getAllTokens()
        val groups = tokens.mapIndexed { index, s -> index to s }.groupBy { it.first / 100 }
        groups.forEach {
            val notification = Notification.builder()
                .setTitle(title)
                .setBody(message)
                .build()
            val multicastMessage = MulticastMessage.builder()
                .addAllTokens(it.value.map { pair -> pair.second })
                .setNotification(notification)
                .build()
            messaging.sendMulticast(multicastMessage)
        }
        return tokens.size.toLong()
    }
}
