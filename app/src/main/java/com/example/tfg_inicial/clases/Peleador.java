package com.example.tfg_inicial.clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Peleador implements Parcelable {

    //Atributos
    @SerializedName("full_name")
    private String nombreCompleto;

    @SerializedName("nickname")
    private String apodo;

    @SerializedName("nationality")
    private String nacionalidad;

    @SerializedName("stance")
    private String guardia;

    @SerializedName("kd")
    private String knockdowns;

    @SerializedName("str_landed")
    private String golpesTotales;

    @SerializedName("str_total")
    private String golpesIntentados;

    @SerializedName("str_head_landed")
    private String golpesCabeza;

    @SerializedName("str_head_total")
    private String intentosCabeza;

    @SerializedName("str_body_landed")
    private String golpesCuerpo;

    @SerializedName("str_body_total")
    private String intentosCuerpo;

    @SerializedName("str_leg_landed")
    private String golpesPierna;

    @SerializedName("str_leg_total")
    private String intentosPierna;

    @SerializedName("str_distance_landed")
    private String golpesDistancia;

    @SerializedName("str_distance_total")
    private String intentosDistancia;

    @SerializedName("str_clinch_landed")
    private String golpesClinch;

    @SerializedName("str_clinch_total")
    private String intentosClinch;

    @SerializedName("str_ground_landed")
    private String golpesSuelo;

    @SerializedName("str_ground_total")
    private String intentosSuelo;

    @SerializedName("td_landed")
    private String takedowns;

    @SerializedName("td_total")
    private String intentosTD;

    @SerializedName("sub")
    private String intentosSub;

    public Peleador() {}

    protected Peleador(Parcel in) {
        nombreCompleto = in.readString();
        apodo = in.readString();
        nacionalidad = in.readString();
        guardia = in.readString();
        knockdowns = in.readString();
        golpesTotales = in.readString();
        golpesIntentados = in.readString();
        golpesCabeza = in.readString();
        intentosCabeza = in.readString();
        golpesCuerpo = in.readString();
        intentosCuerpo = in.readString();
        golpesPierna = in.readString();
        intentosPierna = in.readString();
        golpesDistancia = in.readString();
        intentosDistancia = in.readString();
        golpesClinch = in.readString();
        intentosClinch = in.readString();
        golpesSuelo = in.readString();
        intentosSuelo = in.readString();
        takedowns = in.readString();
        intentosTD = in.readString();
        intentosSub = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombreCompleto);
        dest.writeString(apodo);
        dest.writeString(nacionalidad);
        dest.writeString(guardia);
        dest.writeString(knockdowns);
        dest.writeString(golpesTotales);
        dest.writeString(golpesIntentados);
        dest.writeString(golpesCabeza);
        dest.writeString(intentosCabeza);
        dest.writeString(golpesCuerpo);
        dest.writeString(intentosCuerpo);
        dest.writeString(golpesPierna);
        dest.writeString(intentosPierna);
        dest.writeString(golpesDistancia);
        dest.writeString(intentosDistancia);
        dest.writeString(golpesClinch);
        dest.writeString(intentosClinch);
        dest.writeString(golpesSuelo);
        dest.writeString(intentosSuelo);
        dest.writeString(takedowns);
        dest.writeString(intentosTD);
        dest.writeString(intentosSub);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Peleador> CREATOR = new Creator<Peleador>() {
        @Override
        public Peleador createFromParcel(Parcel in) {
            return new Peleador(in);
        }

        @Override
        public Peleador[] newArray(int size) {
            return new Peleador[size];
        }
    };

    public String getNombreCompleto() {return nombreCompleto;}
    public String getApodo() {return apodo;}
    public String getNacionalidad() {return nacionalidad;}
    public String getGuardia() {return guardia;}
    public String getKnockdowns() {return knockdowns;}
    public String getGolpesTotales() {return golpesTotales;}
    public String getGolpesIntentados() {return golpesIntentados;}
    public String getGolpesCabeza() {return golpesCabeza;}
    public String getIntentosCabeza() {return intentosCabeza;}
    public String getGolpesCuerpo() {return golpesCuerpo;}
    public String getIntentosCuerpo() {return intentosCuerpo;}
    public String getGolpesPierna() {return golpesPierna;}
    public String getIntentosPierna() {return intentosPierna;}
    public String getGolpesDistancia() {return golpesDistancia;}
    public String getIntentosDistancia() {return intentosDistancia;}
    public String getGolpesClinch() {return golpesClinch;}
    public String getIntentosClinch() {return intentosClinch;}
    public String getGolpesSuelo() {return golpesSuelo;}
    public String getIntentosSuelo() {return intentosSuelo;}
    public String getTakedowns() {return takedowns;}
    public String getIntentosTD() {return intentosTD;}
    public String getIntentosSub() {return intentosSub;}
}
