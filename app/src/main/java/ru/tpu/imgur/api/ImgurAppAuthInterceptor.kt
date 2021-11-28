package ru.tpu.imgur.api

import okhttp3.Interceptor
import okhttp3.Response

class ImgurAppAuthInterceptor : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request().newBuilder()
			.addHeader("Authorization", "Client-ID 9280722d63297f2")
			.build()
		return chain.proceed(request)
	}
}