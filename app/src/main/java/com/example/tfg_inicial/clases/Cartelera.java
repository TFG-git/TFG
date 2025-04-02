package com.example.tfg_inicial.clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Cartelera implements Parcelable {

    //Atributos
    private int idCartelera;
    private String nombreCartelera;
    private String fecha;
    private String lugar;
    private ArrayList<Pelea> peleas;

    //Constructor vacío
    public Cartelera() { }

    //Constructor
    public Cartelera(int idCartelera, String nombreCartelera, String fecha, String lugar, ArrayList<Pelea> peleas) {
        this.idCartelera = idCartelera;
        this.nombreCartelera = nombreCartelera;
        this.fecha = fecha;
        this.lugar = lugar;
        this.peleas = peleas;
    }

    //Metodos Parcelable
    protected Cartelera(Parcel in) {
        idCartelera = in.readInt();
        nombreCartelera = in.readString();
        fecha = in.readString();
        lugar = in.readString();
        peleas = in.createTypedArrayList(Pelea.CREATOR);
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idCartelera);
        dest.writeString(nombreCartelera);
        dest.writeString(lugar);
        dest.writeString(fecha);
        dest.writeTypedList(peleas);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<Cartelera> CREATOR = new Creator<Cartelera>() {
        @Override
        public Cartelera createFromParcel(Parcel in) {
            return new Cartelera(in);
        }

        @Override
        public Cartelera[] newArray(int size) {
            return new Cartelera[size];
        }
    };

    //Getters y Setters
    public int getIdCartelera() {return idCartelera;}
    public void setIdCartelera(int idCartelera) {this.idCartelera = idCartelera;}

    public String getNombreCartelera() {return nombreCartelera;}
    public void setNombreCartelera(String nombreCartelera) {this.nombreCartelera = nombreCartelera;}

    public String getFecha() {return fecha;}
    public void setFecha(String fecha) {this.fecha = fecha;}

    public String getLugar() {return lugar;}
    public void setLugar(String lugar) {this.lugar = lugar;}

    public ArrayList<Pelea> getPeleas() {return peleas;}
    public void setPeleas(ArrayList<Pelea> peleas) {this.peleas = peleas;}
}
