package com.example.tfg_inicial.clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Peleador implements Parcelable {

    //Atributos
    private int idPeleador;
    private String nombrePeleador;
    private String nacionalidad;
    private String division;

    //Constructor vacío
    public Peleador() { }

    //Constructor
    public Peleador(int idPeleador, String nombrePeleador, String nacionalidad, String division) {
        this.idPeleador = idPeleador;
        this.nombrePeleador = nombrePeleador;
        this.nacionalidad = nacionalidad;
        this.division = division;
    }

    //Metodo Parcelable
    protected Peleador(Parcel in) {
        idPeleador = in.readInt();
        nombrePeleador = in.readString();
        nacionalidad = in.readString();
        division = in.readString();
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idPeleador);
        dest.writeString(nombrePeleador);
        dest.writeString(nacionalidad);
        dest.writeString(division);
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

    //Getters y Setters
    public int getIdPeleador() {return idPeleador;}
    public void setIdPeleador(int idPeleador) {this.idPeleador = idPeleador;}

    public String getNombrePeleador() {return nombrePeleador;}
    public void setNombrePeleador(String nombrePeleador) {this.nombrePeleador = nombrePeleador;}

    public String getNacionalidad() {return nacionalidad;}
    public void setNacionalidad(String nacionalidad) {this.nacionalidad = nacionalidad;}

    public String getDivision() {return division;}
    public void setDivision(String division) {this.division = division;}
}
