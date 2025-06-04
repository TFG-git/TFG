package com.example.tfg_inicial;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    long tiempo = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        };

        Timer timer = new Timer();
        timer.schedule(task, tiempo);*/
        setContentView(R.layout.activity_splash_screen);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                // Ya hay sesión iniciada
                startActivity(new Intent(this, MainActivity.class));
            } else {
                // No hay sesión, redirigir a login
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        }, 1500); // duración del splash en ms
    }
}