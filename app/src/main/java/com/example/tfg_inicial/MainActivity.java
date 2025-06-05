package com.example.tfg_inicial;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.tfg_inicial.fragments.inicio.FragmentInicio;
import com.example.tfg_inicial.fragments.listas.FragmentListas;
import com.example.tfg_inicial.fragments.perfil.FragmentPerfil;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnInicio, btnListas, btnPerfil;
    private MainViewModel viewModel;

    private LinearLayout layoutCargando;

    int icHomeFilled = R.drawable.ic_home;
    int icHomeOutline = R.drawable.ic_home_outline;
    int icListasOutline = R.drawable.ic_favorite;
    int icListasFilled = R.drawable.ic_favorite_filled;
    int icPerfilFilled = R.drawable.ic_profile;
    int icPerfilOutline = R.drawable.ic_profile_outline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutCargando = findViewById(R.id.layoutCargando);
        layoutCargando.setVisibility(View.VISIBLE);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        if (viewModel.isDataLoadedCarteleras() && viewModel.isDataLoadedPeleadores()) {
            layoutCargando.setVisibility(View.GONE);
            mostrarFragmentInicioSiNoEsta();
        } else {
            layoutCargando.setVisibility(View.VISIBLE);
            cargarEventos();
            cargarPeleadores();
        }

        btnInicio = findViewById(R.id.btnInicio);
        btnListas = findViewById(R.id.btnListas);
        btnPerfil = findViewById(R.id.btnPerfil);

        btnInicio.setOnClickListener(this);
        btnListas.setOnClickListener(this);
        btnPerfil.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Fragment miFragment;
        if (view == findViewById(R.id.btnInicio)) {
            miFragment = new FragmentInicio();
            actualizarIconosBottomNav(0);
        } else if (view == findViewById(R.id.btnListas)) {
            miFragment = new FragmentListas();
            actualizarIconosBottomNav(1);
        } else if (view == findViewById(R.id.btnPerfil)) {
            miFragment = new FragmentPerfil();
            actualizarIconosBottomNav(2);
        } else {
            miFragment = new FragmentInicio();
            actualizarIconosBottomNav(0);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.llContenedorFragments, miFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void cargarEventos(){
        if (!viewModel.isDataLoadedCarteleras()) {
            Log.d("MainActivity", "Cargando JSON desde Firebase");
            StorageReference ref = FirebaseStorage.getInstance()
                    .getReference()
                    .child("jsonCarteleras.json");
            ref.getBytes(25 * 1024 * 1024).addOnSuccessListener(bytes -> {
                String jsonEventos = new String(bytes, StandardCharsets.UTF_8);
                runOnUiThread(() -> {
                    viewModel.cargarJSONEventosDesdeTexto(jsonEventos);
                    chequearCargaCompleta();
                });
            }).addOnFailureListener(e -> {
                Log.e("MainActivity", "Error al cargar JSON", e);
                Toast.makeText(this, "Error al cargar eventos", Toast.LENGTH_LONG).show();
            });
        }
    }
    public void cargarPeleadores(){
        if (!viewModel.isDataLoadedPeleadores()) {
            Log.d("MainActivity", "Cargando JSON desde Firebase");
            StorageReference ref = FirebaseStorage.getInstance()
                    .getReference()
                    .child("jsonPeleadores.json");
            ref.getBytes(20 * 1024 * 1024).addOnSuccessListener(bytes -> {
                String jsonPeleadores = new String(bytes, StandardCharsets.UTF_8);
                runOnUiThread(() -> {
                    viewModel.cargarJSONPeleadoresDesdeTexto(jsonPeleadores);
                    chequearCargaCompleta();
                });
            }).addOnFailureListener(e -> {
                Log.e("MainActivity", "Error al cargar JSON", e);
                Toast.makeText(this, "Error al cargar eventos", Toast.LENGTH_LONG).show();
            });
        }
    }

    private void actualizarIconosBottomNav(int seleccionado) {
        switch (seleccionado) {
            case 0: // Home
                btnInicio.setImageResource(icHomeFilled);
                btnListas.setImageResource(icListasOutline);
                btnPerfil.setImageResource(icPerfilOutline);
                break;
            case 1: // Listas
                btnInicio.setImageResource(icHomeOutline);
                btnListas.setImageResource(icListasFilled);
                btnPerfil.setImageResource(icPerfilOutline);
                break;
            case 2: // Perfil
                btnInicio.setImageResource(icHomeOutline);
                btnListas.setImageResource(icListasOutline);
                btnPerfil.setImageResource(icPerfilFilled);
                break;
        }
    }

    private void chequearCargaCompleta() {
        if (viewModel.isDataLoadedCarteleras() && viewModel.isDataLoadedPeleadores()) {
            layoutCargando.setVisibility(View.GONE);
            mostrarFragmentInicioSiNoEsta();
        }
    }
    private void mostrarFragmentInicioSiNoEsta() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.llContenedorFragments);
        if (!(fragment instanceof FragmentInicio)) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.llContenedorFragments, new FragmentInicio());
            ft.commit();
        }
    }

}
