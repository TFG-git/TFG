package com.example.tfg_inicial.clases;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

public class Cartelera implements Parcelable {

    //Atributos
    private int idCartelera;
    @SerializedName("event_name")
    private String nombreCartelera;
    @SerializedName("event_date")
    private String fecha;
    @SerializedName("event_location")
    private String lugar;
    @SerializedName("fights")
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

    public Date getFechaParseada() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            return sdf.parse(fecha);
        } catch (ParseException e) {
            Log.e("ERROR AL PARSEAR", "Error al parsear fecha: \"" + fecha + "\"", e);
            return null;
        }
    }

    public String getLugar() {return lugar;}
    public void setLugar(String lugar) {this.lugar = lugar;}

    public ArrayList<Pelea> getPeleas() {return peleas;}
    public void setPeleas(ArrayList<Pelea> peleas) {this.peleas = peleas;}
}
