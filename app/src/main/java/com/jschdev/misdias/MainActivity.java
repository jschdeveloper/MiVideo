
package com.jschdev.misdias;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.jschdev.misdias.Calendario.Calendario;
import com.jschdev.misdias.Galeria.Galeria;
import com.jschdev.misdias.Video.Videos;
import com.jschdev.misdias.utils.AppConstant;
import com.jschdev.mivideo.R;

import me.drakeet.materialdialog.MaterialDialog;


public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private MyPagerAdapter pagerAdapter;
    public ActionBar actionBar;
    public Activity myActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myActivity = this;
        setupToolbar();
        setupTabLayout();
        setupTabIcons();

    }

    private void setupToolbar() {
        // Set a toolbar which will replace the action bar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show menu icon

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_home);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Setup the viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int idTable) {
                setActionBarTitle(idTable);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setupTabLayout() {

        // Setup the Tabs
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        // By using this method the tabs will be populated according to viewPager's count and
        // with the name from the pagerAdapter getPageTitle()
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        // This method ensures that tab selection events update the ViewPager and page changes update the selected tab.
        tabLayout.setupWithViewPager(viewPager);

    }


    private void setupTabIcons() {
        tabLayout.getTabAt(AppConstant.ID_TAB_CALENDARIO).setIcon(R.drawable.ic_calendar);
        tabLayout.getTabAt(AppConstant.ID_TAB_GALERIA).setIcon(R.drawable.ic_gallery);
        tabLayout.getTabAt(AppConstant.ID_TAB_VIDEO).setIcon(R.drawable.ic_video);
    }

    public void setActionBarTitle(int idTab) {
        switch (idTab) {
            case AppConstant.ID_TAB_CALENDARIO:
                getSupportActionBar().setTitle(getResources().getString(R.string.id_calendario));
                break;
            case AppConstant.ID_TAB_GALERIA:
                getSupportActionBar().setTitle(getResources().getString(R.string.id_galeria));
                break;
            case AppConstant.ID_TAB_VIDEO:
                getSupportActionBar().setTitle(getResources().getString(R.string.id_video));
                break;
            default:
                getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.opciones:
                Intent i = new Intent(myActivity, ConfigActivity.class);
                myActivity.startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_principal, menu);
        return true;
    }


    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case AppConstant.ID_TAB_CALENDARIO:
                    return Calendario.newInstance();
                case AppConstant.ID_TAB_GALERIA:
                    return Galeria.newInstance();
                case AppConstant.ID_TAB_VIDEO:
                    return Videos.newInstance();
                default:
                    return Calendario.newInstance();
            }
        }

        @Override
        public int getCount() {
            return AppConstant.NUM_TABS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
           /* switch (position) {

                case AppConstant.ID_TAB_CALENDARIO:
                    return getResources().getString(R.string.id_calendario);
                case AppConstant.ID_TAB_GALERIA:
                    return getResources().getString(R.string.id_galeria);
                case AppConstant.ID_TAB_VIDEO:
                    return getResources().getString(R.string.id_video);
                default:
                    return getResources().getString(R.string.id_calendario);
            }*/
            return null;
        }
    }


    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);

        } else {
            final MaterialDialog mMaterialDialog = new MaterialDialog(this);
            mMaterialDialog
                    .setTitle(R.string.titulo_confirmar_dialog)
                    .setMessage(R.string.msj_salida)
                    .setPositiveButton(R.string.txt_boton_ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                            finish();
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
    }


}
