package com.lightcyclesoftware.photoscodeexample.service

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

/**
 * Created by Edward on 2/3/2018.
 */

class ReceivedCookiesInterceptor: Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        val body = ResponseBody.create(MediaType.parse("text/html; charset=utf-8"), "<ok>ok</ok>")
        return originalResponse.newBuilder().body(body).build()
    }
}