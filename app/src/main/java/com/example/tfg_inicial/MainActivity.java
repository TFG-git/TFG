package com.example.tfg_inicial;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnInicio, btnListas, btnPerfil;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString("message", "integración de firebase completa");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


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

        Toast.makeText(this, "Prueba de José", Toast.LENGTH_LONG).show();
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

}
