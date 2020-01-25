package com.lightcyclesoftware.photoscodeexample.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.transition.Fade
import android.transition.TransitionInflater
import android.view.View
import com.lightcyclesoftware.photoscodeexample.R
import com.lightcyclesoftware.photoscodeexample.event.Event
import com.lightcyclesoftware.photoscodeexample.ui.DetailsTransition
import com.lightcyclesoftware.photoscodeexample.ui.ImageFragment
import com.lightcyclesoftware.photoscodeexample.ui.PhotosFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * Created by Edward on 2/3/2018.
 */

class MainActivity: AppCompatActivity() {
    private val MOVE_DEFAULT_TIME: Long = 1000
    private val FADE_DEFAULT_TIME: Long = 300
    private var clickedItem = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        val fragmentManager = supportFragmentManager
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, PhotosFragment() as Fragment, "IMAGE_LIST")
                .commit()
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: Event) {
        val url = event.data.get("url")
        val view = event.data.get("view")
        clickedItem = event.data.get("id") as String
        performTransition(url as String, view as View, clickedItem)
    }

    private fun performTransition(url: String?, view: View?, clickedItem: String?) {
        val previousFragment = supportFragmentManager.findFragmentByTag("IMAGE_LIST")

        val exitFade = Fade()
        exitFade.duration = FADE_DEFAULT_TIME

        val nextFragment = ImageFragment.newInstance(url, clickedItem)

        nextFragment.sharedElementEnterTransition = DetailsTransition()
        nextFragment.enterTransition = Fade()
        nextFragment.exitTransition = Fade()

        previousFragment?.sharedElementReturnTransition = DetailsTransition()
        previousFragment?.exitTransition = Fade()
        previousFragment?.reenterTransition = Fade()

        view?.let {
            clickedItem?.let { it1 ->
                supportFragmentManager
                    .beginTransaction()
                    .addSharedElement(it, it1)
                    .replace(R.id.fragment_container, nextFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}