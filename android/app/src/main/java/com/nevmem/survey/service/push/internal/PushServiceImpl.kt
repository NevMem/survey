package com.nevmem.survey.service.push.internal

import com.google.firebase.messaging.FirebaseMessaging
import com.nevmem.survey.network.api.NetworkService
import com.nevmem.survey.service.preferences.PreferencesService
import com.nevmem.survey.service.push.api.PushService
import com.nevmem.survey.service.settings.api.SettingsService
import com.nevmem.survey.service.uid.UserIdProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class PushServiceImpl(
    private val background: CoroutineScope,
    private val settingsService: SettingsService,
    private val networkService: NetworkService,
    private val userIdProvider: UserIdProvider,
) : PushService {

    private var currentToken: String? = null

    init {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            currentToken = if (!it.isSuccessful) {
                null
            } else {
                it.result
            }
            sendToken()
        }

        background.launch {
            settingsService.isPushNotificationsEnabled.changes.collect {
                sendToken()
            }
        }
    }

    private fun sendToken() {
        var token = currentToken
        if (!settingsService.isPushNotificationsEnabled.value) {
            token = null
        }
        val uid = userIdProvider.provide()
        background.launch {
            try {
                networkService.sendToken(uid, token)
            } catch (exception: Exception) {
                println(exception)
            }
        }
    }
}
