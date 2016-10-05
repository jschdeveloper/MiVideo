package com.jschdev.misdias.Calendario;

/**
 * Created by jschdev on 26/11/2015.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isseiaoki.simplecropview.CropImageView;
import com.jschdev.misdias.Galeria.DetalleGaleriaActivity;
import com.jschdev.misdias.utils.AppConstant;
import com.jschdev.misdias.utils.UtilsFotos;
import com.jschdev.mivideo.R;
import com.squareup.picasso.Picasso;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import me.drakeet.materialdialog.MaterialDialog;


public class Calendario extends Fragment {
    GridView gridView;
    Calendar month;
    GregorianCalendar cal_month;
    MaterialIconView next, previous;
    AdaptadorCalendario adaptadorCalendario;
    MaterialIconView btn_takePhoto, btn_guardar, btn_delete, btn_importar,
            btn_editar, btn_rotate_left, btn_rotate_right, btn_editar_cancel,btn_open_photo;
    MaterialDialog mMaterialDialog;
    String rs;
    RelativeLayout relative_rotate;
    CropImageView imgDay;
    String fecha, ruta, fechaConFormato;
    ArrayList<String> imgs;
    TextView txt_0, txt_1, txt_2, txt_3, txt_4, txt_5, txt_6;
    private final int IDRESULT = 100;
    private final int RESULT_LOAD_IMG = 1;
    private UtilsFotos utilFotos;
    public static final int RESULT_OK = -1;
    CoordinatorLayout coordinatorLayout;
    private boolean isRunning = false;
    Activity myActivity;

    public static Calendario newInstance() {
        return new Calendario();
    }

    public Calendario() { /*Required empty public constructor*/ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendario, container, false);


        myActivity = getActivity();
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.cl_calendario);

        utilFotos = new UtilsFotos(getActivity());
        ruta = utilFotos.getRootDirectory();


        imgs = utilFotos.misFotos();

        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();


        TextView mes = (TextView) view.findViewById(R.id.mes);

        month = Calendar.getInstance();
        mes.setText(getMonthName(getActivity().getApplicationContext(), month));

        gridView = (GridView) view.findViewById(R.id.gridView);

        adaptadorCalendario = new AdaptadorCalendario(getActivity(), android.R.layout.simple_list_item_1, R.id.text_dia, cal_month, imgs);
        adaptadorCalendario.setContext(getActivity());

        next = (MaterialIconView) view.findViewById(R.id.next);
        previous = (MaterialIconView) view.findViewById(R.id.previous);

        next.setOnClickListener(refreshCalendar);
        previous.setOnClickListener(refreshCalendar);

        gridView.setAdapter(adaptadorCalendario);

        txt_0 = (TextView) view.findViewById(R.id.txt_0);
        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);
        txt_4 = (TextView) view.findViewById(R.id.txt_4);
        txt_5 = (TextView) view.findViewById(R.id.txt_5);
        txt_6 = (TextView) view.findViewById(R.id.txt_6);

        txt_0.setText(getResources().getString(R.string.domingo));
        txt_1.setText(getResources().getString(R.string.lunes));
        txt_2.setText(getResources().getString(R.string.martes));
        txt_3.setText(getResources().getString(R.string.miercoles));
        txt_4.setText(getResources().getString(R.string.jueves));
        txt_5.setText(getResources().getString(R.string.viernes));
        txt_6.setText(getResources().getString(R.string.sabado));

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                fecha = adaptadorCalendario.getItem(position);
                Intent i = new Intent(myActivity, DetalleGaleriaActivity.class);
                i.putExtra("idPhoto", fecha);
                i.putExtra("tipo", 1);
                i.putExtra("position", 0);
                Log.d("fecha", fecha);
                myActivity.startActivity(i);
                //set the image as wallpaper
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fecha = adaptadorCalendario.getItem(position);

                final Dialog dialog = new Dialog(getActivity());

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle(fecha);
                dialog.setCancelable(true);
                dialog.show();

                TextView tituloDialog = (TextView) dialog
                        .findViewById(R.id.tituloDialog);

                imgDay = (CropImageView) dialog.findViewById(R.id.myPhoto);
                btn_open_photo =(MaterialIconView) dialog.findViewById(R.id.btn_open_photo);
                btn_editar = (MaterialIconView) dialog.findViewById(R.id.btn_editar);
                btn_rotate_left = (MaterialIconView) dialog.findViewById(R.id.btn_rotate_left);
                btn_rotate_right = (MaterialIconView) dialog.findViewById(R.id.btn_rotate_right);
                btn_editar_cancel = (MaterialIconView) dialog.findViewById(R.id.btn_editar_cancel);
                relative_rotate = (RelativeLayout) dialog.findViewById(R.id.relative_rotate);

                btn_open_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(myActivity, DetalleGaleriaActivity.class);
                        i.putExtra("idPhoto", fecha);
                        i.putExtra("tipo", 1);
                        i.putExtra("position", 0);
                        Log.d("fecha", fecha);
                        myActivity.startActivity(i);
                    }
                });
                btn_rotate_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgDay.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                    }
                });

                btn_rotate_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgDay.rotateImage(CropImageView.RotateDegrees.ROTATE_180D);
                    }
                });

                btn_editar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        relative_rotate.setVisibility(View.VISIBLE);
                        btn_editar.setVisibility(View.INVISIBLE);
                        imgDay.setCropEnabled(true);
                        btn_editar_cancel.setVisibility(View.VISIBLE);
                    }
                });

                btn_editar_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btn_editar_cancel.setVisibility(View.GONE);
                        relative_rotate.setVisibility(View.GONE);
                        btn_editar.setVisibility(View.VISIBLE);
                        imgDay.setCropEnabled(false);
                    }
                });


                btn_guardar = (MaterialIconView) dialog.findViewById(R.id.btn_cancel);
                btn_importar = (MaterialIconView) dialog.findViewById(R.id.btn_importar);
                btn_delete = (MaterialIconView) dialog.findViewById(R.id.btn_delete);
                btn_takePhoto = (MaterialIconView) dialog.findViewById(R.id.btn_camera);


                imgDay.setCropMode(CropImageView.CropMode.RATIO_FREE);
                imgDay.setInitialFrameScale(1.0f);
                imgDay.setGuideShowMode(CropImageView.ShowMode.SHOW_ON_TOUCH);
                imgDay.setBackgroundColor(0xFFFFFFFB);
                imgDay.setOverlayColor(0xAA1C1C1C);
                imgDay.setFrameColor(getResources().getColor(R.color.primary_dark));
                imgDay.setHandleColor(getResources().getColor(R.color.primary_dark));
                imgDay.setGuideColor(getResources().getColor(R.color.primary_act));
                imgDay.setMinFrameSizeInDp(200);


                rs = adaptadorCalendario.getImg(fecha);

                /**
                 * colocamos la imagen que le corresponde
                 * */
                Picasso.with(getActivity().getApplicationContext())

                        //Cargamos la imagen sobre la que se esté iterando
                        .load(rs != "" ? rs : null)
                                //Imagen por defecto usada mientras se cargan las imágenes
                        .placeholder(R.drawable.ic_load)
                        .error(R.drawable.ic_load_error)
                        .fit().centerCrop()
                                //Se aplica sobre la imagen (ImageView - se hizo referencia a "convertView")
                        .into(imgDay);


                if (rs == "" || rs == null) {
                    mostrarElementos(false);
                } else {
                    mostrarElementos(true);
                }

                try {
                    Date fechaSinFormato = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(fecha);
                    fechaConFormato = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(fechaSinFormato);
                    tituloDialog.setText(fechaConFormato);

                    DateFormat formatActual = new SimpleDateFormat("dd/MM/yyyy");
                    Date dateActual = new Date();

                    /*if (comparaFechas(fechaConFormato, formatActual.format(dateActual))) {
                        btn_takePhoto.setVisibility(View.VISIBLE);
                    } else {
                        btn_takePhoto.setVisibility(View.GONE);
                    }*/


                } catch (ParseException e) {
                    e.printStackTrace();
                }

                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog = new MaterialDialog(dialog.getContext());
                        mMaterialDialog
                                .setTitle(R.string.titulo_confirmar_dialog)
                                .setMessage(R.string.msj_delete_imagen)
                                .setPositiveButton(R.string.txt_boton_ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (rs.length() > 0 && !rs.equals("")) {
                                            String archivoBorrar = rs.substring(rs.indexOf(AppConstant.CARPETA_FOTOS +
                                                    File.separator + AppConstant.CARPETA_FOTOS), rs.length());
                                            File file = new File(ruta + archivoBorrar);
                                            file.delete();
                                            adaptadorCalendario.borrarArchivo(rs);
                                            adaptadorCalendario.notifyDataSetChanged();

                                            Picasso.with(getActivity().getApplicationContext())
                                                    //Cargamos la imagen sobre la que se esté iterando
                                                    .load(R.drawable.ic_load)
                                                    .into(imgDay);
                                            mostrarMensaje(getResources().getString(R.string.toast_exito_borrar));
                                            restartGrid();

                                            mostrarElementos(false);
                                        }
                                        mMaterialDialog.dismiss();
                                    }
                                })
                                .setNegativeButton(R.string.txt_boton_cancel, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mMaterialDialog.dismiss();
                                    }
                                });

                        mMaterialDialog.show();
                    }
                });
                btn_importar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // Start the Intent
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                    }
                });


                btn_guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String filename = ruta + AppConstant.CARPETA_FOTOS + File.separator + createNameFile();
                        File file = new File(filename);
                        if (file.exists()) {
                            file.delete();
                        }
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(filename);

                            imgDay.getCroppedBitmap().compress(Bitmap.CompressFormat.JPEG, 100, out);
                            imgDay.setImageBitmap(imgDay.getCroppedBitmap());
                            mostrarMensaje(getResources().getString(R.string.toast_exito_imagen));
                            restartGrid();
                            refreshCalendar();

                        } catch (Exception e) {
                            e.printStackTrace();
                            mostrarMensaje(getResources().getString(R.string.toast_error_imagen));
                        } finally {
                            try {
                                if (out != null) {
                                    out.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        dialog.dismiss();
                    }
                });

                btn_takePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent iCamara = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        iCamara.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getFile(getActivity().getApplicationContext())));
                        startActivityForResult(iCamara, IDRESULT);
                    }
                });
            }
        });
        isRunning = true;
        return view;
    }

    public Bitmap addMarcaAgua(Bitmap src, String watermark) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(getActivity().getApplicationContext().getResources().getColor(R.color.primary));
        if (w > 2000 && h > 3000) {
            paint.setTextSize(42);
        } else {
            paint.setTextSize(22);
        }
        Typeface Lobster = Typeface.createFromAsset(getActivity().getAssets(),
                "Lobster-Regular.ttf");
        paint.setTypeface(Lobster);
        paint.setAntiAlias(true);
        canvas.drawText(getActivity().getApplication().getResources().getString(R.string.app_name) + " " + watermark, 40, 45, paint);

        return result;
    }

    public void mostrarElementos(boolean visible) {
        if (visible) {
            btn_delete.setVisibility(View.VISIBLE);
            btn_open_photo.setVisibility(View.VISIBLE);
            btn_editar.setVisibility(View.VISIBLE);
            imgDay.setCropEnabled(false);
            btn_guardar.setVisibility(View.VISIBLE);
            relative_rotate.setVisibility(View.GONE);

        } else {
            btn_editar.setVisibility(View.INVISIBLE);
            btn_open_photo.setVisibility(View.INVISIBLE);
            btn_delete.setVisibility(View.INVISIBLE);
            imgDay.setCropEnabled(false);
            btn_guardar.setVisibility(View.INVISIBLE);
            relative_rotate.setVisibility(View.GONE);
        }

    }


    public static String getMonthName(Context context, Calendar cal) {

        String result = "";
        int month = cal.get(Calendar.MONTH);

        try {
            result = context.getResources().getStringArray(R.array.nombre_meses)[month];
        } catch (ArrayIndexOutOfBoundsException e) {
            result = Integer.toString(month);
        }

        return result + " " + cal.get(Calendar.YEAR);
    }

    private File getFile(Context context) {
        final File path = new File(ruta, getResources().getString(R.string.nombre_carpeta_temporal));
        if (!path.exists()) {
            path.mkdir();
        }
        return new File(path, getResources().getString(R.string.nombre_archivo_temporal));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case IDRESULT:
                if (requestCode == IDRESULT && resultCode == RESULT_OK) {
                    final File file = getFile(getActivity());
                    try {
                        Bitmap captureBmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));
                        imgDay.setImageBitmap(addMarcaAgua(captureBmp, fechaConFormato));
                        mostrarElementos(true);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        mostrarMensaje(getResources().getString(R.string.toast_error_imagen));
                    } catch (IOException e) {
                        e.printStackTrace();
                        mostrarMensaje(getResources().getString(R.string.toast_error_imagen));
                    }
                }
                break;

            case RESULT_LOAD_IMG:
                try {
                    String imgDecodableString;
                    // When an Image is picked
                    if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                            && null != data) {
                        // Get the Image from data

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        // Get the cursor
                        Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imgDecodableString = cursor.getString(columnIndex);
                        cursor.close();
                        mostrarElementos(true);

                        imgDay.setImageBitmap(addMarcaAgua(BitmapFactory.decodeFile(imgDecodableString), fechaConFormato));
                    }
                } catch (Exception e) {
                    mostrarMensaje(getResources().getString(R.string.toast_error_imagen));
                }
                break;
        }

    }

    public boolean comparaFechas(String fecha1, String fecha2) {
        return fecha1.equals(fecha2);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isRunning) {
            restartGrid();
        }
    }


    public void restartGrid() {
        utilFotos = new UtilsFotos(getActivity());
        imgs = utilFotos.misFotos();
        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        adaptadorCalendario = new AdaptadorCalendario(getActivity(), android.R.layout.simple_list_item_1, R.id.text_dia, cal_month, imgs);
        gridView.setAdapter(adaptadorCalendario);
        month = Calendar.getInstance();
        TextView mes = (TextView) getActivity().findViewById(R.id.mes);
        mes.setText(getMonthName(getActivity(), this.month));
    }

    public void refreshCalendar() {
        adaptadorCalendario.refreshDays();
        adaptadorCalendario.notifyDataSetChanged();
        TextView mes = (TextView) getActivity().findViewById(R.id.mes);
        mes.setText(getMonthName(getActivity(), this.month));
    }


    private View.OnClickListener refreshCalendar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == previous) {
                if (month.get(Calendar.MONTH) == month
                        .getActualMinimum(Calendar.MONTH)) {
                    month.set((month.get(Calendar.YEAR) - 1),
                            month.getActualMaximum(Calendar.MONTH), 1);
                } else {
                    month.set(Calendar.MONTH, month.get(Calendar.MONTH) - 1);
                }
                setPreviousMonth();
                refreshCalendar();
            } else if (v == next) {
                if (month.get(Calendar.MONTH) == month
                        .getActualMaximum(Calendar.MONTH)) {
                    month.set((month.get(Calendar.YEAR) + 1),
                            month.getActualMinimum(Calendar.MONTH), 1);
                } else {
                    month.set(Calendar.MONTH, month.get(Calendar.MONTH) + 1);
                }
                setNextMonth();
                refreshCalendar();

            }
        }
    };

    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
                    cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1),
                    cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    public void mostrarMensaje(String msj) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, msj, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private String createNameFile() {
        return AppConstant.CARPETA_FOTOS + "_" + fecha + ".jpg";
    }
}