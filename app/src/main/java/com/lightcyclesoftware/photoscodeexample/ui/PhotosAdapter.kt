package com.lightcyclesoftware.photoscodeexample.ui

import android.net.Uri
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lightcyclesoftware.photoscodeexample.R
import com.lightcyclesoftware.photoscodeexample.event.Event
import com.lightcyclesoftware.photoscodeexample.model.Record
import org.greenrobot.eventbus.EventBus
import java.util.*

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
        holder.imageIndex.text = Integer.toString(position)
        holder.itemView.setOnClickListener(null)
        val record = mRecordList[position]
        val url = getMedium2xUrl(record)
        val uri = Uri.parse(url)
        holder.image.transitionName = "item_" + position
        holder.itemView.setOnClickListener {
            ViewCompat.setTransitionName(holder.itemView, holder.itemView.resources.getString(R.string.imageTransitionName))
            EventBus.getDefault().post(Event("ImageClick", hashMapOf("id" to "item_" + position, "view" to holder.image, "url" to url)))
        }
        val draweeView = holder.image
        Glide.with(holder.itemView.context)
                .load(url)
                .into(draweeView)
    }

    override fun getItemCount(): Int {
        return mRecordList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image = view.findViewById<ImageView>(R.id.image)
        var imageIndex = view.findViewById<TextView>(R.id.image_index)
    }

    private fun getMedium2xUrl(record: Record): String? {
        return record.urls.firstOrNull { it.size_code.contains(IMAGE_SIZE) }?.url
    }
}