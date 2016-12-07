package com.lightcyclesoftware.photoscodeexample.activity;

import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.lightcyclesoftware.photoscodeexample.ui.PhotoInfiniteScrollListener;
import com.lightcyclesoftware.photoscodeexample.ui.PhotosAdapter;
import com.lightcyclesoftware.photoscodeexample.R;
import com.lightcyclesoftware.photoscodeexample.model.DataModel;
import com.lightcyclesoftware.photoscodeexample.service.PhotosApiManager;
import com.wang.avi.AVLoadingIndicatorView;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.bind(this);
    }
}
