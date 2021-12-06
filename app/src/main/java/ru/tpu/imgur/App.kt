package ru.tpu.imgur

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tpu.imgur.api.ImgurApi
import ru.tpu.imgur.api.ImgurAppAuthInterceptor
import ru.tpu.imgur.api.TpuApi

private const val CHANNEL_ID_COMMON = "common"

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)

        scope = Scope(this)
        scope.initChannels()
    }

    companion object {
        lateinit var scope: Scope
    }

    class Scope(val app: Application) {
        val albumSearchRepository: AlbumSearchRepository
        val pushTokenRepository: PushTokenRepository

        val commonChannelId: String = CHANNEL_ID_COMMON

        init {

            val baseOkHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            val imgurHttpClient = baseOkHttpClient.newBuilder()
                .addInterceptor(ImgurAppAuthInterceptor())
                .build()

            val imgurRetrofit = Retrofit.Builder()
                .baseUrl("https://api.imgur.com/3/")
                .client(imgurHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val imgurApi = imgurRetrofit.create(ImgurApi::class.java)

            albumSearchRepository = AlbumSearchRepository(imgurApi)

            val tpuRetrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .client(baseOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val tpuApi = tpuRetrofit.create(TpuApi::class.java)

            pushTokenRepository = PushTokenRepository(
                app.getSharedPreferences("app", Context.MODE_PRIVATE),
                tpuApi
            )
        }

        fun initChannels() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createCommonNotificationsChannel()
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun createCommonNotificationsChannel() {
            val name = app.getString(R.string.common_channel_name)
            val descriptionText = app.getString(R.string.common_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID_COMMON, name, importance)
            channel.description = descriptionText

            NotificationManagerCompat.from(app).createNotificationChannel(channel)
        }
    }
}