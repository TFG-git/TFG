package com.example.tfg_inicial;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tfg_inicial.R;
import com.example.tfg_inicial.clases.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.List;

public class SignUpActivity extends AppCompatActivity{

    private EditText etNombre, etEmail, etPassword, etBio;
    private Spinner spinnerNacionalidad;
    private ImageView ivFotoPerfil;
    private ImageButton btnCambiarFoto;
    private Button btnSignUp;
    private TextView textGoToLogin;

    private Uri imagenUri = null;
    private FirebaseAuth mAuth;
    private DatabaseReference usuariosRef;
    private StorageReference storageRef;

    private final List<String> listaPaises = Arrays.asList(
            "🇺🇸 Estados Unidos",
            "🇧🇷 Brasil",
            "🇲🇽 México",
            "🇬🇧 Reino Unido",
            "🇨🇦 Canadá",
            "🇪🇸 España",
            "🇦🇷 Argentina",
            "🇨🇱 Chile",
            "🇵🇪 Perú",
            "🇨🇴 Colombia",
            "🇻🇪 Venezuela",
            "🇩🇴 República Dominicana",
            "🇨🇷 Costa Rica",
            "🇵🇷 Puerto Rico",
            "🇯🇵 Japón",
            "🇦🇺 Australia",
            "🇳🇿 Nueva Zelanda",
            "🇷🇺 Rusia",
            "🇮🇪 Irlanda",
            "🇫🇷 Francia",
            "🇩🇪 Alemania",
            "🇵🇭 Filipinas",
            "🇵🇱 Polonia",
            "🇳🇱 Países Bajos",
            "🇮🇹 Italia",
            "🇰🇷 Corea del Sur",
            "🇨🇳 China",
            "🇵🇹 Portugal",
            "🇹🇭 Tailandia",
            "🇿🇦 Sudáfrica",
            "🌎 Otro"
    );
    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imagenUri = uri;
                    Glide.with(this)
                            .load(imagenUri)
                            .circleCrop()
                            .into(ivFotoPerfil);
                }
            });

    private String obtenerExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etNombre = findViewById(R.id.editTextNombre);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        etBio = findViewById(R.id.editTextBio);
        spinnerNacionalidad = findViewById(R.id.spinnerNacionalidad);
        ivFotoPerfil = findViewById(R.id.ivFotoPerfil);
        btnCambiarFoto = findViewById(R.id.btnCambiarFoto);
        btnSignUp = findViewById(R.id.buttonSignUp);
        textGoToLogin = findViewById(R.id.textGoToLogin);

        mAuth = FirebaseAuth.getInstance();
        usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");
        storageRef = FirebaseStorage.getInstance().getReference("fotos_perfil");

        ArrayAdapter<String> adaptadorPaises = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                listaPaises
        );
        adaptadorPaises.setDropDownViewResource(R.layout.spinner_item);
        spinnerNacionalidad.setAdapter(adaptadorPaises);

        btnCambiarFoto.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        btnSignUp.setOnClickListener(v -> registrarUsuario());

        textGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            imagenUri = data.getData();
            Glide.with(this)
                    .load(imagenUri)
                    .placeholder(R.drawable.no_profile_image)
                    .circleCrop()
                    .into(ivFotoPerfil);
        }
    }

    private void registrarUsuario() {
        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String nacionalidad = spinnerNacionalidad.getSelectedItem().toString();
        String bio = etBio.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }
        btnSignUp.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        if (imagenUri != null) {
                            String extension = obtenerExtension(imagenUri);
                            if (extension == null) extension = "jpg";
                            String storagePath = "fotos_perfil/" + uid + "." + extension;

                            StorageReference ref = storageRef.child(uid + "." + extension);
                            ref.putFile(imagenUri)
                                    .addOnSuccessListener(ts -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                                        guardarUsuario(uid, nombre, nacionalidad, bio, storagePath);
                                    }))
                                    .addOnFailureListener(e -> {
                                        guardarUsuario(uid, nombre, nacionalidad, bio, "fotos_perfil/no_profile.png"); // Imagen por defecto
                                    });
                        } else {
                            guardarUsuario(uid, nombre, nacionalidad, bio, "fotos_perfil/no_profile.png");
                        }
                    } else {
                        btnSignUp.setEnabled(true);
                        Toast.makeText(SignUpActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void guardarUsuario(String uid, String nombre, String nacionalidad, String bio, String fotoPerfilUrl) {
        Usuario usuario = new Usuario(uid, nombre, nacionalidad, bio, fotoPerfilUrl);
        usuariosRef.child(uid).setValue(usuario)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignUpActivity.this, "¡Registro completo!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignUpActivity.this, "Error al guardar usuario", Toast.LENGTH_SHORT).show();
                    btnSignUp.setEnabled(true);
                });
    }
}
