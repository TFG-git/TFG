package com.example.tfg_inicial.adaptadores;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tfg_inicial.ComentariosFragment;
import com.example.tfg_inicial.DescargarUrlCache;
import com.example.tfg_inicial.InteraccionesManager;
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
        ImageButton buttonLike, buttonDislike, buttonFavorito, buttonComentarios;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePeleador = itemView.findViewById(R.id.imagePeleador);
            textViewNombrePeleador = itemView.findViewById(R.id.textViewNombrePeleador);
            textViewApodo = itemView.findViewById(R.id.textViewApodo);
            textViewNacionalidad = itemView.findViewById(R.id.textViewNacionalidad);
            buttonLike = itemView.findViewById(R.id.buttonLike);
            buttonDislike = itemView.findViewById(R.id.buttonDislike);
            buttonFavorito = itemView.findViewById(R.id.buttonFavorito);
            buttonComentarios = itemView.findViewById(R.id.buttonComentarios);
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
                    imagePeleador.setBackground(null);
                }
            });

            itemView.setOnClickListener(v -> listener.onItemClick(peleador));

            //Logica Botones
            String peleadorId = peleador.getIdPeleador();

            InteraccionesManager.getEstados("peleador", peleadorId, estado -> {
                buttonLike.setImageResource(estado.like ? R.drawable.ic_thumb_filled : R.drawable.ic_thumb);
                buttonDislike.setImageResource(estado.dislike ? R.drawable.ic_thumb_filled : R.drawable.ic_thumb);
                buttonFavorito.setImageResource(estado.favorito ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite);
            });

            buttonLike.setOnClickListener(v -> {
                InteraccionesManager.toggleLike("peleador", peleadorId);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    InteraccionesManager.getEstados("peleador", peleadorId, estadoActualizado -> {
                        buttonLike.setImageResource(estadoActualizado.like ? R.drawable.ic_thumb_filled : R.drawable.ic_thumb);
                        buttonDislike.setImageResource(estadoActualizado.dislike ? R.drawable.ic_thumb_filled : R.drawable.ic_thumb);

                        // Toast personalizado con icono del estado
                        Toast toast = new Toast(v.getContext());
                        LayoutInflater inflater = LayoutInflater.from(v.getContext());
                        View layout = inflater.inflate(R.layout.toast_custom_icon, null);
                        ImageView icon = layout.findViewById(R.id.toastIcon);
                        TextView text = layout.findViewById(R.id.toastText);
                        text.setText("Acción completada correctamente");
                        toast.setView(layout);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.show();
                    });
                }, 300);
            });

            buttonDislike.setOnClickListener(v -> {
                InteraccionesManager.toggleDislike("peleador", peleadorId);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    InteraccionesManager.getEstados("peleador", peleadorId, estadoActualizado -> {
                        buttonLike.setImageResource(estadoActualizado.like ? R.drawable.ic_thumb_filled : R.drawable.ic_thumb);
                        buttonDislike.setImageResource(estadoActualizado.dislike ? R.drawable.ic_thumb_filled : R.drawable.ic_thumb);

                        // Toast personalizado con icono del estado
                        Toast toast = new Toast(v.getContext());
                        LayoutInflater inflater = LayoutInflater.from(v.getContext());
                        View layout = inflater.inflate(R.layout.toast_custom_icon, null);
                        ImageView icon = layout.findViewById(R.id.toastIcon);
                        TextView text = layout.findViewById(R.id.toastText);
                        text.setText("Acción completada correctamente");
                        toast.setView(layout);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.show();
                    });
                }, 300);
            });

            buttonFavorito.setOnClickListener(v -> {
                InteraccionesManager.toggleFavorito("peleador", peleadorId);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    InteraccionesManager.getEstados("peleador", peleadorId, estadoActualizado -> {
                        buttonFavorito.setImageResource(estadoActualizado.favorito ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite);

                        // Toast personalizado con icono del estado
                        Toast toast = new Toast(v.getContext());
                        LayoutInflater inflater = LayoutInflater.from(v.getContext());
                        View layout = inflater.inflate(R.layout.toast_custom_icon, null);
                        ImageView icon = layout.findViewById(R.id.toastIcon);
                        TextView text = layout.findViewById(R.id.toastText);
                        text.setText("Acción completada correctamente");
                        toast.setView(layout);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.show();
                    });
                }, 300);
            });

            buttonComentarios.setOnClickListener(v -> {
                FragmentManager fm = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fade_enter, R.anim.fade_exit); // transición suave
                ft.replace(R.id.llContenedorFragments, ComentariosFragment.newInstance("peleador", peleadorId));
                ft.addToBackStack(null);
                ft.commit();
            });
        }
    }
}
