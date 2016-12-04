package com.lightcyclesoftware.photoscodeexample.service;


import java.io.IOException;
import java.util.HashSet;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ReceivedCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        ResponseBody body = ResponseBody.create(MediaType.parse("text/html; charset=utf-8"), "<ok>ok</ok>");
        return originalResponse.newBuilder().body(body).build();
    }
}
