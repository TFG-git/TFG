package com.example.tfg_inicial.fragments.inicio.carteleras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tfg_inicial.R;
import com.example.tfg_inicial.clases.Pelea;

public class DetallePeleaFragment extends Fragment {

    private static final String ARG_PELEA = "pelea";
    private Pelea pelea;

    public static DetallePeleaFragment newInstance(Pelea pelea) {
        DetallePeleaFragment fragment = new DetallePeleaFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PELEA, pelea);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pelea = getArguments().getParcelable(ARG_PELEA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_pelea, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView imageRojo = view.findViewById(R.id.imagePeleadorRojo);
        ImageView imageAzul = view.findViewById(R.id.imagePeleadorAzul);
        TextView titulo = view.findViewById(R.id.textViewTitulo);
        TextView nombreRojo = view.findViewById(R.id.textViewNombreRojo);
        TextView nombreAzul = view.findViewById(R.id.textViewNombreAzul);
        TextView infoPelea = view.findViewById(R.id.textViewInfoPelea);

        if (pelea != null) {
            titulo.setText(pelea.getCategoriaPeso());
            nombreRojo.setText(pelea.getPeleadorRojo().getNombreCompleto());
            nombreAzul.setText(pelea.getPeleadorAzul().getNombreCompleto());
            infoPelea.setText("Método: " + pelea.getMetodo() +
                    "\nRound: " + pelea.getRonda() +
                    "\nTiempo: " + pelea.getTiempo());

            //imageRojo.setImageResource(R.drawable.no_profile_image);
            //imageAzul.setImageResource(R.drawable.no_profile_image);
        }
    }
}
