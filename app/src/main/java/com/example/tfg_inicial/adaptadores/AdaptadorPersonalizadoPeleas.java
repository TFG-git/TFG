package com.example.tfg_inicial.adaptadores;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.tfg_inicial.DescargarUrlCache;
import com.example.tfg_inicial.InteraccionesManager;
import com.example.tfg_inicial.MainViewModel;
import com.example.tfg_inicial.R;
import com.example.tfg_inicial.clases.Cartelera;
import com.example.tfg_inicial.clases.EstadisticasPeleaPeleador;
import com.example.tfg_inicial.clases.Pelea;
import com.example.tfg_inicial.clases.Peleador;
import com.example.tfg_inicial.dialogs.PeleadorDialogFragment;
import com.example.tfg_inicial.fragments.inicio.carteleras.DetallePeleaFragment;

import java.util.List;

public class AdaptadorPersonalizadoPeleas extends RecyclerView.Adapter<AdaptadorPersonalizadoPeleas.ViewHolder> {

    private final List<Pelea> peleaList;
    private FragmentManager fragmentManager;
    private DialogFragment dialogFragment;
    private final MainViewModel viewModel;
    public AdaptadorPersonalizadoPeleas(List<Pelea> peleaList, MainViewModel viewModel) {
        this.peleaList = peleaList;
        this.viewModel = viewModel;
    }

    // Constructor extendido para instancia desde PeleadorDialogFragment
    public AdaptadorPersonalizadoPeleas(List<Pelea> peleas, FragmentManager fragmentManager, DialogFragment dialogFragment, MainViewModel viewModel) {
        this.peleaList = peleas;
        this.fragmentManager = fragmentManager;
        this.dialogFragment = dialogFragment;
        this.viewModel = viewModel;
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

    public void actualizarLista(List<Pelea> nuevaLista) {
        peleaList.clear();
        peleaList.addAll(nuevaLista);
        Log.d("Adaptador Peleas", "Actualizando con " + nuevaLista.size() + " elementos");
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNombreRojo, textViewVs, textViewNombreAzul, textViewMetodo, textViewCategoria;
        ImageView imagePeleadorRojo, imagePeleadorAzul;
        ImageButton buttonLike, buttonDislike, buttonFavorito, buttonComentarios;

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
            buttonFavorito = itemView.findViewById(R.id.buttonFavorito);
            buttonComentarios = itemView.findViewById(R.id.buttonComentarios);
        }

        public void bind(Pelea pelea) {
            String nombreRojo = pelea.getPeleadorRojo() != null ? pelea.getPeleadorRojo().getNombre() : "Rojo";
            String nombreAzul = pelea.getPeleadorAzul() != null ? pelea.getPeleadorAzul().getNombre() : "Azul";

            textViewNombreRojo.setText(nombreRojo);
            textViewNombreAzul.setText(nombreAzul);
            textViewVs.setText("vs");
            textViewMetodo.setText("Método: " + (pelea.getMetodo() != null ? pelea.getMetodo() : "N/A"));
            textViewCategoria.setText((pelea.getCategoriaPeso() != null ? pelea.getCategoriaPeso() : "Desconocida"));

            imagePeleadorRojo.setImageResource(R.drawable.no_profile_image);
            imagePeleadorAzul.setImageResource(R.drawable.no_profile_image);

            String idPeleadorRojo = pelea.getPeleadorRojo().getIdPeleador();
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

            String idPeleadorAzul = pelea.getPeleadorAzul().getIdPeleador();
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

            //Clic para detalle Peleas
            itemView.setOnClickListener(v -> {
                if (fragmentManager != null){
                    FragmentManager fm = fragmentManager;
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.fade_enter, R.anim.fade_exit); // transición suave
                    ft.replace(R.id.llContenedorFragments, DetallePeleaFragment.newInstance(pelea));
                    ft.addToBackStack(null);
                    ft.commit();

                }else{
                    FragmentManager fm = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.fade_enter, R.anim.fade_exit); // transición suave
                    ft.replace(R.id.llContenedorFragments, DetallePeleaFragment.newInstance(pelea));
                    ft.addToBackStack(null);
                    ft.commit();}
                if (dialogFragment != null) dialogFragment.dismiss();
            });

            //Logica Botones

            String peleaId = String.valueOf(pelea.getIdPelea());

            InteraccionesManager.getEstados("peleas", peleaId, estado -> {
                buttonLike.setImageResource(estado.like ? R.drawable.ic_thumb_filled : R.drawable.ic_thumb);
                buttonDislike.setImageResource(estado.dislike ? R.drawable.ic_thumb_filled : R.drawable.ic_thumb);
                buttonFavorito.setImageResource(estado.favorito ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite);
            });

            buttonLike.setOnClickListener(v -> {
                InteraccionesManager.toggleLike("peleas", peleaId);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    InteraccionesManager.getEstados("peleas", peleaId, estadoActualizado -> {
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
                InteraccionesManager.toggleDislike("peleas", peleaId);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    InteraccionesManager.getEstados("peleas", peleaId, estadoActualizado -> {
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
                InteraccionesManager.toggleFavorito("peleas", peleaId);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    InteraccionesManager.getEstados("peleas", peleaId, estadoActualizado -> {
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
        }

        private String normalizarNombreCargarImagen(Peleador peleador) {
            String nombreNormalizado = peleador.getNombreCompleto().strip().toLowerCase().replace(" ", "-");
            return "peleadores/" + nombreNormalizado + "-og.webp";
        }

    }
}
