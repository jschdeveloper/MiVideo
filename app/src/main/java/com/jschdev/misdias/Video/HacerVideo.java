package com.jschdev.misdias.Video;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.jschdev.misdias.utils.AppConstant;
import com.jschdev.misdias.utils.BitmapScaler;
import com.jschdev.mivideo.R;

import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by jesussanchez on 06/11/2015.
 */
public class HacerVideo {

    private String nombreVideo;
    private CoordinatorLayout cl;
    private Activity myActivity;

    public HacerVideo(String nameVideo, CoordinatorLayout coordinatorLayout, Activity activity) {
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


    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }


    public void doVideo() {
        boolean creoVideo = false;
        File videoNuevo = null;

        try {

            File[] archivos = getRootDirectory(AppConstant.CARPETA_FOTOS).listFiles();
            if (archivos.length > 0) {
                int fps = 1;
                videoNuevo = this.getSDPathToFile(AppConstant.CARPETA_VIDEOS, nombreVideo);
                SequenceEncoder encoder = new SequenceEncoder(videoNuevo, fps);
                Arrays.sort(archivos);
                Bitmap fondoInicio = BitmapScaler.scaleToFill(BitmapFactory.decodeResource(myActivity.getResources(),
                        R.drawable.fondo_video), 800, 600);

                Bitmap fondoNegro = BitmapScaler.scaleToFill(BitmapFactory.decodeResource(myActivity.getResources(),
                        R.drawable.fondo_video), 800, 600);

                encoder.encodeNativeFrame(this.fromBitmap(fondoInicio), fps);
                for (File archivo : archivos) {
                    Bitmap img = decodeFile(archivo);
                    encoder.encodeNativeFrame(this.fromBitmap
                            (overlay(fondoNegro, BitmapScaler.scaleToFill(img, 800, 600)))
                            , fps);
                }

                encoder.finish();
                creoVideo = true;
            }

        } catch (IOException io) {
            io.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException ae) {
            videoNuevo.delete();
            mostrarMensaje(myActivity.getString(R.string.toast_ocurrio_problema));
            ae.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!creoVideo) {
                videoNuevo.delete();
            }
        }
    }

    public File getRootDirectory(String nombreCarpeta) {
        String targetPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + nombreCarpeta;

        File targetDirector = new File(targetPath);

        if (!targetDirector.exists()) {
            targetDirector.mkdir();
        }
        return targetDirector;
    }

    // get full SD path
    File getSDPathToFile(String nombreCarpeta, String nombreArchivo) {
        File parentDirectory = getRootDirectory(nombreCarpeta);
        return new File(parentDirectory.getAbsolutePath() + File.separator + nombreArchivo);// file;
    }

    // convert from Bitmap to Picture (jcodec native structure)
    public Picture fromBitmap(Bitmap src) {
        Picture dst = Picture.create((int) src.getWidth(), (int) src.getHeight(), ColorSpace.RGB);
        fromBitmap(src, dst);
        return dst;
    }

    public void fromBitmap(Bitmap src, Picture dst) {
        int[] dstData = dst.getPlaneData(0);
        int[] packed = new int[(src.getWidth()) * (src.getHeight())];


        src.getPixels(packed, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());

        for (int i = 0, srcOff = 0, dstOff = 0; i < src.getHeight(); i++) {
            for (int j = 0; j < src.getWidth(); j++, srcOff++, dstOff += 3) {
                int rgb = packed[srcOff];
                dstData[dstOff] = (rgb >> 16) & 0xff;
                dstData[dstOff + 1] = (rgb >> 8) & 0xff;
                dstData[dstOff + 2] = rgb & 0xff;
            }
        }
    }


    private Bitmap decodeFile(File img) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(img), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 500;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(img), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

}
