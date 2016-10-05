package com.jschdev.misdias.Galeria;

/**
 * Created by tcsadmin on 30/11/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jschdev.mivideo.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class GridGaleriaAdapter extends BaseAdapter {

    private Activity myActivity;
    private Context mContext;
    private ArrayList<String> items = new ArrayList<String>();
    private int imageWidth;
    String fechaConFormato = "";

    public GridGaleriaAdapter(Context context, Activity activity, ArrayList<String> items,
                              int imageWidth) {
        this.myActivity = activity;
        this.items = items;
        this.mContext = context;
        this.imageWidth = imageWidth;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vista = inflater.inflate(R.layout.item_galeria, parent, false);

        ImageView img = (ImageView) vista.findViewById(R.id.imgDia);
        TextView fechaDia = (TextView) vista.findViewById(R.id.fechaDia);

        Glide.with(mContext)
                .load(items.get(position))
                .placeholder(R.drawable.ic_load_m)
                .error(R.drawable.ic_load_error)
                .override(imageWidth, imageWidth)
                .centerCrop()
                .into(img);


        String fullDia = items.get(position).substring(items.get(position).length() - 12, items.get(position).length()).replace(".jpg", "");

        try {
            Date fechaSinFormato = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(fullDia);
            fechaConFormato = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(fechaSinFormato);
            fechaDia.setText(fechaConFormato);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return vista;
    }
}
