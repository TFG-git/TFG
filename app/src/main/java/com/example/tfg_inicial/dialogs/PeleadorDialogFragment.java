package com.example.tfg_inicial.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import androidx.fragment.app.DialogFragment;

import com.example.tfg_inicial.R;
import com.example.tfg_inicial.clases.Peleador;

public class PeleadorDialogFragment extends DialogFragment {

    private static final String ARG_PELEADOR = "peleador";
    private Peleador peleador;

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

        ImageView imagePeleador = view.findViewById(R.id.imagePeleador);
        TextView textViewNombre = view.findViewById(R.id.textViewNombre);
        TextView textViewApodo = view.findViewById(R.id.textViewApodo);
        TextView textViewNacionalidad = view.findViewById(R.id.textViewNacionalidad);
        TextView textViewRecord = view.findViewById(R.id.textViewRecord);
        ImageButton buttonCerrar = view.findViewById(R.id.buttonCerrar);

        if (peleador != null) {
            textViewNombre.setText(peleador.getNombreCompleto());
            textViewApodo.setText(peleador.getNombreCompleto());
            textViewNacionalidad.setText(peleador.getNacionalidad());
            //textViewRecord.setText(peleador.get());

            //imagePeleador.setImageResource(R.drawable.no_profile_image);
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
}
