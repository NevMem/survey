package com.nevmem.survey.push.service.push

import com.nevmem.survey.push.service.data.PushDataService

fun createPushService(pushDataService: PushDataService): PushService = PushServiceImpl(pushDataService)
