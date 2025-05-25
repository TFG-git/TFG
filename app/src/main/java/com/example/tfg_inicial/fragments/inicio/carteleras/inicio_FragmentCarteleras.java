package com.example.tfg_inicial.fragments.inicio.carteleras;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_inicial.MainViewModel;
import com.example.tfg_inicial.R;
import com.example.tfg_inicial.adaptadores.AdaptadorPersonalizadoCarteleras;
import com.example.tfg_inicial.clases.Cartelera;

import java.util.ArrayList;
import java.util.List;

public class inicio_FragmentCarteleras extends Fragment {

    private MainViewModel viewModel;
    private AdaptadorPersonalizadoCarteleras adaptador;

    private RecyclerView recyclerView;
    private Button btnCargarMas;
    private EditText editBuscar;
    private ImageButton btnBuscar, btnOrdenar;

    private boolean ordenAscendente = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio__carteleras, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewCarteleras);
        btnCargarMas = view.findViewById(R.id.buttonCargarMas);
        editBuscar = view.findViewById(R.id.editTextBuscar);
        btnBuscar = view.findViewById(R.id.buttonBuscar);
        btnOrdenar = view.findViewById(R.id.buttonOrdenar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Crear el adaptador inicial con lista vacía
        adaptador = new AdaptadorPersonalizadoCarteleras(new ArrayList<>(), cartelera -> {
            Fragment fragment = carteleras_FragmentPeleas.newInstance(cartelera);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.llContenedorFragments, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(adaptador);

        // ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        if (viewModel.isDataLoaded()) {
            Log.d("Fragment", "ViewModel ya tiene datos, forzando recarga");
            viewModel.aplicarFiltroYOrden("", true); // usa filtro por defecto
        }

        // Observar cambios en los datos
        viewModel.getCarteleraLiveData().observe(getViewLifecycleOwner(), lista -> {
            Log.d("Fragment", "Lista observada: " + lista.size());
            adaptador.actualizarLista(lista);
            btnCargarMas.setVisibility(viewModel.hayMas() ? View.VISIBLE : View.GONE);
        });


        // Botón: Cargar más
        btnCargarMas.setOnClickListener(v -> {
            List<Cartelera> nuevas = viewModel.cargarMas();
            if (!viewModel.hayMas()) {
                btnCargarMas.setVisibility(View.GONE);
            }
        });

        // Botón: Buscar
        btnBuscar.setOnClickListener(v -> {
            String texto = editBuscar.getText().toString();
            viewModel.aplicarFiltroYOrden(texto, ordenAscendente);
        });

        // Botón: Ordenar
        btnOrdenar.setOnClickListener(v -> {
            ordenAscendente = !ordenAscendente;
            String texto = editBuscar.getText().toString();
            viewModel.aplicarFiltroYOrden(texto, ordenAscendente);
        });
    }
}