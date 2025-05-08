package com.example.tfg_inicial.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tfg_inicial.R;
import com.example.tfg_inicial.clases.Cartelera;

import java.util.ArrayList;

public class AdaptadorPersonalizadoCarteleras extends BaseAdapter {

    private Context context;

    private ArrayList<Cartelera> carteleras;

    public AdaptadorPersonalizadoCarteleras(Context context, ArrayList<Cartelera> carteleras) {
        this.context = context;
        this.carteleras = carteleras;
    }

    @Override
    public int getCount() { return carteleras.size(); }

    @Override
    public Object getItem(int position) { return carteleras.get(position); }

    @Override
    public long getItemId(int position) { return carteleras.get(position).hashCode(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return null;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_carteleraslistview, null);

        TextView tvNombreCartelera = view.findViewById(R.id.tvNombreCartelera);
        TextView tvLugar = view.findViewById(R.id.tvLugar);
        TextView tvFecha = view.findViewById(R.id.tvFecha);
        ImageView ivPeleador1 = view.findViewById(R.id.ivPeleador1);
        ImageView ivPeleador2 = view.findViewById(R.id.ivPeleador2);

        tvNombreCartelera.setText(carteleras.get(position).getNombreCartelera());
        tvLugar.setText(carteleras.get(position).getLugar());
        tvFecha.setText(carteleras.get(position).getFecha());

        return view;
    }
}
