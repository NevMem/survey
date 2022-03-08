package com.nevmem.survey.network.api

import com.nevmem.survey.network.internal.NetworkServiceImpl

fun createNetworkService(
    baseUrl: String,
): NetworkService = NetworkServiceImpl(baseUrl)
