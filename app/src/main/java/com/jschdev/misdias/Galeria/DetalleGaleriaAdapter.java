package com.jschdev.misdias.Galeria;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.jschdev.misdias.utils.TouchImageView;
import com.jschdev.mivideo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetalleGaleriaAdapter extends PagerAdapter {

    private Activity myActivity;
    private ArrayList<String> imagenes;
    private LayoutInflater inflater;

    // constructor
    public DetalleGaleriaAdapter(Activity activity, ArrayList<String> imagenes) {
        this.myActivity = activity;
        this.imagenes = imagenes;
    }

    @Override
    public int getCount() {
        return this.imagenes.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView img;
        Button btnClose;

        inflater = (LayoutInflater) myActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        img = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
        btnClose = (Button) viewLayout.findViewById(R.id.btnClose);


        Glide.with(myActivity.getBaseContext())
                //Cargamos la imagen sobre la que se esté iterando
                .load(imagenes.get(position))
                .placeholder(R.drawable.ic_load)
                .error(R.drawable.ic_load_error)
                        //Se aplica sobre la imagen (ImageView - se hizo referencia a "convertView")
                .into(img);

        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myActivity.finish();
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}
