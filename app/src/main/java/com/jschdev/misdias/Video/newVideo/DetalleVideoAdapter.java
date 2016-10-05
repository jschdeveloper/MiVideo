package com.jschdev.misdias.Video.newVideo;

/**
 * Created by jschdev on 04/12/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.jschdev.mivideo.R;

import java.util.ArrayList;

public class DetalleVideoAdapter extends PagerAdapter {

    private Activity myActivity;
    private ArrayList<String> videos;
    private LayoutInflater inflater;

    // constructor
    public DetalleVideoAdapter(Activity activity, ArrayList<String> videos) {
        this.myActivity = activity;
        this.videos = videos;

    }

    @Override
    public int getCount() {
        return this.videos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        VideoView videoView;
        Button btnClose;

        inflater = (LayoutInflater) myActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View viewLayout = inflater.inflate(R.layout.layout_video_image, container,
                false);

        videoView = (VideoView) viewLayout.findViewById(R.id.videoView);
        btnClose = (Button) viewLayout.findViewById(R.id.btnCloseVideos);

        videoView.setVideoURI(Uri.parse(videos.get(position)));
        videoView.setKeepScreenOn(true);
        videoView.setMediaController(new MediaController(myActivity));
        videoView.start();
        videoView.requestFocus();


        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myActivity.finish();
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}

