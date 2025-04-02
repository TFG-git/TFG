package com.example.tfg_inicial.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tfg_inicial.R;
import com.example.tfg_inicial.clases.Cartelera;
import com.example.tfg_inicial.clases.Pelea;

import java.util.ArrayList;

public class AdaptadorPersonalizadoPeleas extends BaseAdapter {

    private Context context;
    private ArrayList<Pelea> peleas;

    public AdaptadorPersonalizadoPeleas(Context context, ArrayList<Pelea> peleas) {
        this.context = context;
        this.peleas = peleas;
    }

    @Override
    public int getCount() { return peleas.size(); }

    @Override
    public Object getItem(int position) { return peleas.get(position); }

    @Override
    public long getItemId(int position) { return peleas.get(position).hashCode(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return null;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.item_peleaslistview, parent, false);

        Pelea pelea = peleas.get(position);
        TextView tvPeso = convertView.findViewById(R.id.tvPeso);
        TextView tvPeleador1 = convertView.findViewById(R.id.tvPeleador1);
        TextView tvPeleador2 = convertView.findViewById(R.id.tvPeleador2);

        tvPeso.setText(pelea.getPeleador1().getDivision());
        tvPeleador1.setText(pelea.getPeleador1().getNombrePeleador());
        tvPeleador2.setText(pelea.getPeleador2().getNombrePeleador());

        return convertView;
    }

}
