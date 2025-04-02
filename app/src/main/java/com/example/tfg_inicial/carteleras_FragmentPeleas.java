package com.example.tfg_inicial;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.tfg_inicial.adaptadores.AdaptadorPersonalizadoPeleas;
import com.example.tfg_inicial.clases.Cartelera;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link carteleras_FragmentPeleas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class carteleras_FragmentPeleas extends Fragment {

    private ListView lvPeleas;
    private Cartelera cartelera;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public carteleras_FragmentPeleas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment carteleras_FragmentPeleas.
     */
    // TODO: Rename and change types and number of parameters
    public static carteleras_FragmentPeleas newInstance(String param1, String param2) {
        carteleras_FragmentPeleas fragment = new carteleras_FragmentPeleas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_carteleras__peleas, container, false);

        Log.d("Fragmento abierto", "El fragmento se creo correctamente");

        lvPeleas = view.findViewById(R.id.lvPeleas);

        if (getArguments() != null) {
            cartelera = getArguments().getParcelable("cartelera");
            AdaptadorPersonalizadoPeleas adaptadorPersonalizadoPeleas = new AdaptadorPersonalizadoPeleas(requireContext(), cartelera.getPeleas());
            lvPeleas.setAdapter(adaptadorPersonalizadoPeleas);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().findViewById(R.id.viewPager).setVisibility(View.VISIBLE);
    }

}