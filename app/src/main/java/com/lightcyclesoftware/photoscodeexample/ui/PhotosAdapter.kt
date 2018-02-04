package com.lightcyclesoftware.photoscodeexample.ui

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.lightcyclesoftware.photoscodeexample.R
import com.lightcyclesoftware.photoscodeexample.model.Record

/**
 * Created by Edward on 2/4/2018.
 */

class PhotosAdapter(recordList: List<Record>) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {
    private val IMAGE_SIZE = "medium"
    private val mRecordList: List<Record> = recordList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.photo_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageIndex!!.text = Integer.toString(position)
        val record = mRecordList[position]
        val uri = Uri.parse(getMedium2xUrl(record))
        val draweeView = holder.image
        draweeView.setImageURI(uri, null)
    }

    override fun getItemCount(): Int {
        return mRecordList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image = view.findViewById<SimpleDraweeView>(R.id.image)
        var imageIndex = view.findViewById<TextView>(R.id.image_index)
    }

    private fun getMedium2xUrl(record: Record): String? {
        return record.urls.firstOrNull { it.size_code.contains(IMAGE_SIZE) }?.url
    }
}