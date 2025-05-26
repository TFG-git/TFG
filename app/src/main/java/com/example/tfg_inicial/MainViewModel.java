package com.example.tfg_inicial;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tfg_inicial.clases.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<List<Cartelera>> carteleraLiveData = new MutableLiveData<>();

    private final List<Cartelera> carteleraCompleta = new ArrayList<>();
    private List<Cartelera> carteleraFiltrada = new ArrayList<>();

    private final int elementosPorPagina = 10;
    private boolean ordenAscendente = true;
    private String textoFiltro = "";

    public LiveData<List<Cartelera>> getCarteleraLiveData() {
        return carteleraLiveData;
    }

    public void cargarJSONDesdeTexto(String jsonCarteleras) {
        Type tipoLista = new TypeToken<List<Cartelera>>() {}.getType();
        List<Cartelera> listaDesdeJson = new Gson().fromJson(jsonCarteleras, tipoLista);

        if (listaDesdeJson != null) {
            carteleraCompleta.clear();
            carteleraCompleta.addAll(listaDesdeJson);
            aplicarFiltroYOrden("", true); // carga inicial sin filtro
        }

        Log.d("CARGADO", carteleraCompleta.toString());
    }

    public void aplicarFiltroYOrden(String texto, boolean ascendente) {
        this.textoFiltro = texto.trim().toLowerCase();
        this.ordenAscendente = ascendente;

        carteleraFiltrada = carteleraCompleta.stream()
                .filter(c -> c.getNombreCartelera() != null &&
                        c.getNombreCartelera().toLowerCase().contains(textoFiltro))
                .sorted((a, b) -> {
                    Date fechaA = a.getFechaParseada();
                    Date fechaB = b.getFechaParseada();
                    return ordenAscendente ? fechaA.compareTo(fechaB) : fechaB.compareTo(fechaA);
                })
                .collect(Collectors.toList());

        // Cargar los primeros elementos visibles
        List<Cartelera> inicial = obtenerRango(0, elementosPorPagina);
        carteleraLiveData.setValue(new ArrayList<>(inicial));
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

    public boolean isDataLoaded() {
        return !carteleraCompleta.isEmpty();
    }
}
