package com.nevmem.survey.service.uid

import com.nevmem.survey.data.user.UserId
import java.util.UUID

class UserIdProvider {
    fun provide(): UserId {
        return UserId(UUID.randomUUID().toString())
    }
}
