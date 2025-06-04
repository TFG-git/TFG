package com.example.tfg_inicial.fragments.inicio.peleadores;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.tfg_inicial.MainViewModel;
import com.example.tfg_inicial.R;
import com.example.tfg_inicial.adaptadores.AdaptadorPersonalizadoPeleadores;
import com.example.tfg_inicial.dialogs.PeleadorDialogFragment;

import java.util.ArrayList;

public class inicio_FragmentPeleadores extends Fragment {

    private MainViewModel viewModel;
    private RecyclerView recyclerView;
    private AdaptadorPersonalizadoPeleadores adapter;
    private EditText etBuscar;
    private ImageButton buttonBuscar, btnOrdenar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio__peleadores, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        recyclerView = view.findViewById(R.id.recyclerPeleadores);
        etBuscar = view.findViewById(R.id.etBuscarPeleador);
        buttonBuscar = view.findViewById(R.id.buttonBuscar);
        btnOrdenar = view.findViewById(R.id.btnOrdenarPeleadores);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdaptadorPersonalizadoPeleadores(viewModel.getPeleadoresAll(), peleador -> {
            // Abre el dialog del peleador
            PeleadorDialogFragment dialog = PeleadorDialogFragment.newInstance(peleador);
            dialog.show(getParentFragmentManager(), "detalle_peleador");
        });
        recyclerView.setAdapter(adapter);

        viewModel.getPeleadoresLiveData().observe(getViewLifecycleOwner(), peleadores -> {
            adapter.actualizarLista(peleadores);
        });

        // Buscar con botón (igual que carteleras)
        buttonBuscar.setOnClickListener(v -> {
            String texto = etBuscar.getText().toString();
            viewModel.aplicarFiltroYOrdenPeleadores(texto, MainViewModel.ORDEN_VALORACION_MAYOR);
        });

        // Buscar al escribir (opcional, igual que carteleras si quieres auto-filtrado)
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No filtrar aquí para que sólo se haga al pulsar el botón, o déjalo si quieres auto-filtrado.
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnOrdenar.setOnClickListener(v -> mostrarMenuOrdenar());

        return view;

    }

    private void mostrarMenuOrdenar() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), btnOrdenar);
        popupMenu.getMenu().add(0, MainViewModel.ORDEN_NOMBRE_AZ, 0, "Nombre (A-Z)");
        popupMenu.getMenu().add(0, MainViewModel.ORDEN_NOMBRE_ZA, 1, "Nombre (Z-A)");
        popupMenu.getMenu().add(0, MainViewModel.ORDEN_VALORACION_MAYOR, 2, "Valoración más alta");
        popupMenu.getMenu().add(0, MainViewModel.ORDEN_VALORACION_MENOR, 3, "Valoración más baja");

        popupMenu.setOnMenuItemClickListener(item -> {
            String texto = etBuscar.getText().toString();
            viewModel.aplicarFiltroYOrdenPeleadores(texto, item.getItemId());
            return true;
        });
        popupMenu.show();
    }
}