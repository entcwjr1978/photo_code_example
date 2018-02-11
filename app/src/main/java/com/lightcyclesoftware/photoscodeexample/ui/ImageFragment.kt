package com.lightcyclesoftware.photoscodeexample.ui

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lightcyclesoftware.photoscodeexample.R
import android.graphics.Bitmap
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition


/**
 * Copyright (c) 2017 ADP. All rights reserved.
 */
class ImageFragment: Fragment() {
    var url: String? = null
    var image: ImageView? = null
    companion object {
        fun newInstance(url: String?): ImageFragment {
            var imageFragment = ImageFragment()
            var bundle = Bundle()
            bundle.putString("URL", url)
            imageFragment.arguments = bundle
            return imageFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        url = arguments?.getString("URL")
        postponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = (TransitionInflater.from(context).inflateTransition(android.R.transition.move))
            enterTransition = (TransitionInflater.from(context).inflateTransition(android.R.transition.fade))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.image_viewer_layout, container, false)
        image = view.findViewById(R.id.image)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(target)
    }

    private val target = object : SimpleTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            image?.setImageBitmap(resource)
            startPostponedEnterTransition()
        }
    }
}