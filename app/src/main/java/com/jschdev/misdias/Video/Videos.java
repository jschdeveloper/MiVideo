package com.jschdev.misdias.Video;

/**
 * Created by jschdev on 26/11/2015.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.jschdev.misdias.Video.newVideo.DetalleVideoActivity;
import com.jschdev.misdias.utils.AppConstant;
import com.jschdev.misdias.utils.UtilsFotos;
import com.jschdev.mivideo.R;

import java.io.File;
import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;

public class Videos extends Fragment implements TaskListener {

    //private String rutaVideos;
    private ListView lv;
    private ArrayList<String> allVideos;
    private ConexionAsincrona conexionAsincrona;
    private UtilsFotos utilsFotos;
    private FragmentActivity myActivity;
    private boolean bMenu;
    private ArrayAdapter<String> adapterVideos;
    CoordinatorLayout coordinatorLayout;
    private String rutaVideos;

    private ProgressDialog progressDialog;
    private boolean isTaskRunning = false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Videos.
     */
    public static Videos newInstance() {
        return new Videos();
    }

    public Videos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // If we are returning here from a screen orientation
        // and the AsyncTask is still working, re-create and display the
        // progress dialog.
        if (isTaskRunning) {
            progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.txt_cargando), getResources().getString(R.string.txt_generando_video));

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final TaskListener myTaskListener = this;
        myActivity = getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.videos, container, false);

        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);


        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.cl_video);
        TextView empty = (TextView) view.findViewById(R.id.empty1);
        FloatingActionButton doVideo = (FloatingActionButton) view.findViewById(R.id.doVideo);

        utilsFotos = new UtilsFotos(myActivity);
        rutaVideos = utilsFotos.getRootDirectory() + File.separator + AppConstant.CARPETA_VIDEOS;
        allVideos = new ArrayList<>();
        allVideos = getFiles(rutaVideos);


        adapterVideos = new ArrayAdapter<>(getContext(), R.layout.item_list, quitarExtension(allVideos));

        lv = (ListView) view.findViewById(R.id.listaVideos);
        lv.setLongClickable(true);
        lv.setAdapter(adapterVideos);
        registerForContextMenu(lv);
        lv.setEmptyView(empty);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(myActivity, DetalleVideoActivity.class);
                i.putExtra("position", position);
                myActivity.startActivity(i);


            }
        });
        /*
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent iVideo = new Intent(getActivity(), DetalleVideo.class);
                iVideo.putExtra(AppConstant.CLAVE_NOMBRE_VIDEO,
                        parent.getAdapter().getItem(position).toString() + AppConstant.MP4);
                getActivity().startActivity(iVideo);
            }
        });*/

        doVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText contentView = new EditText(myActivity);
                contentView.setTextColor(getResources().getColor(R.color.primary));
                final MaterialDialog mMaterialDialog = new MaterialDialog(myActivity);
                mMaterialDialog
                        .setContentView(contentView)
                        .setTitle(R.string.txt_nombre_video)
                        .setPositiveButton(R.string.txt_boton_ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (contentView.getText().toString() != null && contentView.getText().toString().length() > 0) {
                                    int size = utilsFotos.misFotos().size();
                                    if (size > 0) {
                                        mMaterialDialog.dismiss();
                                        if (!isTaskRunning) {
                                            conexionAsincrona = new ConexionAsincrona(myTaskListener);
                                            conexionAsincrona.execute(contentView.getText().toString() + AppConstant.MP4);
                                            conexionAsincrona = null;
                                            restartList();
                                        }
                                    } else {
                                        mostrarMensaje(getResources().getString(R.string.toast_sin_imagenes));
                                    }
                                } else {
                                    mostrarMensaje(getResources().getString(R.string.toast_nombre_requerido));
                                }
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
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         /*
        * Se comenta esta seccion porque no se ha implementado
        * el borrado masivo
        * */
        //menu.clear();
        //inflater.inflate(R.menu.menu_galeria, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.eliminar_galeria:
                mostrarMensaje("borrar video");
                return true;
        }
        return false;
    }


    public ArrayList<String> quitarExtension(ArrayList<String> allVideos) {
        ArrayList<String> videosSinExtension = new ArrayList<>();
        for (String video : allVideos) {
            videosSinExtension.add(video.substring(0, video.length() - 4));
        }
        return videosSinExtension;
    }

    public void restartList() {
        rutaVideos = utilsFotos.getRootDirectory() + File.separator + AppConstant.CARPETA_VIDEOS;
        allVideos = new ArrayList<>();
        allVideos = getFiles(rutaVideos);
        adapterVideos = new ArrayAdapter<>(getContext(), R.layout.item_list, quitarExtension(allVideos));
        lv.setAdapter(adapterVideos);

        //lv.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.item_list, allVideos));
    }

    public void borrarArchivo(String nombreVideo) {
        try {
            File file = new File(rutaVideos + File.separator + nombreVideo);
            if (file.delete()) {
                mostrarMensaje(getResources().getString(R.string.toast_exito_borrar));
                restartList();
            } else {
                mostrarMensaje(getResources().getString(R.string.toast_error_borrar));
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensaje(getResources().getString(R.string.toast_error_video));
        }
    }

    public void compartir(String rutaArchivo) {
        String type = "video/*";
        String mediaPath = utilsFotos.getRootDirectory() + AppConstant.CARPETA_VIDEOS + File.separator + rutaArchivo;
        createInstagramIntent(type, mediaPath);

    }

    private void createInstagramIntent(String type, String mediaPath) {

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);

        share.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name));
        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, getResources().getString(R.string.txt_seleccione_opcion)));
    }


    public ArrayList<String> getFiles(String directoryPath) {

        ArrayList<String> MyFiles = new ArrayList<>();
        File f = new File(directoryPath);

        if (!f.exists()) {
            f.mkdirs();
        }
        File[] files = f.listFiles();
        if (files.length == 0)
            return new ArrayList<>();
        else {
            for (File file : files) {
                if (utilsFotos.soportaArchivo(file.getAbsolutePath(), AppConstant.ID_VIDEO)) {
                    MyFiles.add(file.getName());
                }
            }
        }
        return MyFiles;
    }

    public void mostrarMensaje(String msj) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, msj, Snackbar.LENGTH_LONG)
                .setAction(AppConstant.RESULT_OK, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
        snackbar.show();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_video, menu);
        bMenu = true;
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (bMenu) {
            bMenu = false;
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            final String nombreArchivo = lv.getAdapter().getItem(info.position).toString() + AppConstant.MP4;

            if (getUserVisibleHint()) {

                if (item.getItemId() == R.id.menu_compartir) {
                    compartir(nombreArchivo);
                } else if (item.getItemId() == R.id.menu_eliminar) {
                    final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
                    mMaterialDialog
                            .setTitle(R.string.titulo_confirmar_dialog)
                            .setMessage(R.string.msj_delete_video)
                            .setPositiveButton(R.string.txt_boton_ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMaterialDialog.dismiss();
                                    borrarArchivo(nombreArchivo);
                                }
                            })
                            .setNegativeButton(R.string.txt_boton_cancel, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMaterialDialog.dismiss();
                                }
                            });

                    mMaterialDialog.show();

                } else {
                    return super.onContextItemSelected(item);
                }
            }
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onTaskStarted() {
        isTaskRunning = true;
        progressDialog = ProgressDialog.show(getActivity(),
                getResources().getString(R.string.txt_cargando),
                getResources().getString(R.string.txt_generando_video));
    }

    @Override
    public void onTaskFinished(String result) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            restartList();
        }
        isTaskRunning = false;
    }

    @Override
    public void onDetach() {
        // All dialogs should be closed before leaving the activity in order to avoid
        // the: Activity has leaked window com.android.internal.policy... exception
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDetach();
    }

    class ConexionAsincrona extends AsyncTask<String, String, String> {

        private final TaskListener listener;

        public ConexionAsincrona(TaskListener listener) {
            this.listener = listener;
        }

        String nombreVideo = "";

        @Override
        protected void onPreExecute() {
            listener.onTaskStarted();
        }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                nombreVideo = params[0];
                HacerVideo hacerVideo = new HacerVideo(nombreVideo, coordinatorLayout, myActivity);
                hacerVideo.doVideo();
                publishProgress("");
            } catch (Exception e) {
                File falloVideo = new File(rutaVideos + File.separator + nombreVideo);
                falloVideo.delete();
                mostrarMensaje(getResources().getString(R.string.toast_ocurrio_problema));
                e.printStackTrace();
            }
            return AppConstant.RESULT_OK;
        }


        @Override
        protected void onPostExecute(String result) {
            listener.onTaskFinished(result);
        }

        public void mostrarMensaje(String msj) {

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, msj, Snackbar.LENGTH_INDEFINITE)
                            //(tipoMensaje == AppConstant.SNACK_LONG) ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT)

                    .setAction(AppConstant.RESULT_OK, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });

            snackbar.show();
        }


    }
}