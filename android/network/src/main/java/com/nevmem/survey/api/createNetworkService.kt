package com.nevmem.survey.api

import com.nevmem.survey.internal.NetworkServiceImpl

fun createNetworkService(): NetworkService = NetworkServiceImpl()
