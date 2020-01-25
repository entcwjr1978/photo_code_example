package com.lightcyclesoftware.photoscodeexample.ui

<<<<<<< HEAD
import android.os.Bundle
import androidx.fragment.app.Fragment
=======
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.transition.TransitionInflater
>>>>>>> 0829545893dfa9db91b1bcdc4a842ef506a9d179
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lightcyclesoftware.photoscodeexample.R
import android.graphics.Bitmap
import android.graphics.Color
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition



class ImageFragment: Fragment() {
    var url: String? = null
    var clickedItem: String? = null
    var image: ImageView? = null
    var topLayout: ConstraintLayout? = null
    companion object {
        fun newInstance(url: String?, clickedItem: String?): ImageFragment {
            var imageFragment = ImageFragment()
            var bundle = Bundle()
            bundle.putString("URL", url)
            bundle.putString("CLICKED_ITEM", clickedItem)
            imageFragment.arguments = bundle
            return imageFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments?.getString("URL")
        clickedItem = arguments?.getString("CLICKED_ITEM")
        postponeEnterTransition()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.image_viewer_layout, container, false)
        image = view.findViewById(R.id.image)
        image?.transitionName = clickedItem
        topLayout = view.findViewById(R.id.top_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this)
                .asBitmap()
                .transition((BitmapTransitionOptions.withCrossFade()))
                .load(url)
                .into(target)
    }

    private val target = object : SimpleTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            image?.setImageBitmap(resource)
            createPaletteAsync(resource)
        }
    }

    fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            palette?.getDarkMutedColor(Color.DKGRAY)?.let {
                topLayout?.setBackgroundColor(it)
                image?.let { it1 -> scheduleStartPostponedTransition(it1) }
            }
        }
    }

    private fun scheduleStartPostponedTransition(sharedElement: View) {
        sharedElement.viewTreeObserver.addOnPreDrawListener( object: ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                sharedElement.viewTreeObserver.removeOnPreDrawListener(this)
                startPostponedEnterTransition()
                return true
            } })
    }
}