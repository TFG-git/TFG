package com.example.tfg_inicial.clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Pelea implements Parcelable {

    //Atributos
    @SerializedName("fight_id")
    private int idPelea;

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
    private Map<String, EstadisticasPeleaPeleador> peleadores;

    //Constructor vacío para GSON
    public Pelea() {
        peleadores = new HashMap<>();
    }

    public int getIdPelea(){ return idPelea; }
    public String getGanador() { return ganador; }
    public String getCategoriaPeso() { return categoriaPeso; }
    public String getMetodo() { return metodo; }
    public String getDetalles() { return detalles; }
    public String getRonda() { return ronda; }
    public String getTiempo() { return tiempo; }

    public Map<String, EstadisticasPeleaPeleador> getPeleadores() { return peleadores; }

    public EstadisticasPeleaPeleador  getPeleadorRojo() { return peleadores != null ? peleadores.get("red") : null; }

    public EstadisticasPeleaPeleador  getPeleadorAzul() { return peleadores != null ? peleadores.get("blue") : null; }

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
            EstadisticasPeleaPeleador value = in.readParcelable(EstadisticasPeleaPeleador.class.getClassLoader());
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
        for (Map.Entry<String, EstadisticasPeleaPeleador> entry : peleadores.entrySet()) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pelea pelea = (Pelea) o;
        return idPelea == pelea.idPelea;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idPelea);
    }
}
