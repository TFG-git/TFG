package com.example.tfg_inicial.fragments.inicio.carteleras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_inicial.R;
import com.example.tfg_inicial.adaptadores.AdaptadorPersonalizadoPeleas;
import com.example.tfg_inicial.clases.Cartelera;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link carteleras_FragmentPeleas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class carteleras_FragmentPeleas extends Fragment {

    private RecyclerView recyclerViewPeleas;
    private static final String ARG_CARTELERA = "cartelera";

    private Cartelera cartelera;

    public static carteleras_FragmentPeleas newInstance(Cartelera cartelera) {
        carteleras_FragmentPeleas fragment = new carteleras_FragmentPeleas();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CARTELERA, cartelera);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cartelera = getArguments().getParcelable(ARG_CARTELERA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carteleras__peleas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewPeleas = view.findViewById(R.id.recyclerViewPeleas);
        recyclerViewPeleas.setLayoutManager(new LinearLayoutManager(getContext()));

        if (cartelera != null && cartelera.getPeleas() != null) {
            AdaptadorPersonalizadoPeleas adaptador = new AdaptadorPersonalizadoPeleas(cartelera.getPeleas());
            recyclerViewPeleas.setAdapter(adaptador);
        }
    }
}