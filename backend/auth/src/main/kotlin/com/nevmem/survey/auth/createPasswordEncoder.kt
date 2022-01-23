package com.nevmem.survey.auth

import com.nevmem.survey.auth.internal.PasswordEncoderImpl

fun createPasswordEncoder(): PasswordEncoder = PasswordEncoderImpl()
