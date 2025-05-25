package com.example.tfg_inicial;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnInicio, btnListas, btnPerfil;
    private FirebaseAnalytics mFirebaseAnalytics;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        /*mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString("message", "integración de firebase completa");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);*/

        cargarCarteleras();

        btnInicio = findViewById(R.id.btnInicio);
        btnListas = findViewById(R.id.btnListas);
        btnPerfil = findViewById(R.id.btnPerfil);

        btnInicio.setOnClickListener(this);
        btnListas.setOnClickListener(this);
        btnPerfil.setOnClickListener(this);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentInicio fragmentInicial = new FragmentInicio();

        fragmentTransaction.add(R.id.llContenedorFragments, fragmentInicial);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        Fragment miFragment;
        if (view == findViewById(R.id.btnInicio)) {
            miFragment = new FragmentInicio();
        } else if (view == findViewById(R.id.btnListas)) {
            miFragment = new FragmentListas();
        } else if (view == findViewById(R.id.btnPerfil)) {
            miFragment = new FragmentPerfil();
        } else {
            miFragment = new FragmentInicio();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.llContenedorFragments, miFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void cargarCarteleras(){
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        if (!viewModel.isDataLoaded()) {
            Log.d("MainActivity", "Cargando JSON desde Firebase");

            StorageReference ref = FirebaseStorage.getInstance()
                    .getReference()
                    .child("jsonPrueba.json");

            ref.getBytes(5 * 1024 * 1024).addOnSuccessListener(bytes -> {
                String jsonCarteleras = new String(bytes, StandardCharsets.UTF_8);
                Log.d("MainActivity", "JSON descargado, tamaño: " + jsonCarteleras.length());
                viewModel.cargarJSONDesdeTexto(jsonCarteleras);
            }).addOnFailureListener(e -> {
                Log.e("MainActivity", "Error al cargar JSON", e);
                Toast.makeText(this, "Error al cargar eventos", Toast.LENGTH_LONG).show();
            });
        } else {
            Log.d("MainActivity", "Datos ya cargados en el ViewModel");
        }
    }

}
