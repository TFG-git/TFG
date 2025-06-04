package com.example.tfg_inicial.fragments.inicio.carteleras;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.tfg_inicial.DescargarUrlCache;
import com.example.tfg_inicial.GraficosUtils;
import com.example.tfg_inicial.MainViewModel;
import com.example.tfg_inicial.R;
import com.example.tfg_inicial.clases.Pelea;
import com.example.tfg_inicial.clases.Peleador;
import com.example.tfg_inicial.dialogs.PeleadorDialogFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class DetallePeleaFragment extends Fragment {

    private static final String ARG_PELEA = "pelea";
    private Pelea pelea;
    private MainViewModel viewModel;

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
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
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

        String idPeleadorRojo = pelea.getPeleadorRojo().getIdPeleador();
        Peleador peleadorGlobalRojo = viewModel.buscarPeleadorPorId(idPeleadorRojo);
        DescargarUrlCache.getUrl(normalizarNombreCargarImagen(peleadorGlobalRojo), url -> {
            if (url != null) {
                Glide.with(view.getContext())
                        .load(url)
                        .error(R.drawable.no_profile_image)
                        .into(imageRojo);
            } else {
                imageRojo.setImageResource(R.drawable.no_profile_image);
            }
        });
        imageRojo.setOnClickListener(v -> {
            Context context = v.getContext();
            PeleadorDialogFragment dialog = PeleadorDialogFragment.newInstance(peleadorGlobalRojo);

            if (context instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) context;
                dialog.show(activity.getSupportFragmentManager(), "detalle_peleador");
            }
        });

        String idPeleadorAzul = pelea.getPeleadorAzul().getIdPeleador();
        Peleador peleadorGlobalAzul = viewModel.buscarPeleadorPorId(idPeleadorAzul);
        DescargarUrlCache.getUrl(normalizarNombreCargarImagen(peleadorGlobalAzul), url -> {
            if (url != null) {
                Glide.with(view.getContext())
                        .load(url)
                        .error(R.drawable.no_profile_image)
                        .into(imageAzul);
            } else {
                imageAzul.setImageResource(R.drawable.no_profile_image);
            }
        });
        imageAzul.setOnClickListener(v -> {
            Context context = v.getContext();
            PeleadorDialogFragment dialog = PeleadorDialogFragment.newInstance(peleadorGlobalAzul);

            if (context instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) context;
                dialog.show(activity.getSupportFragmentManager(), "detalle_peleador");
            }
        });

        if (pelea != null) {
            titulo.setText(pelea.getCategoriaPeso());
            nombreRojo.setText(pelea.getPeleadorRojo().getNombre());
            nombreAzul.setText(pelea.getPeleadorAzul().getNombre());
            infoPelea.setText("Método: " + pelea.getMetodo() +
                    "\nRound: " + pelea.getRonda() +
                    "\nTiempo: " + pelea.getTiempo());

            //imageRojo.setImageResource(R.drawable.no_profile_image);
            //imageAzul.setImageResource(R.drawable.no_profile_image);
        }

        BarChart barChartLandedByTarget = view.findViewById(R.id.barChartLandedByTarget);
        BarChart barChartLandedByPosition = view.findViewById(R.id.barChartLandedByPosition);

        // Etiquetas y valores de cada gráfica
        String[] labelsTarget = {"Cabeza", "Cuerpo", "Pierna"};
        String[] valoresRojoTarget = {
                pelea.getPeleadorRojo().getGolpesTotalesCabeza(),
                pelea.getPeleadorRojo().getGolpesTotalesCuerpo(),
                pelea.getPeleadorRojo().getGolpesTotalesPierna()
        };
        String[] valoresAzulTarget = {
                pelea.getPeleadorAzul().getGolpesTotalesCabeza(),
                pelea.getPeleadorAzul().getGolpesTotalesCuerpo(),
                pelea.getPeleadorAzul().getGolpesTotalesPierna()
        };
        String[] porcentajeRojoTarget = GraficosUtils.calcularPorcentajes(valoresRojoTarget);
        String[] porcentajeAzulTarget = GraficosUtils.calcularPorcentajes(valoresAzulTarget);

        // Lo mismo para "Position"
        String[] labelsPosition = {"Distancia", "Clinch", "Ground"};
        String[] valoresRojoPosition = {
                pelea.getPeleadorRojo().getGolpesIntentadosDistancia(),
                pelea.getPeleadorRojo().getGolpesIntentadosClinch(),
                pelea.getPeleadorRojo().getGolpesIntentadosGround()
        };
        String[] valoresAzulPosition = {
                pelea.getPeleadorAzul().getGolpesIntentadosDistancia(),
                pelea.getPeleadorAzul().getGolpesIntentadosClinch(),
                pelea.getPeleadorAzul().getGolpesIntentadosGround()
        };;
        String[] porcentajeRojoPosition = GraficosUtils.calcularPorcentajes(valoresRojoPosition);
        String[] porcentajeAzulPosition = GraficosUtils.calcularPorcentajes(valoresAzulPosition);

        // Llama al utils:
        GraficosUtils.mostrarBarChartEnfrentado(getContext(), barChartLandedByTarget, labelsTarget, porcentajeRojoTarget, porcentajeAzulTarget);
        GraficosUtils.mostrarBarChartEnfrentado(getContext(), barChartLandedByPosition, labelsPosition, porcentajeRojoPosition, porcentajeAzulPosition);

        int totalLandedRojo = Integer.parseInt(pelea.getPeleadorRojo().getGolpesTotales());
        int totalThrownRojo = Integer.parseInt(pelea.getPeleadorRojo().getGolpesIntentados());
        int totalLandedAzul = Integer.parseInt(pelea.getPeleadorAzul().getGolpesTotales());
        int totalThrownAzul = Integer.parseInt(pelea.getPeleadorAzul().getGolpesIntentados());

        TextView textViewLandedTarget = view.findViewById(R.id.textViewGolpes);
        textViewLandedTarget.setText(
                "GOLPES ACERTADOS\n" +
                "Rojo: " + totalLandedRojo + " de " + totalThrownRojo +
                        "   |   Azul: " + totalLandedAzul + " de " + totalThrownAzul
        );
        int submisionesRojo = Integer.parseInt(pelea.getPeleadorRojo().getIntentosSubmision());
        int submisionesAzul = Integer.parseInt(pelea.getPeleadorAzul().getIntentosSubmision());
        TextView textViewSubmision = view.findViewById(R.id.textViewIntentosSubmision);
        textViewSubmision.setText(
                "INTENTOS SUBMISION\n" +
                "Rojo: " + submisionesRojo +
                "   |   Azul: " + submisionesAzul
        );
    }

    private String normalizarNombreCargarImagen(Peleador peleador) {
        String nombreNormalizado = peleador.getNombreCompleto().strip().toLowerCase().replace(" ", "-");
        return "peleadores/" + nombreNormalizado + "-full.webp";
    }
}
