package ru.tpu.imgur

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tpu.imgur.api.ImgurApi
import ru.tpu.imgur.api.ImgurAppAuthInterceptor

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)

        scope = Scope()
    }

    companion object {
        lateinit var scope: Scope
    }

    class Scope() {
        val albumSearchRepository: AlbumSearchRepository

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
    }
}