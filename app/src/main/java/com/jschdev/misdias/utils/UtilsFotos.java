package com.jschdev.misdias.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.jschdev.mivideo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by jschdeveloper on 09/11/2015.
 */
public class UtilsFotos {

    private Activity myActivity;
    private Context mContext;


    public UtilsFotos(Activity activity) {
        this.myActivity = activity;
    }

    public UtilsFotos(Context context, Activity activity) {
        this.myActivity = activity;
        this.mContext = context;
    }

    //public final static String CARPETA_FOTOS = "myAPP";
    //public final static String CARPETA_VIDEOS = "myAPPVideo";

    public int getIndex(String idPhoto, List<String> misFotos){
        int index = 0;
        for(int i = 0; i < misFotos.size(); i++){
            if(misFotos.get(i).contains(idPhoto)){
                index = i;
                return i;
            }
        }
        return index;
    }

    public File makeDirectory(String nameDirectory) {

        File targetDirector = new File(path(nameDirectory));

        if (!targetDirector.exists()) {
            targetDirector.mkdir();
            Toast.makeText(myActivity,
                    myActivity.getResources().getString(R.string.toast_creo_directorio),
                    Toast.LENGTH_SHORT).show();
        }
        return targetDirector;
    }


    public String path(String nameDirectory) {
        return Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + nameDirectory;
    }


    public ArrayList<String> misFotos() {

        ArrayList<String> images = new ArrayList<>();

        String targetPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + AppConstant.CARPETA_FOTOS;

        File targetDirector = new File(targetPath);

        if (!targetDirector.exists()) {
            targetDirector.mkdir();
            Toast.makeText(myActivity,
                    myActivity.getResources().getString(R.string.toast_creo_directorio),
                    Toast.LENGTH_SHORT).show();
        } else {

            File[] files = targetDirector.listFiles();

            for (File file : files) {
                String fileName = file.getName();
                if (soportaArchivo(fileName, AppConstant.ID_IMAGENES)) {
                    fileName = fileName.replace(':', '/');
                    fileName = fileName.replace('/', '_');
                    images.add("file://" + path(AppConstant.CARPETA_FOTOS) + File.separator + fileName);
                }
            }
            //}
        }
        Collections.sort(images);
        Collections.reverse(images);
        return images;
    }

    public ArrayList<String> misVideos() {

        ArrayList<String> videos = new ArrayList<>();

        String targetPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + AppConstant.CARPETA_VIDEOS;

        File targetDirector = new File(targetPath);

        if (!targetDirector.exists()) {
            targetDirector.mkdir();
            Toast.makeText(myActivity,
                    myActivity.getResources().getString(R.string.toast_creo_directorio),
                    Toast.LENGTH_SHORT).show();
        } else {

            File[] files = targetDirector.listFiles();

            for (File file : files) {
                String fileName = file.getName();
                if (soportaArchivo(fileName, AppConstant.ID_VIDEO)) {
                    fileName = fileName.replace(':', '/');
                    fileName = fileName.replace('/', '_');
                    videos.add("file://" + path(AppConstant.CARPETA_VIDEOS) + File.separator + fileName);
                }
            }
            //}
        }
        return videos;
    }


    public String getRootDirectory() {

        return Environment.getExternalStorageDirectory() + File.separator;
    }

    public boolean soportaArchivo(String filePath, int id_arhivo) {
        boolean isValido = false;
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
                filePath.length());

        if (id_arhivo == AppConstant.ID_IMAGENES) {
            if (AppConstant.IMAGENES_EXTN
                    .contains(ext.toLowerCase(Locale.getDefault()))) {
                isValido = true;
            } else {
                isValido = false;
            }

        } else if (id_arhivo == AppConstant.ID_VIDEO) {
            if (AppConstant.VIDEO_EXTN
                    .contains(ext.toLowerCase(Locale.getDefault()))) {
                isValido = true;
            } else {
                isValido = false;
            }
        }
        return isValido;
    }


    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }


}
