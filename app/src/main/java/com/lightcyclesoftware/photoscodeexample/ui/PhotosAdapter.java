package com.lightcyclesoftware.photoscodeexample.ui;


import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lightcyclesoftware.photoscodeexample.R;
import com.lightcyclesoftware.photoscodeexample.model.DataModel;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    private static final String IMAGE_SIZE = "medium";
    private List<DataModel.Record> mRecordList;

    public PhotosAdapter(List<DataModel.Record> recordList) {
        this.mRecordList = recordList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageIndex.setText(Integer.toString(position));
        DataModel.Record record = mRecordList.get(position);
        Uri uri = Uri.parse(getMedium2xUrl(record));
        SimpleDraweeView draweeView = holder.image;
        draweeView.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        public SimpleDraweeView image;

        @BindView(R.id.image_index)
        public TextView imageIndex;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private String getMedium2xUrl(DataModel.Record record) {
        for (DataModel.Url url: record.getUrls()) {
            if (url.getSize_code().contains(IMAGE_SIZE)) {
                return url.getUrl();
            }
        }
        return null;
    }
}
