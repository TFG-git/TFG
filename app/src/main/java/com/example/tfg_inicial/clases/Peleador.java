package com.example.tfg_inicial.clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Peleador implements Parcelable {

    //Atributos
    @SerializedName("id")
    private String idPeleador;
    @SerializedName("nombrePeleador")
    private String nombreCompleto;

    @SerializedName("apodo")
    private String apodo;

    @SerializedName("record")
    private String record;

    @SerializedName("altura")
    private String altura;

    @SerializedName("peso")
    private String peso;

    @SerializedName("alcance")
    private String alcance;

    @SerializedName("guardia")
    private String guardia;

    @SerializedName("fechaNacimiento")
    private String fechaNacimiento;

    @SerializedName("strikesPrecision")
    private String strikesPrecision;

    @SerializedName("strikesDefensa")
    private String strikesDefensa;

    @SerializedName("tdPrecision")
    private String tdPrecision;

    @SerializedName("tdDefensa")
    private String tdDefensa;

    //Constructor vacío para GSON
    public Peleador() {}

    // Constructor para Parcel
    protected Peleador(Parcel in) {
        idPeleador = in.readString();
        nombreCompleto = in.readString();
        apodo = in.readString();
        record = in.readString();
        altura = in.readString();
        peso = in.readString();
        alcance = in.readString();
        guardia = in.readString();
        fechaNacimiento = in.readString();
        strikesPrecision = in.readString();
        strikesDefensa = in.readString();
        tdPrecision = in.readString();
        tdDefensa = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idPeleador);
        dest.writeString(nombreCompleto);
        dest.writeString(apodo);
        dest.writeString(record);
        dest.writeString(altura);
        dest.writeString(peso);
        dest.writeString(alcance);
        dest.writeString(guardia);
        dest.writeString(fechaNacimiento);
        dest.writeString(strikesPrecision);
        dest.writeString(strikesDefensa);
        dest.writeString(tdPrecision);
        dest.writeString(tdDefensa);
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

    // Getters y setters
    public String getIdPeleador() { return idPeleador; }
    public void setIdPeleador(String idPeleador) { this.idPeleador = idPeleador; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getApodo() { return apodo; }
    public void setApodo(String apodo) { this.apodo = apodo; }

    public String getRecord() { return record; }
    public void setRecord(String record) { this.record = record; }

    public String getAltura() { return altura; }
    public void setAltura(String altura) { this.altura = altura; }

    public String getPeso() { return peso; }
    public void setPeso(String peso) { this.peso = peso; }

    public String getAlcance() { return alcance; }
    public void setAlcance(String alcance) { this.alcance = alcance; }

    public String getGuardia() { return guardia; }
    public void setGuardia(String guardia) { this.guardia = guardia; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getStrikesPrecision() { return strikesPrecision; }
    public void setStrikesPrecision(String strikesPrecision) { this.strikesPrecision = strikesPrecision; }

    public String getStrikesDefensa() { return strikesDefensa; }
    public void setStrikesDefensa(String strikesDefensa) { this.strikesDefensa = strikesDefensa; }

    public String getTdPrecision() { return tdPrecision; }
    public void setTdPrecision(String tdPrecision) { this.tdPrecision = tdPrecision; }

    public String getTdDefensa() { return tdDefensa; }
    public void setTdDefensa(String tdDefensa) { this.tdDefensa = tdDefensa; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Peleador peleador = (Peleador) o;
        return idPeleador != null ? idPeleador.equals(peleador.idPeleador) : peleador.idPeleador == null;
    }

    @Override
    public int hashCode() {
        return idPeleador != null ? idPeleador.hashCode() : 0;
    }

}
