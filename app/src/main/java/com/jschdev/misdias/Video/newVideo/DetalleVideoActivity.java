package com.jschdev.misdias.Video.newVideo;

/**
 * Created by jschdev on 04/12/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.jschdev.misdias.utils.UtilsFotos;
import com.jschdev.mivideo.R;

public class DetalleVideoActivity extends Activity {

    private UtilsFotos utilsFotos;
    private DetalleVideoAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_video);

        viewPager = (ViewPager) findViewById(R.id.pagerVideo);
        utilsFotos = new UtilsFotos(getApplicationContext(), this);

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        adapter = new DetalleVideoAdapter(DetalleVideoActivity.this, utilsFotos.misVideos());

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);
    }
}