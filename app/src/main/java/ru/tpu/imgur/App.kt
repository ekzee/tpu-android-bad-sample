package ru.tpu.imgur

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.facebook.drawee.backends.pipeline.Fresco
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tpu.imgur.api.ImgurApi
import ru.tpu.imgur.api.ImgurAppAuthInterceptor

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

    class Scope(private val app: Application) {
        val albumSearchRepository: AlbumSearchRepository
        val commonChannelId: String = CHANNEL_ID_COMMON

        init {

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ImgurAppAuthInterceptor())
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.imgur.com/3/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            val imgurApi = retrofit.create(ImgurApi::class.java)

            albumSearchRepository = AlbumSearchRepository(imgurApi)
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