package com.example.tfg_inicial.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tfg_inicial.DescargarUrlCache;
import com.example.tfg_inicial.GraficosUtils;
import com.example.tfg_inicial.MainViewModel;
import com.example.tfg_inicial.R;
import com.example.tfg_inicial.adaptadores.AdaptadorPersonalizadoPeleas;
import com.example.tfg_inicial.clases.Cartelera;
import com.example.tfg_inicial.clases.Pelea;
import com.example.tfg_inicial.clases.Peleador;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeleadorDialogFragment extends DialogFragment {

    private static final String ARG_PELEADOR = "peleador";
    private Peleador peleador;
    private MainViewModel viewModel;
    public static PeleadorDialogFragment newInstance(Peleador peleador) {
        PeleadorDialogFragment fragment = new PeleadorDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PELEADOR, peleador);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.TransparentDialogTheme);
        if (getArguments() != null) {
            peleador = getArguments().getParcelable(ARG_PELEADOR);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_peleador, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        ImageView imagePeleador = view.findViewById(R.id.imagePeleador);
        TextView textViewNombre = view.findViewById(R.id.textViewNombre);
        TextView textViewApodo = view.findViewById(R.id.textViewApodo);
        TextView textViewGuardia = view.findViewById(R.id.textViewGuardia);
        TextView textViewRecord = view.findViewById(R.id.textViewRecord);
        ImageButton buttonCerrar = view.findViewById(R.id.buttonCerrar);

        if (peleador != null) {
            textViewNombre.setText(peleador.getNombreCompleto());
            textViewApodo.setText(peleador.getApodo() == null ? "" : peleador.getApodo());
            textViewRecord.setText(peleador.getRecord());
            textViewGuardia.setText(peleador.getGuardia() == null ? "" : "Guardia: " + peleador.getGuardia());

            DescargarUrlCache.getUrl(normalizarNombreCargarImagen(peleador), url -> {
                if (url != null) {
                    Glide.with(view.getContext())
                            .load(url)
                            .error(R.drawable.no_profile_image)
                            .into(imagePeleador);
                } else {
                    imagePeleador.setImageResource(R.drawable.no_profile_image);
                }
            });

            List<Pelea> todasLasPeleas = viewModel.getPeleasAll();

            // Filtrado peleas donde el peleador participo
            List<Pelea> peleasDeEstePeleador = new ArrayList<>();
            int cuenta = 0;
            Log.d("PELEAS", "Total peleas: " + todasLasPeleas.size());
            for (Pelea pelea : todasLasPeleas) {
                String idPeleadorRojo = pelea.getPeleadorRojo().getIdPeleador();
                Peleador peleadorGlobalRojo = viewModel.buscarPeleadorPorId(idPeleadorRojo);
                String idPeleadorAzul = pelea.getPeleadorAzul().getIdPeleador();
                Peleador peleadorGlobalAzul = viewModel.buscarPeleadorPorId(idPeleadorAzul);
                if (peleadorGlobalRojo.getIdPeleador().equals(peleador.getIdPeleador()) || peleadorGlobalAzul.getIdPeleador().equals(peleador.getIdPeleador())) {
                    peleasDeEstePeleador.add(pelea);
                    cuenta++;
                    Log.d("PELEAS_MATCH", "Pelea encontrada: " + pelea.getIdPelea());
                }
            }
            Log.d("PELEAS", "Total peleas encontradas: " + cuenta);

            List<Cartelera> todasLasCarteleras = viewModel.getCarteleraCompleta();

            Collections.sort(peleasDeEstePeleador, (p1, p2) -> {
                Cartelera cartelera1 = buscarCarteleraDePelea(p1, todasLasCarteleras);
                Cartelera cartelera2 = buscarCarteleraDePelea(p2, todasLasCarteleras);
                Date fecha1 = cartelera1 != null ? cartelera1.getFechaParseada() : new Date(0);
                Date fecha2 = cartelera2 != null ? cartelera2.getFechaParseada() : new Date(0);
                return fecha2.compareTo(fecha1); // Descendente
            });

            TextView textNumeroPeleas = view.findViewById(R.id.textNumeroPeleas);
            textNumeroPeleas.setText("Total de peleas: " + peleasDeEstePeleador.size());

            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPeleas);
            FragmentManager fm = ((AppCompatActivity) requireActivity()).getSupportFragmentManager();
            AdaptadorPersonalizadoPeleas adapter = new AdaptadorPersonalizadoPeleas(peleasDeEstePeleador, fm, this, viewModel, true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            // Llama al utils:
            int porcentajeStrikesPrecision = parsePorcentajeInt(peleador.getStrikesPrecision());
            int porcentajeStrikesDefensa = parsePorcentajeInt(peleador.getStrikesDefensa());
            int porcentajeTdPrecision = parsePorcentajeInt(peleador.getTdPrecision());
            int porcentajeTdDefensa = parsePorcentajeInt(peleador.getTdDefensa());

            PieChart pieChartStrikesPrecision = view.findViewById(R.id.pieChartStrikesPrecision);
            PieChart pieChartStrikesDefensa = view.findViewById(R.id.pieChartStrikesDefensa);
            PieChart pieChartTdPrecision = view.findViewById(R.id.pieChartTdPrecision);
            PieChart pieChartTdDefensa = view.findViewById(R.id.pieChartTdDefensa);

            GraficosUtils.mostrarPieChartPorcentaje(getContext(), pieChartStrikesPrecision, porcentajeStrikesPrecision, "Acertados", "Fallados", ContextCompat.getColor(view.getContext(), R.color.rojo2), ContextCompat.getColor(view.getContext(), R.color.rojo3));
            GraficosUtils.mostrarPieChartPorcentaje(getContext(), pieChartStrikesDefensa, porcentajeStrikesDefensa, "Defendidos", "Recibidos", ContextCompat.getColor(view.getContext(), R.color.rojo2), ContextCompat.getColor(view.getContext(), R.color.rojo3));
            GraficosUtils.mostrarPieChartPorcentaje(getContext(), pieChartTdPrecision, porcentajeTdPrecision, "Acertados", "Fallados", ContextCompat.getColor(view.getContext(), R.color.azul), ContextCompat.getColor(view.getContext(), R.color.azulClaro));
            GraficosUtils.mostrarPieChartPorcentaje(getContext(), pieChartTdDefensa, porcentajeTdDefensa, "Defendidos", "Recibidos", ContextCompat.getColor(view.getContext(), R.color.azul), ContextCompat.getColor(view.getContext(), R.color.azulClaro));

        }

        buttonCerrar.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            int width = (int) (requireContext().getResources().getDisplayMetrics().widthPixels * 0.95);
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;

            window.setLayout(width, height);

            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 0.4f;
            window.setAttributes(params);
        }
    }
    private String normalizarNombreCargarImagen(Peleador peleador) {
        String nombreNormalizado = peleador.getNombreCompleto().strip().toLowerCase().replace(" ", "-");
        return "peleadores/" + nombreNormalizado + "-full.webp";
    }

    private Cartelera buscarCarteleraDePelea(Pelea pelea, List<Cartelera> carteleraCompleta) {
        for (Cartelera c : carteleraCompleta) {
            if (c.getPeleas().contains(pelea)) {
                return c;
            }
        }
        return null;
    }

    public static int parsePorcentajeInt(String valor) {
        try {
            return Integer.parseInt(valor.replace("%", "").trim());
        } catch (Exception e) {
            return 0;
        }
    }
}
