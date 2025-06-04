package com.example.tfg_inicial.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tfg_inicial.DescargarUrlCache;
import com.example.tfg_inicial.R;
import com.example.tfg_inicial.clases.Peleador;

import java.util.List;

public class AdaptadorPersonalizadoPeleadores extends RecyclerView.Adapter<AdaptadorPersonalizadoPeleadores.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Peleador peleador);
    }

    private List<Peleador> lista;
    private final OnItemClickListener listener;

    public AdaptadorPersonalizadoPeleadores(List<Peleador> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_peleadoresrecyclerview, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Peleador peleador = lista.get(position);
        holder.bind(peleador, listener);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void actualizarLista(List<Peleador> nuevaLista) {
        lista = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePeleador;
        TextView textViewNombrePeleador, textViewApodo, textViewNacionalidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePeleador = itemView.findViewById(R.id.imagePeleador);
            textViewNombrePeleador = itemView.findViewById(R.id.textViewNombrePeleador);
            textViewApodo = itemView.findViewById(R.id.textViewApodo);
            textViewNacionalidad = itemView.findViewById(R.id.textViewNacionalidad);
        }

        public void bind(Peleador peleador, OnItemClickListener listener) {
            textViewNombrePeleador.setText(peleador.getNombreCompleto());
            textViewApodo.setText(peleador.getApodo() == null ? "" : peleador.getApodo());
            textViewNacionalidad.setText("");

            // Imagen
            DescargarUrlCache.getUrl("peleadores/" + peleador.getNombreCompleto().toLowerCase().replace(" ", "-") + "-og.webp", url -> {
                if (url != null) {
                    Glide.with(itemView.getContext())
                            .load(url)
                            .placeholder(R.drawable.no_profile_image)
                            .error(R.drawable.no_profile_image)
                            .circleCrop()
                            .into(imagePeleador);
                } else {
                    imagePeleador.setImageResource(R.drawable.no_profile_image);
                }
            });

            itemView.setOnClickListener(v -> listener.onItemClick(peleador));
        }
    }
}
