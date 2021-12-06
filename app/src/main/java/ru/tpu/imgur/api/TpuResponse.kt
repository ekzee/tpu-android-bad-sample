package ru.tpu.imgur.api

class TpuResponse<T>(
    val code: Int,
    val data: T
)