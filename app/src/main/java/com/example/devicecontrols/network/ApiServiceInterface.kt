package com.example.devicecontrols.network

import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServiceInterface {
    @Multipart
    @POST(ApiConfig.POST_LOGS_ADD)
    suspend fun postLogsAccountBalance(@Part("message") message: String): Boolean
}