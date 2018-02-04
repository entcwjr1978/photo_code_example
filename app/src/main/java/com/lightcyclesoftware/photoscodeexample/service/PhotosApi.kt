package com.lightcyclesoftware.photoscodeexample.service

import com.lightcyclesoftware.photoscodeexample.model.DataModel
import com.lightcyclesoftware.photoscodeexample.model.GraphQLBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable

/**
 * Created by Edward on 2/3/2018.
 */

interface PhotosApi {
    @FormUrlEncoded
    @POST("/")
    fun getCookie(@Field("username") user: String, @Field("password") password: String, @Field("dest") dest: String): Observable<String>

    @POST("/gql")
    fun getPhotos(@Body graphQLBody: GraphQLBody): Observable<DataModel>
}