package com.lightcyclesoftware.photoscodeexample.service

import android.app.Activity
import android.content.Context
import android.util.Log
import com.lightcyclesoftware.photoscodeexample.R
import com.lightcyclesoftware.photoscodeexample.model.DataModel
import com.lightcyclesoftware.photoscodeexample.model.GraphQLBody
import com.lightcyclesoftware.photoscodeexample.ui.PhotosFragment.Companion.RECORDS_PER_QUERY
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import rx.Observable
import java.io.UnsupportedEncodingException
import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.TimeUnit

/**
 * Created by Edward on 2/3/2018.
 */

object PhotosApiManager {
    val TAG = "PhotosApiManager"

    @JvmStatic
    fun getCookie(context: Context): Observable<String> {
        val logging = HttpLoggingInterceptor()
        val cookiesInterceptor = ReceivedCookiesInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.cookieJar(object : CookieJar {
            private val cookieStore = HashMap<HttpUrl, List<Cookie>>()

            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                cookieStore[url] = cookies
                val savedCookie = ((cookieStore[url] as List<*>)[0] as Cookie).name() + "=" + ((cookieStore[url] as List<*>)[0] as Cookie).value()
                Log.d(TAG, savedCookie)
                (context as Activity).getPreferences(Context.MODE_PRIVATE).edit().putString(context.getString(R.string.cookie_pref), savedCookie)
                        .commit()
            }

            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                val cookies = cookieStore[url]
                return cookies ?: ArrayList()
            }
        })
        httpClient.addInterceptor(cookiesInterceptor)
        val retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.get_cookie_endpoint))
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build())
                .build()
        return retrofit.create(PhotosApi::class.java).getCookie(context.getString(R.string.username), context.getString(R.string.password), "/")
    }

    @JvmStatic
    @Throws(UnsupportedEncodingException::class)
    fun getPhotos(context: Context, page: Int): Observable<DataModel> {
        val query = "query {\n" +
                "  album(id: \"vERTnkSHzxMzgfpo3GCNxV\") {\n" +
                "    id\n" +
                "    name\n" +
                "    photos(slice: { limit:" + RECORDS_PER_QUERY + " offset: " + page * RECORDS_PER_QUERY + " }) {\n" +
                "      records {\n" +
                "        id\n" +
                "        urls {\n" +
                "          size_code\n" +
                "          url\n" +
                "          width\n" +
                "          height\n" +
                "          quality\n" +
                "          mime\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"
        val graphQLBody = GraphQLBody(query)
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.connectTimeout(5, TimeUnit.MINUTES)
        httpClient.readTimeout(5, TimeUnit.MINUTES)
        httpClient.writeTimeout(5, TimeUnit.MINUTES)
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val cookie = (context as Activity).getPreferences(Context.MODE_PRIVATE).getString(context.getString(R.string.cookie_pref), null)
            val request = original.newBuilder()
                    .header("Cookie", cookie!!)

                    .method(original.method(), original.body())
                    .build()

            chain.proceed(request)
        }
        val retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.get_photos_endpoint))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build())
                .build()
        return retrofit.create(PhotosApi::class.java).getPhotos(graphQLBody)
    }
}