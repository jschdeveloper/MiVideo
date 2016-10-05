package com.jschdev.misdias.Video.newVideo;

import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.jschdev.misdias.utils.AppConstant;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jschdev on 08/12/2015.
 */
public class mixAudioVideo {

    private String nombreVideo;
    private CoordinatorLayout cl;
    private Activity myActivity;

    public mixAudioVideo(String nameVideo, CoordinatorLayout coordinatorLayout, Activity activity) {
        this.nombreVideo = nameVideo;
        this.cl = coordinatorLayout;
        this.myActivity = activity;
    }


    public void mostrarMensaje(String msj) {
        Snackbar snackbar = Snackbar
                .make(cl, msj, Snackbar.LENGTH_LONG)
                .setAction(AppConstant.RESULT_OK, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
        snackbar.show();
    }
}
