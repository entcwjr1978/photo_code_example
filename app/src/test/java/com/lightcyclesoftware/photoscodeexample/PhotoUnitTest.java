package com.lightcyclesoftware.photoscodeexample;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.lightcyclesoftware.photoscodeexample.model.DataModel;
import com.lightcyclesoftware.photoscodeexample.service.PhotosApiManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PhotoUnitTest {
    @Mock
    PhotosApiManager photosApiManager;

    @Mock
    Activity activity;

    @Mock
    SharedPreferences sharedPreferences;

    @Test
    public void valid_cookie() throws Exception {
        // Mocks
        when(sharedPreferences.getString("WALDO_COOKIE", null))
                .thenReturn("__staging.waldo.auth__=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjoiMmEyODQ4M2YtNWM2Yi00ZWU5LWE4YjUtYzFlMGU5NWUwYTY5Iiwicm9sZXMiOlsiYWRtaW5pc3RyYXRvciJdLCJpc3MiOiJ3YWxkbzpjb3JlIiwiZ3JhbnRzIjpbImFsYnVtczpkZWxldGU6KiIsImFsYnVtczpjcmVhdGU6KiIsImFsYnVtczplZGl0OioiLCJhbGJ1bXM6dmlldzoqIl0sImV4cCI6MTQ4MzQ1ODY0OCwiaWF0IjoxNDgwODY2NjQ4fQ.ZGH8VfaxhP8SI8rUNdfoXPwB0-2bF6VMSRhjbDfK-jI");

        when(activity.getPreferences(Context.MODE_PRIVATE))
                .thenReturn(sharedPreferences);

        when(photosApiManager.getCookie(activity))
                .thenReturn(Observable.just("ok"));

        when(activity.getPreferences(Context.MODE_PRIVATE).getString("WALDO_COOKIE", null)).thenReturn("__staging.waldo.auth__=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjoiMmEyODQ4M2YtNWM2Yi00ZWU5LWE4YjUtYzFlMGU5NWUwYTY5Iiwicm9sZXMiOlsiYWRtaW5pc3RyYXRvciJdLCJpc3MiOiJ3YWxkbzpjb3JlIiwiZ3JhbnRzIjpbImFsYnVtczpkZWxldGU6KiIsImFsYnVtczpjcmVhdGU6KiIsImFsYnVtczplZGl0OioiLCJhbGJ1bXM6dmlldzoqIl0sImV4cCI6MTQ4MzQ1ODY0OCwiaWF0IjoxNDgwODY2NjQ4fQ.ZGH8VfaxhP8SI8rUNdfoXPwB0-2bF6VMSRhjbDfK-jI");

        //assertions
        assert(activity.getPreferences(Context.MODE_PRIVATE).getString("WALDO_COOKIE", null).equals("__staging.waldo.auth__=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjoiMmEyODQ4M2YtNWM2Yi00ZWU5LWE4YjUtYzFlMGU5NWUwYTY5Iiwicm9sZXMiOlsiYWRtaW5pc3RyYXRvciJdLCJpc3MiOiJ3YWxkbzpjb3JlIiwiZ3JhbnRzIjpbImFsYnVtczpkZWxldGU6KiIsImFsYnVtczpjcmVhdGU6KiIsImFsYnVtczplZGl0OioiLCJhbGJ1bXM6dmlldzoqIl0sImV4cCI6MTQ4MzQ1ODY0OCwiaWF0IjoxNDgwODY2NjQ4fQ.ZGH8VfaxhP8SI8rUNdfoXPwB0-2bF6VMSRhjbDfK-jI"));
    }

    @Test
    public void no_cookie() throws Exception {
        // Mocks
        when(sharedPreferences.getString("WALDO_COOKIE", null))
                .thenReturn(null);

        when(activity.getPreferences(Context.MODE_PRIVATE))
                .thenReturn(sharedPreferences);

        when(photosApiManager.getCookie(activity))
                .thenReturn(Observable.just("ok"));

        when(activity.getPreferences(Context.MODE_PRIVATE).getString("WALDO_COOKIE", null)).thenReturn(null);
    }

    @Test
    public void empty_photo_list() throws Exception {
        // Mocks
        DataModel dataModel = setupEmptyDataModel();
        when(photosApiManager.getPhotos(activity, 0)).thenReturn(Observable.just(dataModel));

        //assert
        photosApiManager.getPhotos(activity, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::onPhotosNext
                        , this::onPhotosFailure
                        , this::onPhotosSuccess);
    }

    @Test
    public void valid_photo_list() throws Exception {

    }

    @Test
    public void photo_list_with_error() throws Exception {

    }

    private void onPhotosNext(DataModel dataModel){
        assert(dataModel.getData().getAlbum().getPhotos().getRecords().size() == 0);
    }

    private void onPhotosFailure(@NonNull Throwable thowable) {
        System.out.println(thowable.getMessage());
    }

    private void onPhotosSuccess(){

    }

    private DataModel setupEmptyDataModel() {
        DataModel dataModel = new DataModel();
        DataModel.Data data = new DataModel().new Data();
        DataModel.Album album  = new DataModel().new Album();
        DataModel.Photos photos = new DataModel().new Photos();
        photos.setRecords(new ArrayList<>());
        album.setPhotos(photos);
        data.setAlbum(album);
        dataModel.setData(data);
        return dataModel;
    }
}