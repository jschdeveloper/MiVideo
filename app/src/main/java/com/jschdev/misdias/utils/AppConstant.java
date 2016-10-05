package com.jschdev.misdias.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jschdev on 30/11/2015.
 */
public class AppConstant {

    public final static String CARPETA_FOTOS = "misdias";
    public final static String CARPETA_VIDEOS = "misvideos";

    // Number of columns of Grid View
    public static final int NUM_OF_COLUMNS = 3;
    public final static String MP4 = ".mp4";
    // Gridview image padding
    public static final int GRID_PADDING = 8; // in dp

    public static final int ID_VIDEO = 1;
    public static final int ID_IMAGENES = 2;


    public static final int ID_TAB_VIDEO = 2;
    public static final int ID_TAB_GALERIA = 1;
    public static final int ID_TAB_CALENDARIO = 0;

    public static final int NUM_TABS = 3;

    public static final String RESULT_OK = "OK";

    public final static String CLAVE_NOMBRE_VIDEO = "nombreVideo";
    public static final int SNACK_SHORT = 1;
    public static final int SNACK_LONG = 2;


    // SD card image directory
    public static final String PHOTO_ALBUM = "myAPP";

    // supported file formats
    public static final List<String> IMAGENES_EXTN = Arrays.asList("jpg", "jpeg", "png");
    public static final List<String> VIDEO_EXTN = Arrays.asList("mp4");
    public static final String CORREO = "jchdeveloper@gmail.com";


}