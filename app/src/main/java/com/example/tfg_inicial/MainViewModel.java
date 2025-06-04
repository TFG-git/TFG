package com.example.tfg_inicial;


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
        }

    }

    private Map<String, Integer> mapaLikesCarteleras = new HashMap<>();
    private Map<String, Integer> mapaDislikesCarteleras = new HashMap<>();

    public void cargarRankingCarteleras() {
        DatabaseReference rankingRef = FirebaseDatabase.getInstance().getReference("Ranking").child("carteleras");
        rankingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mapaLikesCarteleras.clear();
                mapaDislikesCarteleras.clear();
                for (DataSnapshot carteleraSnapshot : snapshot.getChildren()) {
                    String id = carteleraSnapshot.getKey();
                    int likes = carteleraSnapshot.child("likes").getValue(Integer.class) != null ? carteleraSnapshot.child("likes").getValue(Integer.class) : 0;
                    int dislikes = carteleraSnapshot.child("dislikes").getValue(Integer.class) != null ? carteleraSnapshot.child("dislikes").getValue(Integer.class) : 0;
                    mapaLikesCarteleras.put(id, likes);
                    mapaDislikesCarteleras.put(id, dislikes);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private final Map<String, Integer> mapaLikesPeleadores = new HashMap<>();
    private final Map<String, Integer> mapaDislikesPeleadores = new HashMap<>();
    public void cargarRankingPeleadores() {
        DatabaseReference rankingRef = FirebaseDatabase.getInstance().getReference("Ranking").child("peleadores");
        rankingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mapaLikesPeleadores.clear();
                mapaDislikesPeleadores.clear();
                for (DataSnapshot peleadorSnapshot : snapshot.getChildren()) {
                    String id = peleadorSnapshot.getKey();
                    int likes = peleadorSnapshot.child("likes").getValue(Integer.class) != null ? peleadorSnapshot.child("likes").getValue(Integer.class) : 0;
                    int dislikes = peleadorSnapshot.child("dislikes").getValue(Integer.class) != null ? peleadorSnapshot.child("dislikes").getValue(Integer.class) : 0;
                    mapaLikesPeleadores.put(id, likes);
                    mapaDislikesPeleadores.put(id, dislikes);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void aplicarFiltroYOrdenEventos(String texto, boolean recargar, int tipoOrden) {
        this.textoFiltro = texto.trim().toLowerCase();
        this.recargar = recargar;

        Comparator<Cartelera> comparator;

        switch (tipoOrden) {
            case ORDEN_MAS_ACTUALES:
                comparator = Comparator.comparing(Cartelera::getFechaParseada).reversed();
                break;
            case ORDEN_MAS_ANTIGUOS:
                comparator = Comparator.comparing(Cartelera::getFechaParseada);
                break;
            case ORDEN_VALORACION_MAYOR:
                comparator = Comparator.comparingInt(this::getValoracion).reversed(); // Mayor a menor
                break;
            case ORDEN_VALORACION_MENOR:
                comparator = Comparator.comparingInt(this::getValoracion); // Menor a mayor
                break;
            default:
                comparator = Comparator.comparing(Cartelera::getFecha); // Por defecto antigüedad
                break;
        }

        carteleraFiltrada = carteleraCompleta.stream()
                .filter(c -> c.getNombreCartelera() != null &&
                        c.getNombreCartelera().toLowerCase().contains(textoFiltro))
                .sorted(comparator)
                .collect(Collectors.toList());

        // Cargar los primeros elementos visibles
        List<Cartelera> inicial = obtenerRango(0, elementosPorPagina);
        carteleraLiveData.setValue(new ArrayList<>(inicial));
    }
    // Calculo del ratio de likes/dislikes para un evento
    private int getValoracion(Cartelera cartelera) {
        // Suponiendo que tienes un Map<String, Integer> de likes y dislikes:
        int likes = mapaLikesCarteleras.getOrDefault(cartelera.getIdCartelera(), 0);
        int dislikes = mapaDislikesCarteleras.getOrDefault(cartelera.getIdCartelera(), 0);
        return likes - dislikes;
    }

    public void aplicarFiltroYOrdenPeleadores(String texto, int tipoOrden) {
        String textoFiltro = texto.trim().toLowerCase();

        Comparator<Peleador> comparator;

        switch (tipoOrden) {
            case ORDEN_NOMBRE_AZ:
                comparator = Comparator.comparing(Peleador::getNombreCompleto, String.CASE_INSENSITIVE_ORDER);
                break;
            case ORDEN_NOMBRE_ZA:
                comparator = Comparator.comparing(Peleador::getNombreCompleto, String.CASE_INSENSITIVE_ORDER).reversed();
                break;
            case ORDEN_VALORACION_MAYOR:
                comparator = Comparator.comparingInt(this::getValoracionPeleador).reversed();
                break;
            case ORDEN_VALORACION_MENOR:
                comparator = Comparator.comparingInt(this::getValoracionPeleador);
                break;
            default:
                comparator = Comparator.comparing(Peleador::getNombreCompleto, String.CASE_INSENSITIVE_ORDER);
                break;
        }

        peleadoresFiltrados = peleadoresAll.stream()
                .filter(p -> p.getNombreCompleto() != null && p.getNombreCompleto().toLowerCase().contains(textoFiltro))
                .sorted(comparator)
                .collect(Collectors.toList());

        peleadoresLiveData.setValue(new ArrayList<>(peleadoresFiltrados));
    }

    // Calculo del ratio de likes/dislikes para un peleador
    private int getValoracionPeleador(Peleador peleador) {
        int likes = mapaLikesPeleadores.getOrDefault(String.valueOf(peleador.getIdPeleador()), 0);
        int dislikes = mapaDislikesPeleadores.getOrDefault(String.valueOf(peleador.getIdPeleador()), 0);
        return likes - dislikes;
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
