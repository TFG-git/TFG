package com.example.tfg_inicial.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tfg_inicial.ComentariosFragment;
import com.example.tfg_inicial.InteraccionesManager;
import com.example.tfg_inicial.MainViewModel;
import com.example.tfg_inicial.R;
import com.example.tfg_inicial.clases.Cartelera;
import com.example.tfg_inicial.clases.EstadisticasPeleaPeleador;
import com.example.tfg_inicial.clases.Pelea;
import com.example.tfg_inicial.clases.Peleador;
import com.example.tfg_inicial.DescargarUrlCache;
import com.example.tfg_inicial.dialogs.PeleadorDialogFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AdaptadorPersonalizadoCarteleras extends RecyclerView.Adapter<AdaptadorPersonalizadoCarteleras.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Cartelera cartelera);
    }

    private final List<Cartelera> carteleraList;
    private final OnItemClickListener listener;
    private final MainViewModel viewModel;

    public AdaptadorPersonalizadoCarteleras(List<Cartelera> carteleraList, OnItemClickListener listener, MainViewModel viewModel) {
        this.carteleraList = carteleraList;
        this.listener = listener;
        this.viewModel = viewModel;
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
        holder.bind(cartelera, listener, viewModel);
        Log.d("Adaptador onBind", "Vinculando: " + cartelera.getNombreCartelera());
    }

    @Override
    public int getItemCount() {
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
        Log.d("Adaptador Carteleras", "Actualizando con " + nuevaLista.size() + " elementos");
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePeleadorRojo, imagePeleadorAzul;
        TextView nombre, fecha, ubicacion;
        ImageButton buttonLike, buttonDislike, buttonFavorito, buttonComentarios;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.textViewNombreEvento);
            fecha = itemView.findViewById(R.id.textViewFecha);
            ubicacion = itemView.findViewById(R.id.textViewUbicacion);
            imagePeleadorRojo = itemView.findViewById(R.id.imagePeleadorRojo);;
            imagePeleadorAzul = itemView.findViewById(R.id.imagePeleadorAzul);
            buttonLike = itemView.findViewById(R.id.buttonLike);
            buttonDislike = itemView.findViewById(R.id.buttonDislike);
            buttonFavorito = itemView.findViewById(R.id.buttonFavorito);
            buttonComentarios = itemView.findViewById(R.id.buttonComentarios);
        }

        public void bind(Cartelera cartelera, OnItemClickListener listener, MainViewModel viewModel) {
            nombre.setText(cartelera.getNombreCartelera());
            fecha.setText(cartelera.getFecha());
            ubicacion.setText(cartelera.getLugar());

            itemView.setOnClickListener(v -> {
                Log.d("Adaptador", "Clic en: " + cartelera.getNombreCartelera());
                listener.onItemClick(cartelera);
            });

            Pelea peleaEstelar = cartelera.getPeleas().get(0);

            String idPeleadorRojo = peleaEstelar.getPeleadorRojo().getIdPeleador();
            Peleador peleadorGlobalRojo = viewModel.buscarPeleadorPorId(idPeleadorRojo);

            DescargarUrlCache.getUrl(normalizarNombreCargarImagen(peleadorGlobalRojo), url -> {
                if (url != null) {
                    Glide.with(itemView.getContext())
                            .load(url)
                            .error(R.drawable.no_profile_image)
                            .circleCrop()
                            .into(imagePeleadorRojo);
                } else {
                    imagePeleadorRojo.setImageResource(R.drawable.no_profile_image);
                }
            });
            imagePeleadorRojo.setOnClickListener(v -> {
                Context context = v.getContext();
                PeleadorDialogFragment dialog = PeleadorDialogFragment.newInstance(peleadorGlobalRojo);

                if (context instanceof AppCompatActivity) {
                    AppCompatActivity activity = (AppCompatActivity) context;
                    dialog.show(activity.getSupportFragmentManager(), "detalle_peleador");
                }
            });

            String idPeleadorAzul = peleaEstelar.getPeleadorAzul().getIdPeleador();
            Peleador peleadorGlobalAzul = viewModel.buscarPeleadorPorId(idPeleadorAzul);
            DescargarUrlCache.getUrl(normalizarNombreCargarImagen(peleadorGlobalAzul), url -> {
                if (url != null) {
                    Glide.with(itemView.getContext())
                            .load(url)
                            .error(R.drawable.no_profile_image)
                            .circleCrop()
                            .into(imagePeleadorAzul);
                } else {
                    imagePeleadorAzul.setImageResource(R.drawable.no_profile_image);
                }
            });
            imagePeleadorAzul.setOnClickListener(v -> {
                Context context = v.getContext();
                PeleadorDialogFragment dialog = PeleadorDialogFragment.newInstance(peleadorGlobalAzul);

                if (context instanceof AppCompatActivity) {
                    AppCompatActivity activity = (AppCompatActivity) context;
                    dialog.show(activity.getSupportFragmentManager(), "detalle_peleador");
                }
            });

            //Logica Botones
            String carteleraId = String.valueOf(cartelera.getIdCartelera());

            InteraccionesManager.getEstados("carteleras", carteleraId, estado -> {
                buttonLike.setImageResource(estado.like ? R.drawable.ic_thumb_filled : R.drawable.ic_thumb);
                buttonDislike.setImageResource(estado.dislike ? R.drawable.ic_thumb_filled : R.drawable.ic_thumb);
                buttonFavorito.setImageResource(estado.favorito ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite);
            });

            buttonLike.setOnClickListener(v -> {
                InteraccionesManager.toggleLike("carteleras", carteleraId);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    InteraccionesManager.getEstados("carteleras", carteleraId, estadoActualizado -> {
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
                InteraccionesManager.toggleDislike("carteleras", carteleraId);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    InteraccionesManager.getEstados("carteleras", carteleraId, estadoActualizado -> {
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
                InteraccionesManager.toggleFavorito("carteleras", carteleraId);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    InteraccionesManager.getEstados("carteleras", carteleraId, estadoActualizado -> {
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
                ft.replace(R.id.llContenedorFragments, ComentariosFragment.newInstance("cartelera", carteleraId));
                ft.addToBackStack(null);
                ft.commit();
            });
        }

        private String normalizarNombreCargarImagen(Peleador peleador) {
            String nombreNormalizado = peleador.getNombreCompleto().strip().toLowerCase().replace(" ", "-");
            return "peleadores/" + nombreNormalizado + "-og.webp";
        }
    }
}
