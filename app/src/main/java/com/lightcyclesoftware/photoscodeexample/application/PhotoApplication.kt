package com.lightcyclesoftware.photoscodeexample.application

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * Created by Edward on 2/4/2018.
 */

class PhotoApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}