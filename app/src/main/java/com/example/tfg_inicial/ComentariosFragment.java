package com.example.tfg_inicial;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tfg_inicial.adaptadores.AdaptadorPersonalizadoComentarios;
import com.example.tfg_inicial.clases.Cartelera;
import com.example.tfg_inicial.clases.Comentario;
import com.example.tfg_inicial.clases.EstadisticasPeleaPeleador;
import com.example.tfg_inicial.clases.Pelea;
import com.example.tfg_inicial.clases.Peleador;
import com.example.tfg_inicial.clases.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class ComentariosFragment extends Fragment {

    private static final String ARG_TIPO = "tipo";
    private static final String ARG_ID_ENTIDAD = "id_entidad";

    private String tipo; // Cartelera / Pelea
    private String idEntidad;

    private AdaptadorPersonalizadoComentarios adapter;
    private List<Comentario> comentariosList = new ArrayList<>();
    private DatabaseReference usuariosRef;
    private Usuario usuarioActual;
    private String uid;
    private DatabaseReference comentariosRef;

    public static ComentariosFragment newInstance(String tipo, String idEntidad) {
        ComentariosFragment fragment = new ComentariosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TIPO, tipo);
        args.putString(ARG_ID_ENTIDAD, idEntidad);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comentarios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            tipo = getArguments().getString(ARG_TIPO);
            idEntidad = getArguments().getString(ARG_ID_ENTIDAD);
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerComentarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new AdaptadorPersonalizadoComentarios(requireContext(), comentariosList);
        recyclerView.setAdapter(adapter);

        EditText editComentario = view.findViewById(R.id.editComentario);
        ImageButton btnEnviarComentario = view.findViewById(R.id.btnEnviarComentario);
        ImageView avatarUsuario = view.findViewById(R.id.avatarUsuario);
        TextView textTitulo = view.findViewById(R.id.textTitulo);
        textTitulo.setText(mostrarInfoElementoComentario(tipo, idEntidad));

        // Avatar usuario
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios").child(uid);

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuarioActual = snapshot.getValue(Usuario.class);
                if (usuarioActual != null) {
                    DescargarUrlCache.getUrl(usuarioActual.getFotoPerfilUrl(), url -> {
                        if (url != null) {
                            Glide.with(requireContext())
                                    .load(url)
                                    .circleCrop()
                                    .into(avatarUsuario);
                        } else {
                            avatarUsuario.setImageResource(R.drawable.no_profile_image);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        comentariosRef = FirebaseDatabase.getInstance().getReference()
                .child("comentarios").child(tipo).child(idEntidad);

        comentariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comentariosList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Comentario c = ds.getValue(Comentario.class);
                    if (c != null) comentariosList.add(c);
                }
                // Ordena por timestamp ascendente
                comentariosList.sort((a, b) -> Long.compare(a.getTimestamp(), b.getTimestamp()));
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(comentariosList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        btnEnviarComentario.setOnClickListener(v -> {
            String texto = editComentario.getText().toString().trim();
            if (TextUtils.isEmpty(texto)) {
                Toast.makeText(requireContext(), "Escribe un comentario", Toast.LENGTH_SHORT).show();
                return;
            }
            uid = FirebaseAuth.getInstance().getCurrentUser() != null
                    ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                    : "anon";
            String idComentario = comentariosRef.push().getKey();
            Comentario nuevo = new Comentario(idComentario, uid, texto, System.currentTimeMillis());
            if (idComentario != null) {
                comentariosRef.child(idComentario).setValue(nuevo)
                        .addOnSuccessListener(aVoid -> editComentario.setText(""))
                        .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error al enviar", Toast.LENGTH_SHORT).show());
            }
        });

    }
    private String mostrarInfoElementoComentario(String tipo, String id) {
        String nombreElemento = "";

        MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        switch (tipo) {
            case "cartelera":
                List<Cartelera> carteleras = viewModel.getCarteleraCompleta();
                for (Cartelera c : carteleras) {
                    if (String.valueOf(c.getIdCartelera()).equals(id)) {
                        nombreElemento = "Comentarios | " + c.getNombreCartelera();
                        break;
                    }
                }
                break;
            case "pelea":
                List<Pelea> peleas = viewModel.getPeleasAll();
                for (Pelea p : peleas) {
                    if (String.valueOf(p.getIdPelea()).equals(id)) {
                        String nombreRojo = p.getPeleadorRojo().getNombre();
                        String nombreAzul = p.getPeleadorAzul().getNombre();
                        nombreElemento = "Comentarios | " + (nombreRojo != null ? nombreRojo : "Peleador Rojo") + " vs " +
                                (nombreAzul != null ? nombreAzul : "Peleador Azul");
                        break;
                    }
                }
                break;
            case "peleador":
                List<Peleador> peleadores = viewModel.getPeleadoresAll();
                for (Peleador pel : peleadores) {
                    if (pel.getIdPeleador().equals(id)) {
                        nombreElemento = "Comentarios | " + pel.getNombreCompleto();
                        break;
                    }
                }
                break;
            default:
                nombreElemento = "Elemento desconocido";
                break;
        }

        return nombreElemento;
    }

}
