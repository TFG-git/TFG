package com.example.tfg_inicial.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tfg_inicial.DescargarUrlCache;
import com.example.tfg_inicial.clases.Comentario;
import com.example.tfg_inicial.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tfg_inicial.clases.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdaptadorPersonalizadoComentarios extends RecyclerView.Adapter<AdaptadorPersonalizadoComentarios.ComentarioViewHolder> {

    private List<Comentario> listaComentarios;
    private Context context;
    private Map<String, Usuario> usuariosCache = new HashMap<>();
    public AdaptadorPersonalizadoComentarios(Context context, List<Comentario> listaComentarios) {
        this.context = context;
        this.listaComentarios = listaComentarios;
    }

    @NonNull
    @Override
    public ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comentariosrecyclerview, parent, false);
        return new ComentarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioViewHolder holder, int position) {
        Comentario comentario = listaComentarios.get(position);

        Log.d("Comentario - uid", comentario.getUidUsuario());
        Usuario usuario = usuariosCache.get(comentario.getUidUsuario());
        if (usuario != null) {
            Log.d("ID U", usuario.getIdUsuario());
            Log.d("Nombre U", usuario.getNombre());
            holder.textUsuario.setText(usuario.getNombre());
            DescargarUrlCache.getUrl(usuario.getFotoPerfilUrl(), url -> {
                if (url != null) {
                    Glide.with(context)
                            .load(url)
                            .error(R.drawable.no_profile_image)
                            .circleCrop()
                            .into(holder.avatar);
                } else {
                    holder.avatar.setImageResource(R.drawable.no_profile_image);
                }
            });
        } else {
            // Consultar a Firebase y guardar en cache
            DatabaseReference refUsuario = FirebaseDatabase.getInstance()
                    .getReference("usuarios")
                    .child(comentario.getUidUsuario());

            refUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Usuario user = snapshot.getValue(Usuario.class);
                    Log.d("ID U - null", user.getIdUsuario());
                    Log.d("Nombre U - null", user.getNombre());
                    if (user != null) {
                        usuariosCache.put(comentario.getUidUsuario(), user);
                        // Si la view todavía está visible, refrescamos
                        holder.textUsuario.setText(user.getNombre());
                        DescargarUrlCache.getUrl(user.getFotoPerfilUrl(), url -> {
                            if (url != null) {
                                Glide.with(context)
                                        .load(url)
                                        .error(R.drawable.no_profile_image)
                                        .circleCrop()
                                        .into(holder.avatar);
                            } else {
                                holder.avatar.setImageResource(R.drawable.no_profile_image);
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Nada, placeholder ya está puesto
                }
            });
        }

        holder.textComentario.setText(comentario.getTexto());

        String fecha = getTimeAgo(comentario.getTimestamp());
        holder.textFecha.setText(fecha);
    }

    @Override
    public int getItemCount() {
        return listaComentarios.size();
    }

    public static class ComentarioViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView textUsuario, textFecha, textComentario;
        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatarComentario);
            textUsuario = itemView.findViewById(R.id.textUsuario);
            textFecha = itemView.findViewById(R.id.textFecha);
            textComentario = itemView.findViewById(R.id.textComentario);
        }
    }

    private String getTimeAgo(long time) {
        long now = System.currentTimeMillis();
        long diff = now - time;
        long minutos = diff / (60 * 1000);
        if (minutos < 1) return "ahora";
        if (minutos < 60) return "hace " + minutos + " min";
        long horas = minutos / 60;
        if (horas < 24) return "hace " + horas + " h";
        long dias = horas / 24;
        return "hace " + dias + " d";
    }
}
