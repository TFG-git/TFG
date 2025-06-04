package com.example.tfg_inicial;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class InteraccionesManager {

    public static class Estado {
        public boolean like = false;
        public boolean dislike = false;
        public boolean favorito = false;
    }

    public interface EstadoCallback {
        void onResult(Estado estado);
    }

    private static DatabaseReference ref() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        return ref.child("interacciones").child(uid);
    }

    public static void getEstados(String tipo, String id, EstadoCallback callback) {
        ref().child(tipo).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Estado estado = new Estado();
                if (snapshot.exists()) {
                    Boolean like = snapshot.child("like").getValue(Boolean.class);
                    Boolean dislike = snapshot.child("dislike").getValue(Boolean.class);
                    Boolean favorito = snapshot.child("favorito").getValue(Boolean.class);
                    estado.like = like != null && like;
                    estado.dislike = dislike != null && dislike;
                    estado.favorito = favorito != null && favorito;
                }
                callback.onResult(estado);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(new Estado());
            }
        });
    }

    public static void toggleLike(String tipo, String id) {
        Log.d("FirebaseTest 1", "toggleLike ejecutado para: "+tipo+"-"+id);
        ref().child(tipo).child(id).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Map<String, Object> data = (Map<String, Object>) currentData.getValue();
                if (data == null) data = new HashMap<>();
                boolean nuevoLike = !(data.containsKey("like") && (Boolean) data.get("like"));
                data.put("like", nuevoLike);
                if (nuevoLike) data.put("dislike", false); // Exclusividad
                currentData.setValue(data);
                return Transaction.success(currentData);
            }
            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot snapshot) {
                if (error != null) {
                    Log.e("FirebaseTest 1", "Error al escribir en Firebase: " + error.getMessage());
                } else {
                    Log.d("FirebaseTest 1", "Transacción completada. Committed: " + committed);
                }
            }
        });
    }

    public static void toggleDislike(String tipo, String id) {
        Log.d("FirebaseTest 2", "toggleDislike ejecutado para: "+tipo+"-"+id);
        ref().child(tipo).child(id).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Map<String, Object> data = (Map<String, Object>) currentData.getValue();
                if (data == null) data = new HashMap<>();
                boolean nuevoDislike = !(data.containsKey("dislike") && (Boolean) data.get("dislike"));
                data.put("dislike", nuevoDislike);
                if (nuevoDislike) data.put("like", false); // Exclusividad
                currentData.setValue(data);
                return Transaction.success(currentData);
            }
            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot snapshot) {
                if (error != null) {
                    Log.e("FirebaseTest 2", "Error al escribir en Firebase: " + error.getMessage());
                } else {
                    Log.d("FirebaseTest 2", "Transacción completada. Committed: " + committed);
                }
            }
        });
    }

    public static void toggleFavorito(String tipo, String id) {
        Log.d("FirebaseTest 3", "toggleFavorito ejecutado para: "+tipo+"-"+id);
        ref().child(tipo).child(id).child("favorito")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean nuevoEstado = !(snapshot.getValue(Boolean.class) != null && snapshot.getValue(Boolean.class));
                        ref().child(tipo).child(id).child("favorito").setValue(nuevoEstado).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("FirebaseTest", "Favorito guardado correctamente.");
                            } else {
                                Log.e("FirebaseTest", "Error al guardar favorito: " + task.getException());
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    // Para la sección de guardados
    public interface FavoritosCallback {
        void onResult(Set<Integer> favoritosIds);
    }

    public static void getFavoritos(String tipo, FavoritosCallback callback) {
        ref().child(tipo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<Integer> favoritos = new HashSet<>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Boolean esFavorito = item.child("favorito").getValue(Boolean.class);
                    if (esFavorito != null && esFavorito) {
                        try {
                            favoritos.add(Integer.parseInt(item.getKey()));
                        } catch (NumberFormatException e) {
                            Log.e("Error al parsear", "No se pudo parsear el favorito: " + item.getKey());
                        }
                    }
                }
                callback.onResult(favoritos);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(new HashSet<>());
            }
        });
    }
}