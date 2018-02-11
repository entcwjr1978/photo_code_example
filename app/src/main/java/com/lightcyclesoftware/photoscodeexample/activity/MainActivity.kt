package com.lightcyclesoftware.photoscodeexample.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.transition.Fade
import android.transition.TransitionInflater
import com.lightcyclesoftware.photoscodeexample.R
import com.lightcyclesoftware.photoscodeexample.event.Event
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import android.view.View
import com.lightcyclesoftware.photoscodeexample.ui.ImageFragment
import com.lightcyclesoftware.photoscodeexample.ui.PhotosFragment
import org.greenrobot.eventbus.EventBus


/**
 * Created by Edward on 2/3/2018.
 */

class MainActivity: AppCompatActivity() {
    private val MOVE_DEFAULT_TIME: Long = 1000
    private val FADE_DEFAULT_TIME: Long = 300
    private var clickedItem = -1


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
        clickedItem = event.data.get("id") as Int
        performTransition(url as String, view as View)
    }

    private fun performTransition(url: String?, view: View?) {

        /*
        val mFragmentManager = supportFragmentManager
        if (isDestroyed) {
            return
        }


        val previousFragment = supportFragmentManager.findFragmentByTag("IMAGE_LIST")
        val nextFragment = ImageFragment.newInstance(url)

        val fragmentTransaction = mFragmentManager.beginTransaction()

        // 1. Exit for Previous Fragment
        val exitFade = Fade()
        exitFade.duration = FADE_DEFAULT_TIME
        previousFragment.exitTransition = exitFade

        // 2. Shared Elements Transition
        val enterTransitionSet = TransitionSet()
        enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move))
        enterTransitionSet.duration = MOVE_DEFAULT_TIME
        enterTransitionSet.startDelay = FADE_DEFAULT_TIME
        nextFragment.sharedElementEnterTransition = enterTransitionSet

        // 3. Enter Transition for New Fragment
        val enterFade = Fade()
        enterFade.startDelay = MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME
        enterFade.duration = FADE_DEFAULT_TIME
        nextFragment.enterTransition = enterFade

        */
        val previousFragment = supportFragmentManager.findFragmentByTag("IMAGE_LIST")
        previousFragment.sharedElementReturnTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.move)
        val exitFade = Fade()
        exitFade.duration = FADE_DEFAULT_TIME
        previousFragment.exitTransition = exitFade
        val nextFragment = ImageFragment.newInstance(url)

        supportFragmentManager
                .beginTransaction()
                .addSharedElement(view, ViewCompat.getTransitionName(view))
                .addToBackStack("IMAGE_DETAILS")
                .replace(R.id.fragment_container, nextFragment)
                .commit()

        /*
        fragmentTransaction.addSharedElement(view, ViewCompat.getTransitionName(view))
        fragmentTransaction.replace(R.id.fragment_container, nextFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commitAllowingStateLoss()
        */
    }

    override fun onBackPressed() {
        val previousFragment = supportFragmentManager.findFragmentByTag("IMAGE_LIST")
        previousFragment.sharedElementReturnTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.move)

        super.onBackPressed()
    }
}