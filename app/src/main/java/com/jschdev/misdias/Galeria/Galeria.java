package com.jschdev.misdias.Galeria;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.jschdev.misdias.utils.AppConstant;
import com.jschdev.misdias.utils.UtilsFotos;
import com.jschdev.mivideo.R;

import java.io.File;
import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by tcsadmin on 01/12/2015.
 */

public class Galeria extends Fragment {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Galeria.
     */

    private UtilsFotos utilsFotos;
    private GridGaleriaAdapter galleryAdapter;
    private GridView galleryGrid;
    private int columnWidth;
    private Activity myActivity;
    private MaterialDialog mMaterialDialog;
    private boolean bMenu;
    private Context mContext;
    CoordinatorLayout coordinatorLayout;
    ArrayList<String> allFotos = new ArrayList<>();

    public static Galeria newInstance() {
        return new Galeria();
    }

    public Galeria() { /* Required empty public constructor*/ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.galeria, container, false);

        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.cl_galeria);
        mContext = getActivity().getApplicationContext();
        myActivity = getActivity();
        utilsFotos = new UtilsFotos(getContext(), getActivity());


        TextView empty = (TextView) view.findViewById(R.id.empty1);
        galleryGrid = (GridView) view.findViewById(R.id.grid_view);


        // Initilizing Grid View
        InitilizeGridLayout();

        allFotos = utilsFotos.misFotos();
        // Gridview adapter
        galleryAdapter = new GridGaleriaAdapter(mContext, myActivity, allFotos, columnWidth);

        // setting grid view adapter
        galleryGrid.setAdapter(galleryAdapter);
        galleryGrid.setAdapter(galleryAdapter);
        registerForContextMenu(galleryGrid);
        galleryAdapter.notifyDataSetChanged();
        galleryGrid.setEmptyView(empty);

        galleryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(myActivity, DetalleGaleriaActivity.class);
                i.putExtra("position", position);
                i.putExtra("tipo", 0);
                myActivity.startActivity(i);


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
                mostrarMensaje("borrar galeria");
                return true;
        }
        return false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            galleryAdapter.notifyDataSetChanged();
            restartGrid();
        }
    }

    private void InitilizeGridLayout() {
        Resources r = getActivity().getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((utilsFotos.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

        galleryGrid.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        galleryGrid.setColumnWidth(columnWidth);
        galleryGrid.setStretchMode(GridView.NO_STRETCH);
        galleryGrid.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        galleryGrid.setHorizontalSpacing((int) padding);
        galleryGrid.setVerticalSpacing((int) padding);
    }

    private void compartirArchivo(String archivoCompartir) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.txt_compartir_texto) +  getResources().getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(archivoCompartir));
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.txt_seleccione_opcion)));
    }

    public void borrarImagenes(String archivoBorrar) {
        File file = new File(archivoBorrar.replace("file://", ""));
        file.delete();
        mostrarMensaje(getResources().getString(R.string.toast_exito_borrar));
        restartGrid();
    }

    public void restartGrid() {
        utilsFotos = new UtilsFotos(getActivity());
        allFotos = utilsFotos.misFotos();
        galleryAdapter = new GridGaleriaAdapter(getActivity().getApplicationContext(), getActivity(), allFotos, columnWidth);
        galleryGrid.setAdapter(galleryAdapter);
    }


    public void mostrarMensaje(String msj) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, msj, Snackbar.LENGTH_SHORT);

        snackbar.show();
        //Toast.makeText(getActivity(), msj, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(10, v.getId(), 0, getResources().getString(R.string.txt_menu_compartir));
        menu.add(10, v.getId(), 0, getResources().getString(R.string.txt_menu_eliminar));
        bMenu = true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (bMenu) {
            bMenu = false;
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            final String nombreArchivo = galleryGrid.getAdapter().getItem(info.position).toString();
            if (getUserVisibleHint()) {
                if (item.getTitle() == getResources().getString(R.string.txt_menu_compartir)) {
                    compartirArchivo(nombreArchivo);
                } else if (item.getTitle() == getResources().getString(R.string.txt_menu_eliminar)) {
                    final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
                    mMaterialDialog
                            .setTitle(R.string.titulo_confirmar_dialog)
                            .setMessage(R.string.msj_delete_imagen)
                            .setPositiveButton(R.string.txt_boton_ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMaterialDialog.dismiss();
                                    borrarImagenes(nombreArchivo);
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
                    return false;
                }
            }
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }
}
