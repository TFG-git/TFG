package com.example.tfg_inicial.clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Pelea implements Parcelable {

    //Atributos
    private int idPelea;
    private Peleador peleador1;
    private Peleador peleador2;
    private Peleador vencedor;
    private String metodo_victoria;

    //Constructor vacío
    public Pelea() { }

    //Constructor
    public Pelea(int idPelea, Peleador peleador1, Peleador peleador2, Peleador vencedor, String metodo_victoria) {
        this.idPelea = idPelea;
        this.peleador1 = peleador1;
        this.peleador2 = peleador2;
        this.vencedor = vencedor;
        this.metodo_victoria = metodo_victoria;
    }

    //Metodos Parcelable
    protected Pelea(Parcel in) {
        idPelea = in.readInt();
        peleador1 = in.readParcelable(Peleador.class.getClassLoader());
        peleador2 = in.readParcelable(Peleador.class.getClassLoader());
        vencedor = in.readParcelable(Peleador.class.getClassLoader());
        metodo_victoria = in.readString();
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idPelea);
        dest.writeParcelable(peleador1, flags);
        dest.writeParcelable(peleador2, flags);
        dest.writeParcelable(vencedor, flags);
        dest.writeString(metodo_victoria);
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

    //Getters y Setters
    public int getIdPelea() {return idPelea;}
    public void setIdPelea(int idPelea) {this.idPelea = idPelea;}

    public Peleador getPeleador1() {return peleador1;}
    public void setPeleador1(Peleador peleador1) {this.peleador1 = peleador1;}

    public Peleador getPeleador2() {return peleador2;}
    public void setPeleador2(Peleador peleador2) {this.peleador2 = peleador2;}

    public Peleador getVencedor() {return vencedor;}
    public void setVencedor(Peleador vencedor) {this.vencedor = vencedor;}

    public String getMetodo_victoria() {return metodo_victoria;}
    public void setMetodo_victoria(String metodo_victoria) {this.metodo_victoria = metodo_victoria;}
}
