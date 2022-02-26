package com.nevmem.survey.service.uid

import com.nevmem.survey.data.user.UserId
import com.nevmem.survey.report.report
import java.util.UUID

class UserIdProvider {

    init {
        report("user-id-provider", "init")
    }

    fun provide(): UserId {
        return UserId(UUID.randomUUID().toString())
    }
}
