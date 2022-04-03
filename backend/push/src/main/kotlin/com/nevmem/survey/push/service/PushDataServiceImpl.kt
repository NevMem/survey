package com.nevmem.survey.push.service

import com.nevmem.survey.data.user.UserId
import com.nevmem.survey.push.service.internal.PushDataEntityDTO
import com.nevmem.survey.push.service.internal.PushDataTable
import org.jetbrains.exposed.sql.transactions.transaction

class PushDataServiceImpl : PushDataService {
    override suspend fun register(userId: UserId, token: String?) {
        val currentRegistration = transaction {
            PushDataEntityDTO.find {
                PushDataTable.uid eq userId.uuid
            }.firstOrNull()
        }

        transaction {
            if (currentRegistration != null) {
                currentRegistration.token = token
            } else {
                PushDataEntityDTO.new {
                    this.uid = userId.uuid
                    this.token = token
                }
            }
        }
    }
}
