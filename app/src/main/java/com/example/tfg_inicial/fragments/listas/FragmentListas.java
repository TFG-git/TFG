package com.example.tfg_inicial.fragments.listas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfg_inicial.InteraccionesManager;
import com.example.tfg_inicial.MainViewModel;
import com.example.tfg_inicial.R;
import com.example.tfg_inicial.adaptadores.AdaptadorPersonalizadoCarteleras;
import com.example.tfg_inicial.adaptadores.AdaptadorPersonalizadoPeleas;
import com.example.tfg_inicial.clases.*;
import com.example.tfg_inicial.fragments.inicio.FragmentInicio;
import com.example.tfg_inicial.fragments.inicio.carteleras.carteleras_FragmentPeleas;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListas extends Fragment {

    private RecyclerView rvCarteleras, rvPeleas, rvPeleadores;
    private AdaptadorPersonalizadoCarteleras adaptadorCarteleras;
    private AdaptadorPersonalizadoPeleas adaptadorPeleas;
    //private AdaptadorPersonalizadoPeleadores adaptadorPeleadores;

    private MainViewModel viewModel;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentListas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentInicio.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListas newInstance(String param1, String param2) {
        FragmentListas fragment = new FragmentListas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listas, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        rvCarteleras = v.findViewById(R.id.recyclerViewCarteleras);
        rvPeleas = v.findViewById(R.id.recyclerViewPeleas);
        rvPeleadores = v.findViewById(R.id.recyclerViewPeleadores);

        rvCarteleras.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvPeleas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvPeleadores.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Iniciar los adaptadores con listas vacías por defecto
        adaptadorCarteleras = new AdaptadorPersonalizadoCarteleras(new ArrayList<>(), cartelera -> {
            FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.fade_enter, R.anim.fade_exit); // transición suave
            ft.replace(R.id.llContenedorFragments, carteleras_FragmentPeleas.newInstance(cartelera));
            ft.addToBackStack(null);
            ft.commit();
        }, viewModel);

        adaptadorPeleas = new AdaptadorPersonalizadoPeleas(new ArrayList<>(), viewModel);
        //adaptadorPeleadores = new AdaptadorPersonalizadoPeleadores(new ArrayList<>());

        rvCarteleras.setAdapter(adaptadorCarteleras);
        rvPeleas.setAdapter(adaptadorPeleas);
        //rvPeleadores.setAdapter(adaptadorPeleadores);

        cargarFavoritosUsuario();

        return v;
    }

    private void cargarFavoritosUsuario() {
        // Carteleras
        InteraccionesManager.getFavoritos("carteleras", idsFavoritos -> {
            List<Cartelera> cartelerasFavs = new ArrayList<>();
            for (Cartelera c : viewModel.getCarteleraCompleta()) {
                if (idsFavoritos.contains(c.getIdCartelera())) {
                    cartelerasFavs.add(c);
                }
            }
            adaptadorCarteleras.actualizarLista(cartelerasFavs);
        });

        // Peleas
        InteraccionesManager.getFavoritos("peleas", idsFavoritos -> {
            List<Pelea> peleasFavs = new ArrayList<>();
            for (Pelea p : viewModel.getPeleasAll()) {
                if (idsFavoritos.contains(p.getIdPelea())) {
                    peleasFavs.add(p);
                }
            }
            adaptadorPeleas.actualizarLista(peleasFavs);
        });

        // Peleadores
        InteraccionesManager.getFavoritos("peleadores", idsFavoritos -> {
            List<Peleador> peleadoresFavs = new ArrayList<>();
            for (Peleador pel : viewModel.getPeleadoresAll()) {
                if (idsFavoritos.contains(pel.getIdPeleador())) {
                    peleadoresFavs.add(pel);
                }
            }
            //adaptadorPeleadores.actualizarLista(peleadoresFavs);
        });
    }
}