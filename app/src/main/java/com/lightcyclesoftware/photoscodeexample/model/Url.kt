package com.lightcyclesoftware.photoscodeexample.model

/**
 * Created by Edward on 2/4/2018.
 */

data class Url(var size_code: String, var url: String, var width: Int = 0, var height: Int = 0, var quality: Float = 0.toFloat(), var mime: String? = null)