package com.nevmem.survey.role

import com.nevmem.survey.role.internal.RoleSerializerImpl

fun createRoleSerializer(roleModel: RoleModel): RoleSerializer = RoleSerializerImpl(roleModel)
