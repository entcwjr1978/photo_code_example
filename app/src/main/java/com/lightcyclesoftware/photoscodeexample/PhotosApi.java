package com.lightcyclesoftware.photoscodeexample;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface PhotosApi {

    @FormUrlEncoded
    @POST("/")
    Observable<String> getCookie(@Field("username") String user, @Field("password") String password, @Field("dest") String dest);

    @POST("/gql")
    Observable<DataModel> getPhotos(@Body GraphQLBody graphQLBody);
}
