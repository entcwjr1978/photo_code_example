package com.lightcyclesoftware.photoscodeexample.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by Edward on 2/3/2018.
 */

class SquareFrameLayout: FrameLayout {
    constructor (context: Context) : super(context)
    constructor (context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor (context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}