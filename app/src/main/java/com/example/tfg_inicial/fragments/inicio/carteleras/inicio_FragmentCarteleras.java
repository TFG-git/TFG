package com.example.tfg_inicial.fragments.inicio.carteleras;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

        // ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        recyclerView = view.findViewById(R.id.recyclerViewCarteleras);
        btnCargarMas = view.findViewById(R.id.buttonCargarMas);
        editBuscar = view.findViewById(R.id.editTextBuscar);
        btnBuscar = view.findViewById(R.id.buttonBuscar);
        btnOrdenar = view.findViewById(R.id.buttonOrdenar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Crear el adaptador inicial con lista vacía
        adaptador = new AdaptadorPersonalizadoCarteleras(new ArrayList<>(), cartelera -> {
            FragmentManager fm = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.fade_enter, R.anim.fade_exit); // transición suave
            ft.replace(R.id.llContenedorFragments, carteleras_FragmentPeleas.newInstance(cartelera));
            ft.addToBackStack(null);
            ft.commit();
        }, viewModel);
        recyclerView.setAdapter(adaptador);

        if (viewModel.isDataLoadedCarteleras()) {
            Log.d("Fragment", "ViewModel ya tiene datos, forzando recarga");
            viewModel.aplicarFiltroYOrdenEventos("", true, MainViewModel.ORDEN_MAS_ACTUALES); // usa filtro por defecto
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
            viewModel.aplicarFiltroYOrdenEventos(texto, true, MainViewModel.ORDEN_MAS_ACTUALES);
        });

        // Botón: Ordenar
        btnOrdenar.setOnClickListener(v -> {

            String texto = editBuscar.getText().toString();
            PopupMenu popupMenu = new PopupMenu(requireContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_ordenar_carteleras, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.orden_mas_actuales) {
                    viewModel.aplicarFiltroYOrdenEventos(texto, true, MainViewModel.ORDEN_MAS_ACTUALES);
                    return true;
                } else if (id == R.id.orden_mas_antiguos) {
                    viewModel.aplicarFiltroYOrdenEventos(texto, true, MainViewModel.ORDEN_MAS_ANTIGUOS);
                    return true;
                } else if (id == R.id.orden_valoracion_mayor) {
                    viewModel.aplicarFiltroYOrdenEventos(texto, true, MainViewModel.ORDEN_VALORACION_MAYOR);
                    return true;
                } else if (id == R.id.orden_valoracion_menor) {
                    viewModel.aplicarFiltroYOrdenEventos(texto, true, MainViewModel.ORDEN_VALORACION_MENOR);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }
}