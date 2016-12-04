package com.lightcyclesoftware.photoscodeexample;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import rx.Observable;

import static com.lightcyclesoftware.photoscodeexample.MainActivity.RECORDS_PER_QUERY;

public class PhotosApiManager {
    public static final String TAG = "PhotosApiManager";

    public static PhotosApiManager getInstance() {
        return new PhotosApiManager();
    }

    public  Observable<String> getCookie(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        ReceivedCookiesInterceptor cookiesInterceptor = new ReceivedCookiesInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.cookieJar(new CookieJar() {
        private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put(url, cookies);
            String savedCookie = ((Cookie)((List)cookieStore.get(url)).get(0)).name() + "=" + ((Cookie)((List)cookieStore.get(url)).get(0)).value();
            Log.d(TAG, savedCookie);
            ((Activity)context).getPreferences(Context.MODE_PRIVATE).edit().putString("WALDO_COOKIE", savedCookie)
            .commit();
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    });
        httpClient.addInterceptor(cookiesInterceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://auth.staging.waldo.photos")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(PhotosApi.class).getCookie("andy", "1234", "/");
    }

    public Observable<DataModel> getPhotos(Context context, int page) throws UnsupportedEncodingException {
        GraphQLBody graphQLBody = new GraphQLBody();
        graphQLBody.setQuery("query {\n" +
                "  album(id: \"YWxidW06YTQwYzc5ODEtMzE1Zi00MWIyLTk5NjktMTI5NjIyZDAzNjA5\") {\n" +
                "    id\n" +
                "    name\n" +
                "    photos(slice: { limit:" + RECORDS_PER_QUERY +" offset: " + page * RECORDS_PER_QUERY + " }) {\n" +
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
                "}");

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.connectTimeout(5, TimeUnit.MINUTES);
        httpClient.readTimeout(5, TimeUnit.MINUTES);
        httpClient.writeTimeout(5, TimeUnit.MINUTES);
        httpClient.addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Interceptor.Chain chain) throws IOException {
                                Request original = chain.request();
                                String cookie = ((Activity)context).getPreferences(Context.MODE_PRIVATE).getString("WALDO_COOKIE", null);
                                Request request = original.newBuilder()
                                        .header("Cookie", cookie)
                                        .method(original.method(), original.body())
                                        .build();

                                return chain.proceed(request);
                            }
                        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://core-graphql.staging.waldo.photos")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(PhotosApi.class).getPhotos(graphQLBody);
    }
}
