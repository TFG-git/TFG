package com.example.tfg_inicial.fragments.perfil;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tfg_inicial.DescargarUrlCache;
import com.example.tfg_inicial.LoginActivity;
import com.example.tfg_inicial.SignUpActivity;
import com.example.tfg_inicial.clases.Usuario;
import com.example.tfg_inicial.R;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.*;

import java.util.Arrays;
import java.util.List;

public class FragmentPerfil extends Fragment {

    private ImageView fotoPerfil;
    private ImageButton btnCambiarFoto, btnCerrarSesion;
    private EditText etNombre, etBio;
    private Spinner spinnerNacionalidad;
    private Button btnEditar, btnGuardar;

    private DatabaseReference usuarioRef;
    private StorageReference storageRef;
    private Usuario usuarioActual;
    private Uri nuevaImagenUri = null;
    private boolean editando = false;
    private String uid;

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
                    nuevaImagenUri = uri;
                    Glide.with(this)
                            .load(nuevaImagenUri)
                            .circleCrop()
                            .into(fotoPerfil);
                }
            });

    private String obtenerExtension(Uri uri) {
        ContentResolver contentResolver = requireContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fotoPerfil = view.findViewById(R.id.fotoPerfil);
        btnCambiarFoto = view.findViewById(R.id.btnCambiarFoto);
        etNombre = view.findViewById(R.id.etNombre);
        etBio = view.findViewById(R.id.etBio);
        spinnerNacionalidad = view.findViewById(R.id.spinnerNacionalidad);
        btnEditar = view.findViewById(R.id.btnEditar);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

        ArrayAdapter<String> adaptadorPaises = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item,
                listaPaises
        );
        adaptadorPaises.setDropDownViewResource(R.layout.spinner_item);
        spinnerNacionalidad.setAdapter(adaptadorPaises);
        spinnerNacionalidad.setEnabled(false);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usuarioRef = FirebaseDatabase.getInstance().getReference("usuarios").child(uid);
        storageRef = FirebaseStorage.getInstance().getReference("fotos_perfil");

        cargarPerfil();

        btnEditar.setOnClickListener(v -> activarEdicion(true));

        btnCambiarFoto.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        btnGuardar.setOnClickListener(v -> guardarCambios());

        btnCerrarSesion.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    private void activarEdicion(boolean activar) {
        editando = activar;
        etNombre.setEnabled(activar);
        spinnerNacionalidad.setEnabled(activar);
        spinnerNacionalidad.setAlpha(1f);
        etBio.setEnabled(activar);
        btnCambiarFoto.setVisibility(activar ? View.VISIBLE : View.GONE);
        btnGuardar.setVisibility(activar ? View.VISIBLE : View.GONE);
        btnEditar.setVisibility(activar ? View.GONE : View.VISIBLE);
    }

    private void cargarPerfil() {
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuarioActual = snapshot.getValue(Usuario.class);
                if (usuarioActual != null) {
                    etNombre.setText(usuarioActual.getNombre());
                    etBio.setText(usuarioActual.getBio());
                    int pos = listaPaises.indexOf(usuarioActual.getNacionalidad());
                    spinnerNacionalidad.setSelection(pos >= 0 ? pos : 0);
                    DescargarUrlCache.getUrl(usuarioActual.getFotoPerfilUrl(), url -> {
                        if (url != null) {
                            Glide.with(requireContext())
                                    .load(url)
                                    .error(R.drawable.no_profile_image)
                                    .circleCrop()
                                    .into(fotoPerfil);
                        } else {
                            fotoPerfil.setImageResource(R.drawable.no_profile_image);
                        }
                    });
                } else {
                    activarEdicion(true); // Usuario nuevo, fuerza edición
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void guardarCambios() {
        String nombre = etNombre.getText().toString().trim();
        String nacionalidad = spinnerNacionalidad.getSelectedItem().toString();
        String bio = etBio.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)) {
            etNombre.setError("El nombre no puede estar vacío");
            return;
        }

        final String bioFinal = TextUtils.isEmpty(bio) ? "" : bio;

        // Si se selecciono una nueva imagen, se sube primero
        if (nuevaImagenUri != null) {
            String extension = obtenerExtension(nuevaImagenUri);
            if (extension == null) extension = "jpg";
            String storagePath = "fotos_perfil/" + uid + "." + extension;

            StorageReference ref = storageRef.child(uid + "." + extension);
            ref.putFile(nuevaImagenUri)
                    .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl()
                            .addOnSuccessListener(uri -> guardarUsuario(nombre, nacionalidad, bioFinal, storagePath)))
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Error al subir imagen", Toast.LENGTH_SHORT).show();
                        guardarUsuario(nombre, nacionalidad, bioFinal, "fotos_perfil/no_profile.png");
                    });
        } else {
            String fotoUrl = usuarioActual != null && usuarioActual.getFotoPerfilUrl() != null
                    ? usuarioActual.getFotoPerfilUrl() : "";
            guardarUsuario(nombre, nacionalidad, bioFinal, fotoUrl);
        }
    }

    private void guardarUsuario(String nombre, String nacionalidad, String bio, String fotoUrl) {
        Usuario user = new Usuario(uid, nombre, nacionalidad, bio, fotoUrl);
        usuarioRef.setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Toast toast = new Toast(getContext());
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    View layout = inflater.inflate(R.layout.toast_custom_icon, null);
                    ImageView icon = layout.findViewById(R.id.toastIcon);
                    TextView text = layout.findViewById(R.id.toastText);
                    text.setText("Usuario guardado correctamente");
                    toast.setView(layout);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    activarEdicion(false);
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error al guardar perfil", Toast.LENGTH_SHORT).show());
    }
}