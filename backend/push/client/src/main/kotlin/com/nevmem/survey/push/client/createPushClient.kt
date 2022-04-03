package com.nevmem.survey.push.client

import com.nevmem.survey.push.client.internal.PushClientImpl

fun createPushClient(baseUrl: String): PushClient = PushClientImpl(baseUrl)
