package com.jschdev.misdias.Video;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.jschdev.misdias.utils.AppConstant;
import com.jschdev.misdias.utils.UtilsFotos;
import com.jschdev.mivideo.R;

import java.io.File;

public class DetalleVideo extends AppCompatActivity {

    private UtilsFotos utilsFotos;
    private Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_video);

        utilsFotos = new UtilsFotos(this);
        btnClose = (Button) findViewById(R.id.btnCloseVideos);
        Intent intent = getIntent();
        String nombreVideo = "";
        if (intent.getStringExtra(AppConstant.CLAVE_NOMBRE_VIDEO) != null) {
            nombreVideo = intent.getStringExtra(AppConstant.CLAVE_NOMBRE_VIDEO);

            Uri vidFile = Uri.parse(utilsFotos.getRootDirectory() + AppConstant.CARPETA_VIDEOS + File.separator + nombreVideo);
            VideoView videoView = (VideoView) findViewById(R.id.videoView);
            videoView.setVideoURI(vidFile);
            videoView.setKeepScreenOn(true);
            videoView.setMediaController(new MediaController(this));
            videoView.start();
            videoView.requestFocus();
        } else {
            Toast.makeText(getApplicationContext(), R.string.toast_error_video, Toast.LENGTH_SHORT).show();
        }
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

