package com.jschdev.misdias;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jschdev.misdias.utils.AppConstant;
import com.jschdev.mivideo.R;
import com.mikepenz.aboutlibraries.LibsBuilder;

public class ConfigActivity extends AppCompatActivity {

    public ActionBar actionBar;
    private Toolbar toolbar;
    private ArrayAdapter<String> adapterAyuda;
    CoordinatorLayout coordinatorLayout;
    private ListView lv;
    private String[] opciones;
    private Activity myActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        setContentView(R.layout.activity_configuracion);
        setupToolbar();
        opciones = getResources().getStringArray(R.array.lista_config);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_video);
        TextView empty = (TextView) findViewById(R.id.empty1);
        adapterAyuda = new ArrayAdapter<>(getApplicationContext(), R.layout.item_list, opciones);
        lv = (ListView) findViewById(R.id.listaConfig);
        lv.setLongClickable(true);
        lv.setAdapter(adapterAyuda);
        lv.setEmptyView(empty);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        new LibsBuilder()
                                //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                                .withAboutIconShown(true)
                                .withSlideInAnimation(true)
                                .withAboutVersionShownCode(true)
                                .withAboutSpecial1(getString(R.string.txt_contacto))
                                .withAboutSpecial1Description(AppConstant.CORREO)
                                .withAboutVersionShown(true)
                                .withLicenseShown(true)
                                .withAboutSpecial2(getString(R.string.txt_librerias))
                                .withAboutSpecial2Description(getString(R.string.lista_librerias))
                                .withAboutAppName(getString(R.string.app_name))
                                .withActivityTheme(R.style.ActivityHelp)
                                        //start the activity
                                .start(myActivity);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
