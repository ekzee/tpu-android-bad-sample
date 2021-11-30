package ru.tpu.imgur.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ImgurApi {
    @GET("gallery/search")
    fun search(
        @Query("q_all") query: String,
        @Query("q_type") type: String
    ): Call<ImgurApiResponse<List<ImgurApiAlbum>>>
}