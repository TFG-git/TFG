package com.example.tfg_inicial;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tfg_inicial.clases.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<List<Cartelera>> carteleraLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Peleador>> peleadoresLiveData = new MutableLiveData<>();

    private final List<Cartelera> carteleraCompleta = new ArrayList<>();
    private final List<Pelea> peleasAll = new ArrayList<>();
    private final List<Peleador> peleadoresAll = new ArrayList<>();
    private List<Cartelera> carteleraFiltrada = new ArrayList<>();
    private List<Peleador> peleadoresFiltrados = new ArrayList<>();

    private final int elementosPorPagina = 10;
    private boolean recargar = true;
    public static final int ORDEN_MAS_ACTUALES = 0; // Para eventos
    public static final int ORDEN_MAS_ANTIGUOS = 1; // Para eventos
    public static final int ORDEN_VALORACION_MAYOR = 2;
    public static final int ORDEN_VALORACION_MENOR = 3;
    public static final int ORDEN_NOMBRE_AZ = 4; // Para peleadores
    public static final int ORDEN_NOMBRE_ZA = 5; // Para peleadores
    private String textoFiltro = "";

    public LiveData<List<Cartelera>> getCarteleraLiveData() {
        return carteleraLiveData;
    }
    public LiveData<List<Peleador>> getPeleadoresLiveData() { return peleadoresLiveData; }
    public List<Cartelera> getCarteleraCompleta() {return carteleraCompleta;}
    public List<Pelea> getPeleasAll() {return peleasAll;}
    public List<Peleador> getPeleadoresAll() {return peleadoresAll;}

    public void cargarJSONEventosDesdeTexto(String jsonEventos) {
        Type tipoLista = new TypeToken<List<Cartelera>>() {}.getType();
        List<Cartelera> listaDesdeJson = new Gson().fromJson(jsonEventos, tipoLista);

        if (listaDesdeJson != null) {
            carteleraCompleta.clear();
            carteleraCompleta.addAll(listaDesdeJson);
            aplicarFiltroYOrdenEventos("", true, ORDEN_MAS_ACTUALES); // carga inicial sin filtro
        }

        // Extraer todas los peleas
        peleasAll.clear();
        for (Cartelera c : carteleraCompleta) {
            peleasAll.addAll(c.getPeleas());
        }

    }


    public void cargarJSONPeleadoresDesdeTexto(String jsonPeleadores) {
        Type tipoLista = new TypeToken<List<Peleador>>() {}.getType();
        List<Peleador> listaDesdeJson = new Gson().fromJson(jsonPeleadores, tipoLista);

        peleadoresAll.clear();
        if (listaDesdeJson != null) {
            peleadoresAll.addAll(listaDesdeJson);
            peleadoresFiltrados = new ArrayList<>(listaDesdeJson);
            peleadoresLiveData.setValue(peleadoresFiltrados);
            aplicarFiltroYOrdenPeleadores("", true, ORDEN_VALORACION_MAYOR);
        }

    }

    private Map<String, Integer> mapaLikesCarteleras = new HashMap<>();
    private Map<String, Integer> mapaDislikesCarteleras = new HashMap<>();

    public void cargarRankingCarteleras(Runnable onRankingLoaded) {
        DatabaseReference interaccionesRef = FirebaseDatabase.getInstance().getReference("interacciones");
        interaccionesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mapaLikesCarteleras.clear();
                mapaDislikesCarteleras.clear();

                for (DataSnapshot usuarioSnapshot : snapshot.getChildren()) {
                    DataSnapshot cartelerasSnapshot = usuarioSnapshot.child("carteleras");
                    for (DataSnapshot carteleraSnapshot : cartelerasSnapshot.getChildren()) {
                        String idCartelera = carteleraSnapshot.getKey();
                        Boolean like = carteleraSnapshot.child("like").getValue(Boolean.class);
                        Boolean dislike = carteleraSnapshot.child("dislike").getValue(Boolean.class);
                        if (like != null && like) {
                            int prev = mapaLikesCarteleras.containsKey(idCartelera) ? mapaLikesCarteleras.get(idCartelera) : 0;
                            mapaLikesCarteleras.put(idCartelera, prev + 1);
                        }
                        if (dislike != null && dislike) {
                            int prev = mapaDislikesCarteleras.containsKey(idCartelera) ? mapaDislikesCarteleras.get(idCartelera) : 0;
                            mapaDislikesCarteleras.put(idCartelera, prev + 1);
                        }
                    }
                }
                if (onRankingLoaded != null) onRankingLoaded.run();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // Calculo del ratio de likes/dislikes para un evento
    private int getValoracionCarteletas(Cartelera cartelera) {
        int likes = mapaLikesCarteleras.getOrDefault(String.valueOf(cartelera.getIdCartelera()), 0);
        int dislikes = mapaDislikesCarteleras.getOrDefault(String.valueOf(cartelera.getIdCartelera()), 0);
        return likes - dislikes;
    }

    public void aplicarFiltroYOrdenEventos(String texto, boolean recargar, int tipoOrden) {
        this.textoFiltro = texto.trim().toLowerCase();
        this.recargar = recargar;

        if (tipoOrden == ORDEN_VALORACION_MAYOR || tipoOrden == ORDEN_VALORACION_MENOR) {
            cargarRankingCarteleras(() -> ordenarYPublicarEventos(tipoOrden));
        } else {
            ordenarYPublicarEventos(tipoOrden);
        }
    }

    // Este metodo hace el filtrado, ordenado y publica el resultado.
    private void ordenarYPublicarEventos(int tipoOrden) {
        Comparator<Cartelera> comparator;
        switch (tipoOrden) {
            case ORDEN_MAS_ACTUALES:
                comparator = Comparator.comparing(Cartelera::getFechaParseada).reversed();
                break;
            case ORDEN_MAS_ANTIGUOS:
                comparator = Comparator.comparing(Cartelera::getFechaParseada);
                break;
            case ORDEN_VALORACION_MAYOR:
                comparator = Comparator.comparingInt(this::getValoracionCarteletas).reversed()
                        .thenComparing(Cartelera::getFechaParseada, Comparator.reverseOrder());
                break;
            case ORDEN_VALORACION_MENOR:
                comparator = Comparator.comparingInt(this::getValoracionCarteletas)
                        .thenComparing(Cartelera::getFechaParseada, Comparator.reverseOrder());
                break;
            default:
                comparator = Comparator.comparing(Cartelera::getFecha);
                break;
        }
        carteleraFiltrada = carteleraCompleta.stream()
                .filter(c -> c.getNombreCartelera() != null &&
                        c.getNombreCartelera().toLowerCase().contains(textoFiltro))
                .sorted(comparator)
                .collect(Collectors.toList());
        List<Cartelera> inicial = obtenerRango(0, elementosPorPagina);
        carteleraLiveData.setValue(new ArrayList<>(inicial));
    }

    private final Map<String, Integer> mapaLikesPeleadores = new HashMap<>();
    private final Map<String, Integer> mapaDislikesPeleadores = new HashMap<>();
    public void cargarRankingPeleadores(Runnable onRankingLoaded) {
        DatabaseReference rankingRef = FirebaseDatabase.getInstance().getReference("interacciones");
        rankingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mapaLikesPeleadores.clear();
                mapaDislikesPeleadores.clear();
                for (DataSnapshot usuarioSnapshot : snapshot.getChildren()) {
                    DataSnapshot peleadoresSnapshot = usuarioSnapshot.child("peleador");
                    for (DataSnapshot peleadorSnapshot : peleadoresSnapshot.getChildren()) {
                        String idPeleador = peleadorSnapshot.getKey();
                        Boolean like = peleadorSnapshot.child("like").getValue(Boolean.class);
                        Boolean dislike = peleadorSnapshot.child("dislike").getValue(Boolean.class);
                        if (like != null && like) {
                            int prev = mapaLikesPeleadores.containsKey(idPeleador) ? mapaLikesPeleadores.get(idPeleador) : 0;
                            mapaLikesPeleadores.put(idPeleador, prev + 1);
                        }
                        if (dislike != null && dislike) {
                            int prev = mapaDislikesPeleadores.containsKey(idPeleador) ? mapaDislikesPeleadores.get(idPeleador) : 0;
                            mapaDislikesPeleadores.put(idPeleador, prev + 1);
                        }
                    }
                }
                if (onRankingLoaded != null) onRankingLoaded.run();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    public void aplicarFiltroYOrdenPeleadores(String texto, boolean recargar, int tipoOrden) {
        this.textoFiltro = texto.trim().toLowerCase();
        this.recargar = recargar;

        if (tipoOrden == ORDEN_VALORACION_MAYOR || tipoOrden == ORDEN_VALORACION_MENOR) {
            cargarRankingPeleadores(() -> ordenarYPublicarPeleadores(tipoOrden));
        } else {
            ordenarYPublicarPeleadores(tipoOrden);
        }
    }

    // Calculo del ratio de likes/dislikes para un peleador
    private int getValoracionPeleador(Peleador peleador) {
        int likes = mapaLikesPeleadores.getOrDefault(String.valueOf(peleador.getIdPeleador()), 0);
        int dislikes = mapaDislikesPeleadores.getOrDefault(String.valueOf(peleador.getIdPeleador()), 0);
        return likes - dislikes;
    }
    private void ordenarYPublicarPeleadores(int tipoOrden) {
        Comparator<Peleador> comparator;
        switch (tipoOrden) {
            case ORDEN_NOMBRE_AZ:
                comparator = Comparator.comparing(Peleador::getNombreCompleto);
                break;
            case ORDEN_NOMBRE_ZA:
                comparator = Comparator.comparing(Peleador::getNombreCompleto).reversed();
                break;
            case ORDEN_VALORACION_MAYOR:
                comparator = Comparator.comparingInt(this::getValoracionPeleador).reversed()
                        .thenComparing(Peleador::getNombreCompleto);
                break;
            case ORDEN_VALORACION_MENOR:
                comparator = Comparator.comparingInt(this::getValoracionPeleador)
                        .thenComparing(Peleador::getNombreCompleto);
                break;
            default:
                comparator = Comparator.comparing(Peleador::getNombreCompleto);
                break;
        }

        peleadoresFiltrados = peleadoresAll.stream()
                .filter(p -> p.getNombreCompleto() != null && p.getNombreCompleto().toLowerCase().contains(textoFiltro))
                .sorted(comparator)
                .collect(Collectors.toList());

        peleadoresLiveData.setValue(new ArrayList<>(peleadoresFiltrados));
    }
    private List<Cartelera> obtenerRango(int inicio, int cantidad) {
        int fin = Math.min(inicio + cantidad, carteleraFiltrada.size());
        if (inicio >= fin) return new ArrayList<>();
        return new ArrayList<>(carteleraFiltrada.subList(inicio, fin));
    }

    public List<Cartelera> cargarMas() {
        List<Cartelera> actual = carteleraLiveData.getValue();
        if (actual == null) actual = new ArrayList<>();

        int yaMostrados = actual.size();
        if (yaMostrados >= carteleraFiltrada.size()) {
            return new ArrayList<>(); // Nada más que cargar
        }

        int fin = Math.min(yaMostrados + elementosPorPagina, carteleraFiltrada.size());
        List<Cartelera> nuevaPagina = carteleraFiltrada.subList(yaMostrados, fin);

        actual.addAll(nuevaPagina);
        carteleraLiveData.setValue(new ArrayList<>(actual));

        return nuevaPagina;
    }

    public boolean hayMas() {
        List<Cartelera> actual = carteleraLiveData.getValue();
        int yaMostrados = actual != null ? actual.size() : 0;
        return yaMostrados < carteleraFiltrada.size();
    }

    public boolean isDataLoadedCarteleras() {
        return !carteleraCompleta.isEmpty();
    }
    public boolean isDataLoadedPeleadores() {
        return !peleadoresAll.isEmpty();
    }

    // Metodo para Dialog Peleador
    public Peleador buscarPeleadorPorId(String idPeleador) {
        for (Peleador p : peleadoresAll) {
            if (p.getIdPeleador().equals(idPeleador)) {
                return p;
            }
        }
        return null;
    }

}
