package com.lightcyclesoftware.photoscodeexample

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.lightcyclesoftware.photoscodeexample.model.*
import com.lightcyclesoftware.photoscodeexample.service.PhotosApiManager
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class PhotoUnitTest {
    @Mock
    var photosApiManager: PhotosApiManager? = null
    @Mock
    var activity: Activity? = null
    @Mock
    var sharedPreferences: SharedPreferences? = null

    @Test
    @Throws(Exception::class)
    fun valid_cookie() { // Mocks
        Mockito.`when`(sharedPreferences!!.getString("WALDO_COOKIE", null))
                .thenReturn("__staging.waldo.auth__=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjoiMmEyODQ4M2YtNWM2Yi00ZWU5LWE4YjUtYzFlMGU5NWUwYTY5Iiwicm9sZXMiOlsiYWRtaW5pc3RyYXRvciJdLCJpc3MiOiJ3YWxkbzpjb3JlIiwiZ3JhbnRzIjpbImFsYnVtczpkZWxldGU6KiIsImFsYnVtczpjcmVhdGU6KiIsImFsYnVtczplZGl0OioiLCJhbGJ1bXM6dmlldzoqIl0sImV4cCI6MTQ4MzQ1ODY0OCwiaWF0IjoxNDgwODY2NjQ4fQ.ZGH8VfaxhP8SI8rUNdfoXPwB0-2bF6VMSRhjbDfK-jI")
        Mockito.`when`(activity!!.getPreferences(Context.MODE_PRIVATE))
                .thenReturn(sharedPreferences)
        Mockito.`when`(photosApiManager!!.getCookie(activity!!))
                .thenReturn(Observable.just("ok"))
        Mockito.`when`(activity!!.getPreferences(Context.MODE_PRIVATE).getString("WALDO_COOKIE", null)).thenReturn("__staging.waldo.auth__=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjoiMmEyODQ4M2YtNWM2Yi00ZWU5LWE4YjUtYzFlMGU5NWUwYTY5Iiwicm9sZXMiOlsiYWRtaW5pc3RyYXRvciJdLCJpc3MiOiJ3YWxkbzpjb3JlIiwiZ3JhbnRzIjpbImFsYnVtczpkZWxldGU6KiIsImFsYnVtczpjcmVhdGU6KiIsImFsYnVtczplZGl0OioiLCJhbGJ1bXM6dmlldzoqIl0sImV4cCI6MTQ4MzQ1ODY0OCwiaWF0IjoxNDgwODY2NjQ4fQ.ZGH8VfaxhP8SI8rUNdfoXPwB0-2bF6VMSRhjbDfK-jI")
        assert(activity!!.getPreferences(Context.MODE_PRIVATE).getString("WALDO_COOKIE", null) == "__staging.waldo.auth__=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjoiMmEyODQ4M2YtNWM2Yi00ZWU5LWE4YjUtYzFlMGU5NWUwYTY5Iiwicm9sZXMiOlsiYWRtaW5pc3RyYXRvciJdLCJpc3MiOiJ3YWxkbzpjb3JlIiwiZ3JhbnRzIjpbImFsYnVtczpkZWxldGU6KiIsImFsYnVtczpjcmVhdGU6KiIsImFsYnVtczplZGl0OioiLCJhbGJ1bXM6dmlldzoqIl0sImV4cCI6MTQ4MzQ1ODY0OCwiaWF0IjoxNDgwODY2NjQ4fQ.ZGH8VfaxhP8SI8rUNdfoXPwB0-2bF6VMSRhjbDfK-jI")
    }

    @Test
    @Throws(Exception::class)
    fun no_cookie() { // Mocks
        Mockito.`when`(sharedPreferences!!.getString("WALDO_COOKIE", null))
                .thenReturn(null)
        Mockito.`when`(activity!!.getPreferences(Context.MODE_PRIVATE))
                .thenReturn(sharedPreferences)
        Mockito.`when`(photosApiManager!!.getCookie(activity!!))
                .thenReturn(Observable.just("ok"))
        Mockito.`when`(activity!!.getPreferences(Context.MODE_PRIVATE).getString("WALDO_COOKIE", null)).thenReturn(null)
    }

    @Test
    @Throws(Exception::class)
    fun empty_photo_list() { // Mocks
        val dataModel = setupEmptyDataModel()
        Mockito.`when`(photosApiManager!!.getPhotos(activity!!, 0)).thenReturn(Observable.just(dataModel))
        //assert
        photosApiManager!!.getPhotos(activity!!, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .toBlocking()
                .subscribe({ dataModel: DataModel -> onPhotosEmptyNext(dataModel) }, { thowable: Throwable -> onPhotosEmptyFailure(thowable) }) { onPhotosEmptySuccess() }
    }

    @Test
    @Throws(Exception::class)
    fun valid_photo_list() { // Mocks
        val dataModel = setupDataModel()
        Mockito.`when`(photosApiManager!!.getPhotos(activity!!, 0)).thenReturn(Observable.just(dataModel))
        //assert
        photosApiManager!!.getPhotos(activity!!, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .toBlocking()
                .subscribe({ dataModel: DataModel -> onPhotosNext(dataModel) }, { throwable: Throwable -> onPhotosFailure(throwable) }) { onPhotosSuccess() }
    }

    @Test
    @Throws(Exception::class)
    fun photo_list_with_error() { // Mocks
        Mockito.`when`(photosApiManager!!.getPhotos(activity!!, 0)).thenReturn(Observable.error(Error()))
        //assert
        photosApiManager!!.getPhotos(activity!!, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .toBlocking()
                .subscribe({ dataModel: DataModel -> onPhotosNext(dataModel) }, { throwable: Throwable -> onPhotosFailure(throwable) }) { onPhotosSuccess() }
    }

    private fun onPhotosNext(dataModel: DataModel) {
        assert(dataModel.data.album.photos.records.size > 0)
    }

    private fun onPhotosFailure(throwable: Throwable) {
        assert(throwable != null)
    }

    private fun onPhotosSuccess() {}
    private fun onPhotosEmptyNext(dataModel: DataModel) {
        assert(dataModel.data.album.photos.records.size == 0)
    }

    private fun onPhotosEmptyFailure(thowable: Throwable) {}
    private fun onPhotosEmptySuccess() {}
    private fun setupEmptyDataModel(): DataModel {
        return DataModel(Data(Album("", "", Photos(ArrayList()))))
    }

    private fun setupDataModel(): DataModel {
        val dataModel = DataModel(Data(Album("", "", Photos(ArrayList()))))
        val record = Record("", ArrayList())
        dataModel.data.album.photos.records.add(record)
        return dataModel
    }
}