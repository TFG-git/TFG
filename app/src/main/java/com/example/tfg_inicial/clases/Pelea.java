package com.example.tfg_inicial.clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Pelea implements Parcelable {

    //Atributos

    @SerializedName("winner")
    private String ganador;

    @SerializedName("weight_class")
    private String categoriaPeso;

    @SerializedName("method")
    private String metodo;

    @SerializedName("details")
    private String detalles;

    @SerializedName("round")
    private String ronda;

    @SerializedName("time")
    private String tiempo;

    @SerializedName("fighters")
    private Map<String, Peleador> peleadores;

    public Pelea() {
        peleadores = new HashMap<>();
    }

    public String getGanador() { return ganador; }
    public String getCategoriaPeso() { return categoriaPeso; }
    public String getMetodo() { return metodo; }
    public String getDetalles() { return detalles; }
    public String getRonda() { return ronda; }
    public String getTiempo() { return tiempo; }
    public Map<String, Peleador> getPeleadores() { return peleadores; }

    public Peleador getPeleadorRojo() {
        return peleadores != null ? peleadores.get("red") : null;
    }

    public Peleador getPeleadorAzul() {
        return peleadores != null ? peleadores.get("blue") : null;
    }

    // Parcelable implementation
    protected Pelea(Parcel in) {
        ganador = in.readString();
        categoriaPeso = in.readString();
        metodo = in.readString();
        detalles = in.readString();
        ronda = in.readString();
        tiempo = in.readString();
        peleadores = new HashMap<>();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            Peleador value = in.readParcelable(Peleador.class.getClassLoader());
            peleadores.put(key, value);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ganador);
        dest.writeString(categoriaPeso);
        dest.writeString(metodo);
        dest.writeString(detalles);
        dest.writeString(ronda);
        dest.writeString(tiempo);
        dest.writeInt(peleadores.size());
        for (Map.Entry<String, Peleador> entry : peleadores.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Pelea> CREATOR = new Creator<Pelea>() {
        @Override
        public Pelea createFromParcel(Parcel in) {
            return new Pelea(in);
        }

        @Override
        public Pelea[] newArray(int size) {
            return new Pelea[size];
        }
    };
}
