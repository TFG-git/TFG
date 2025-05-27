package com.example.tfg_inicial.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tfg_inicial.R;
import com.example.tfg_inicial.clases.Pelea;
import com.example.tfg_inicial.clases.Peleador;
import com.example.tfg_inicial.dialogs.PeleadorDialogFragment;
import com.example.tfg_inicial.fragments.inicio.carteleras.DetallePeleaFragment;

import java.util.List;

public class AdaptadorPersonalizadoPeleas extends RecyclerView.Adapter<AdaptadorPersonalizadoPeleas.ViewHolder> {

    private final List<Pelea> peleaList;

    public AdaptadorPersonalizadoPeleas(List<Pelea> peleaList) {
        this.peleaList = peleaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peleasrecyclerview, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pelea pelea = peleaList.get(position);
        holder.bind(pelea);
    }

    @Override
    public int getItemCount() {
        return peleaList != null ? peleaList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNombreRojo, textViewVs, textViewNombreAzul, textViewMetodo, textViewCategoria;
        ImageView imagePeleadorRojo, imagePeleadorAzul;
        ImageButton buttonLike, buttonDislike, buttonComentarios;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreRojo = itemView.findViewById(R.id.textViewNombreRojo);
            textViewVs = itemView.findViewById(R.id.textViewVs);
            textViewNombreAzul = itemView.findViewById(R.id.textViewNombreAzul);
            textViewMetodo = itemView.findViewById(R.id.textViewMetodo);
            textViewCategoria = itemView.findViewById(R.id.textViewCategoria);
            imagePeleadorRojo = itemView.findViewById(R.id.imagePeleadorRojo);
            imagePeleadorAzul = itemView.findViewById(R.id.imagePeleadorAzul);
            buttonLike = itemView.findViewById(R.id.buttonLike);
            buttonDislike = itemView.findViewById(R.id.buttonDislike);
            buttonComentarios = itemView.findViewById(R.id.buttonComentarios);
        }

        public void bind(Pelea pelea) {
            String nombreRojo = pelea.getPeleadorRojo() != null ? pelea.getPeleadorRojo().getNombreCompleto() : "Rojo";
            String nombreAzul = pelea.getPeleadorAzul() != null ? pelea.getPeleadorAzul().getNombreCompleto() : "Azul";

            textViewNombreRojo.setText(nombreRojo);
            textViewNombreAzul.setText(nombreAzul);
            textViewVs.setText("vs");
            textViewMetodo.setText("Método: " + (pelea.getMetodo() != null ? pelea.getMetodo() : "N/A"));
            textViewCategoria.setText("Categoría: " + (pelea.getCategoriaPeso() != null ? pelea.getCategoriaPeso() : "Desconocida"));

            imagePeleadorRojo.setImageResource(R.drawable.no_profile_image);
            imagePeleadorAzul.setImageResource(R.drawable.no_profile_image);

            imagePeleadorRojo.setOnClickListener(v -> {
                Peleador peleador = pelea.getPeleadorRojo();
                PeleadorDialogFragment dialog = PeleadorDialogFragment.newInstance(peleador);
                dialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "PeleadorDialog");
            });

            imagePeleadorAzul.setOnClickListener(v -> {
                Peleador peleador = pelea.getPeleadorAzul();
                PeleadorDialogFragment dialog = PeleadorDialogFragment.newInstance(peleador);
                dialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "PeleadorDialog");
            });

            //Clic para detalle Peleas
            itemView.setOnClickListener(v -> {
                FragmentManager fm = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fade_enter, R.anim.fade_exit); // transición suave
                ft.replace(R.id.llContenedorFragments, DetallePeleaFragment.newInstance(pelea));
                ft.addToBackStack(null);
                ft.commit();
            });

            buttonLike.setOnClickListener(v -> {
                // Like
            });

            buttonDislike.setOnClickListener(v -> {
                // Dislike
            });

            buttonComentarios.setOnClickListener(v -> {
                // Comentarios
            });
        }
    }
}
