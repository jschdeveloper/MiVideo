package com.jschdev.misdias.Calendario;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jschdev.mivideo.R;
import com.jschdev.misdias.utils.UtilsFotos;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class AdaptadorCalendario extends ArrayAdapter<String> {

    Context context;
    String[] dia;
    Calendar month;
    Calendar selectedDate;
    static List<String> day_string;
    String fullDate;
    String fechaActual;
    GregorianCalendar pmonth;
    public GregorianCalendar pmonthmaxset;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int mnthlength;
    String itemvalue;
    DateFormat df;
    ArrayList<String> imgs;
    UtilsFotos utilsFotos;

    public AdaptadorCalendario(Context context, int resource, int textViewResourceId, Calendar monthCalendar, ArrayList<String> imgs) {
        super(context, resource, textViewResourceId);

        this.month = monthCalendar;
        this.context = context;
        this.imgs = imgs;
        AdaptadorCalendario.day_string = new ArrayList<String>();
        Locale.setDefault(Locale.US);
        selectedDate = (GregorianCalendar) monthCalendar.clone();

        //set first day of week
        month.set(GregorianCalendar.DAY_OF_MONTH, Calendar.SUNDAY);

        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        fechaActual = df.format(selectedDate.getTime());
        refreshDays();

        month = monthCalendar;
        selectedDate = (Calendar) monthCalendar.clone();
    }

    public void borrarArchivo(String archivo) {
        imgs.remove(archivo);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setDia(String[] dia) {
        this.dia = dia;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vista = inflater.inflate(R.layout.vista_lista_personalizada, parent, false);

        fullDate = day_string.get(position).replace("-", "");

        String oneDay = day_string.get(position).split("-")[2].replaceFirst(
                "^0*", "");

        customDay(vista, oneDay, position);

        return vista;
    }

    public void customDay(View vista, String oneDay, int position) {
        TextView diaText = (TextView) vista.findViewById(R.id.text_dia);
        ImageView diaImg = (ImageView) vista.findViewById(R.id.img_dia);
        FrameLayout frame = (FrameLayout) vista.findViewById(R.id.contenedorDia);
        if ((Integer.parseInt(oneDay) > 1) && (position < firstDay)) {
            // color dias anteriores del mes
            diaText.setTextColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
        } else if ((Integer.parseInt(oneDay) < 7) && (position > 28)) {
            // color dias despues del mes
            diaText.setTextColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
        } else {
            // Todos los dias del mes
            diaText.setTextColor(Color.BLACK);
        }
        if (day_string.get(position).equals(fechaActual)) {
            // Pone de color el dia actual
            diaText.setTextColor(Color.WHITE);
            frame.setBackgroundColor(getContext().getResources().getColor(R.color.primary_act));
        } else {
            frame.setBackgroundColor(getContext().getResources().getColor(android.R.color.white));
        }
        String fecha = day_string.get(position).replace("-", "");
        getImg(fecha);

        String rs = getImg(fecha);
        Picasso.with(context)
                //Cargamos la imagen sobre la que se esté iterando
                .load(rs != "" ? getImg(fecha) : null)
                        //Imagen por defecto usada mientras se cargan las imágenes
                .fit().centerCrop()
                //Se aplica sobre la imagen (ImageView - se hizo referencia a "convertView")
                .into(diaImg);

        diaText.setText(oneDay);

    }

    public String getImg(String fecha) {
        String index = "";
        for (String img : imgs) {
            if (img.contains(fecha)) {
                index = img;
                break;
            }
        }

        return index;

    }

    public int getSizeImgs() {
        return imgs.size();
    }

    public int getCount() {
        return day_string.size();
    }

    public String getItem(int position) {
        return day_string.get(position).replace("-", "");
    }


    public void refreshDays() {
        day_string.clear();
        Locale.setDefault(Locale.US);
        pmonth = (GregorianCalendar) month.clone();
        // mes start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        // finding number of weeks in current mes.
        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP(); // previous mes maximum day 31,30....
        calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
        /**
         * Calendar instance for getting a complete gridview including the three
         * mes's (previous,current,next) dates.
         */
        pmonthmaxset = (GregorianCalendar) pmonth.clone();
        /**
         * setting the start date as previous mes's required date.
         */
        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

        /**
         * filling calendar gridview.
         */
        for (int n = 0; n < mnthlength; n++) {
            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            day_string.add(itemvalue);

        }
    }

    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }
}
