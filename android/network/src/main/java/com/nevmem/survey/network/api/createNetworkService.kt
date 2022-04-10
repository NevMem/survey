package com.nevmem.survey.network.api

import com.nevmem.survey.network.internal.NetworkServiceImpl

fun createNetworkService(
    backendBaseUrlProvider: BackendBaseUrlProvider,
): NetworkService = NetworkServiceImpl(backendBaseUrlProvider)
