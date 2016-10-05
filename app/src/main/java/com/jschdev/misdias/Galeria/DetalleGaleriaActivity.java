package com.jschdev.misdias.Galeria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.jschdev.misdias.utils.UtilsFotos;
import com.jschdev.mivideo.R;

import java.util.ArrayList;
import java.util.List;

public class DetalleGaleriaActivity extends Activity {

    private UtilsFotos utilsFotos;
    private DetalleGaleriaAdapter adapter;
    private ViewPager viewPager;

    private static final String LOGTAG = "DetalleGaleriaActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_galeria);

        viewPager = (ViewPager) findViewById(R.id.pager);
        utilsFotos = new UtilsFotos(getApplicationContext(), this);

        Intent i = getIntent();

        int position = i.getIntExtra("position", 0);
        int tipo = i.getIntExtra("tipo", 0);
        String idPhoto = i.getStringExtra("idPhoto");

        ArrayList<String> fotos = utilsFotos.misFotos();
        adapter = new DetalleGaleriaAdapter(DetalleGaleriaActivity.this, fotos);
        viewPager.setAdapter(adapter);

        // displaying selected image first
        if(tipo == 1){
            int index = utilsFotos.getIndex(idPhoto, fotos);
            viewPager.setCurrentItem(index);
        }else{
            viewPager.setCurrentItem(position);
        }

    }
}
