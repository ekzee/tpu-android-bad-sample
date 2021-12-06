package ru.tpu.imgur

import android.content.SharedPreferences
import android.util.Log
import retrofit2.Call
import retrofit2.Response
import ru.tpu.imgur.api.TpuApi
import ru.tpu.imgur.api.TpuResponse
import java.util.*

private const val PREF_DEVICE_ID = "device_id"

class PushTokenRepository(
    sharedPreferences: SharedPreferences,
    private val tpuApi: TpuApi
) {

    private val deviceId: String

    init {
        var deviceId = sharedPreferences.getString(PREF_DEVICE_ID, null)
        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString()
            sharedPreferences.edit().putString(PREF_DEVICE_ID, deviceId).apply()
        }
        this.deviceId = deviceId
    }

    fun updateToken(newToken: String) {
        Log.d("push", "new token = $newToken")
        tpuApi.registerToken(deviceId, newToken)
            .enqueue(object : retrofit2.Callback<TpuResponse<Unit?>> {
                override fun onResponse(
                    call: Call<TpuResponse<Unit?>>,
                    response: Response<TpuResponse<Unit?>>
                ) {

                }

                override fun onFailure(call: Call<TpuResponse<Unit?>>, t: Throwable) {

                }
            })
    }
}