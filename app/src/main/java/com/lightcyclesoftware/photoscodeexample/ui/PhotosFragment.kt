package com.lightcyclesoftware.photoscodeexample.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lightcyclesoftware.photoscodeexample.R
import com.lightcyclesoftware.photoscodeexample.model.DataModel
import com.lightcyclesoftware.photoscodeexample.model.Record
import com.lightcyclesoftware.photoscodeexample.service.PhotosApiManager
import com.wang.avi.AVLoadingIndicatorView
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.UnsupportedEncodingException
import java.util.ArrayList

/**
 * Created by Edward on 2/3/2018.
 */

class PhotosFragment: Fragment() {
    val TAG = "PhotosFragment"
    val LOADING_POINT = 0.5f
    private val recordList = ArrayList<Record>()
    private var mAdapter: PhotosAdapter? = null
    private var mLayoutManager: GridLayoutManager? = null
    private var page = 0
    private var lastPageLoaded: Boolean = false
    private var mInfiniteScrollListener: PhotoInfiniteScrollListener? = null
    private var hasScrolled: Boolean = false

    companion object {
        const val RECORDS_PER_QUERY = 48
    }

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAVLoadingIndicatorView: AVLoadingIndicatorView
    lateinit var mScrollingAVLoadingIndicatorView: AVLoadingIndicatorView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        mRecyclerView = view.findViewById(R.id.photo_list)
        mAVLoadingIndicatorView = view.findViewById(R.id.loading_indicator)
        mScrollingAVLoadingIndicatorView = view.findViewById(R.id.scrollng_loading_indicator)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        if (mAdapter == null) {
            mAdapter = PhotosAdapter(recordList)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val enterTransition = (TransitionInflater.from(context).inflateTransition(android.R.transition.move))
            sharedElementEnterTransition = enterTransition
        }
    }

    override fun onResume() {
        super.onResume()
        if (recordList.size == 0) {
            mAVLoadingIndicatorView.visibility = View.VISIBLE
            PhotosApiManager.getCookie(activity as Context).subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe({ this.onNext() }, { this.onFailure(it) }, { this.onSuccess() })
        } else {
            mAVLoadingIndicatorView.visibility = View.GONE
            startPostponedEnterTransition()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
           if (mLayoutManager == null) {
               mLayoutManager = GridLayoutManager(activity, 3)
               mRecyclerView.layoutManager = mLayoutManager
               mRecyclerView.setHasFixedSize(true)
               mRecyclerView.adapter = mAdapter
               mInfiniteScrollListener = createInfiniteScrollListener()
               mRecyclerView.addOnScrollListener(mInfiniteScrollListener)
           } else {
               mRecyclerView.layoutManager = mLayoutManager
               mRecyclerView.setHasFixedSize(true)
               mRecyclerView.adapter = mAdapter
               mRecyclerView.addOnScrollListener(mInfiniteScrollListener)
           }
    }

    private fun createInfiniteScrollListener(): PhotoInfiniteScrollListener {
        return object : PhotoInfiniteScrollListener(RECORDS_PER_QUERY, LOADING_POINT, mLayoutManager as GridLayoutManager) {
            override fun onScrolledToEnd(firstVisibleItemPosition: Int) {
                if (!hasScrolled && !lastPageLoaded) {
                    try {
                        hasScrolled = true
                        mScrollingAVLoadingIndicatorView.visibility = View.VISIBLE
                        PhotosApiManager.getPhotos(activity as Context, page)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ this@PhotosFragment.onPhotosNext(it) }, { this@PhotosFragment.onPhotosFailure(it) }, { this@PhotosFragment.onPhotosSuccess() })
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun onNext() {}

    private fun onFailure(throwable: Throwable) {
        println(throwable.message)
        mAVLoadingIndicatorView.visibility = View.GONE
    }

    private fun onPhotosNext(dataModel: DataModel) {
        if (dataModel.data.album.photos.records.isEmpty()) {
            lastPageLoaded = true
        } else {
            recordList.addAll(dataModel.data.album.photos.records)
        }
    }

    private fun onPhotosFailure(throwable: Throwable) {
        println(throwable.message)
        hasScrolled = false
        mScrollingAVLoadingIndicatorView.visibility = View.GONE
        mAVLoadingIndicatorView.visibility = View.GONE
    }

    private fun onPhotosSuccess() {
        Log.d(TAG, "# of photos = " + Integer.toString(recordList.size))
        mAdapter!!.notifyDataSetChanged()
        mRecyclerView.scrollToPosition(page * RECORDS_PER_QUERY + RECORDS_PER_QUERY)
        mScrollingAVLoadingIndicatorView.visibility = View.GONE
        mAVLoadingIndicatorView.visibility = View.GONE
        page++
        hasScrolled = false
        startPostponedEnterTransition()
    }

    private fun onSuccess() {
        try {
            PhotosApiManager.getPhotos(activity as Context, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ this.onPhotosNext(it) }, { this.onPhotosFailure(it) }, { this.onPhotosSuccess() })
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }
}