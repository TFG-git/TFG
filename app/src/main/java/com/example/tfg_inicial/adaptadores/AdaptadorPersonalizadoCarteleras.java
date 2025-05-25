package com.example.tfg_inicial.adaptadores;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_inicial.R;
import com.example.tfg_inicial.clases.Cartelera;

import java.util.List;

public class AdaptadorPersonalizadoCarteleras extends RecyclerView.Adapter<AdaptadorPersonalizadoCarteleras.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Cartelera cartelera);
    }

    private final List<Cartelera> carteleraList;
    private final OnItemClickListener listener;

    public AdaptadorPersonalizadoCarteleras(List<Cartelera> carteleraList, OnItemClickListener listener) {
        this.carteleraList = carteleraList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cartelerasrecyclerview, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cartelera cartelera = carteleraList.get(position);
        holder.bind(cartelera, listener);
        Log.d("Adaptador onBind", "Vinculando: " + cartelera.getNombreCartelera());
    }

    @Override
    public int getItemCount() {
        Log.d("itemCount", carteleraList.size() + "");
        return carteleraList.size();
    }

    public void agregarMas(List<Cartelera> nuevasCarteleras) {
        int posicionInicio = carteleraList.size();
        carteleraList.addAll(nuevasCarteleras);
        notifyItemRangeInserted(posicionInicio, nuevasCarteleras.size());

        Log.d("Adaptador agregarMas", "Agregadas " + nuevasCarteleras.size() + " nuevas carteleras");
    }

    public void actualizarLista(List<Cartelera> nuevaLista) {
        carteleraList.clear();
        carteleraList.addAll(nuevaLista);
        Log.d("Adaptador actLista", "Actualizando con " + nuevaLista.size() + " elementos");
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, fecha, ubicacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.textViewNombreEvento);
            fecha = itemView.findViewById(R.id.textViewFecha);
            ubicacion = itemView.findViewById(R.id.textViewUbicacion);
        }

        public void bind(Cartelera cartelera, OnItemClickListener listener) {
            nombre.setText(cartelera.getNombreCartelera());
            fecha.setText(cartelera.getFecha());
            ubicacion.setText(cartelera.getLugar());

            itemView.setOnClickListener(v -> {
                Log.d("Adaptador", "Clic en: " + cartelera.getNombreCartelera());
                listener.onItemClick(cartelera);
            });
        }
    }
}
