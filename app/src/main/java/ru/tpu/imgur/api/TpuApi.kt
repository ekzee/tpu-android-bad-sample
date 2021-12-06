package ru.tpu.imgur.api

import retrofit2.Call
import retrofit2.http.*

interface TpuApi {
    @FormUrlEncoded
    @POST("push/")
    fun registerToken(
        @Field("deviceId") deviceId: String,
        @Field("token") token: String
    ): Call<TpuResponse<Unit?>>
}