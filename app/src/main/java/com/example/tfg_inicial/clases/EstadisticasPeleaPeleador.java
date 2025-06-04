package com.example.tfg_inicial.clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class EstadisticasPeleaPeleador implements Parcelable {

    @SerializedName("idPeleador")
    private String idPeleador;
    @SerializedName("name")
    private String nombre;
    @SerializedName("kd")
    private String knockdowns;
    @SerializedName("str_landed")
    private String golpesTotales;
    @SerializedName("str_total")
    private String golpesIntentados;
    @SerializedName("str_head_landed")
    private String golpesTotalesCabeza;
    @SerializedName("str_head_total")
    private String golpesIntentadosCabeza;
    @SerializedName("str_body_landed")
    private String  golpesTotalesCuerpo;
    @SerializedName("str_body_total")
    private String golpesIntentadosCuerpo;
    @SerializedName("str_leg_landed")
    private String golpesTotalesPierna;
    @SerializedName("str_leg_total")
    private String golpesIntentadosPierna;
    @SerializedName("str_distance_landed")
    private String golpesIntentadosDistancia;
    @SerializedName("str_distance_total")
    private String golpesTotalesDistancia;
    @SerializedName("str_clinch_landed")
    private String golpesIntentadosClinch;
    @SerializedName("str_clinch_total")
    private String golpesTotalesClinch;
    @SerializedName("str_ground_landed")
    private String golpesIntentadosGround;
    @SerializedName("str_ground_total")
    private String golpesTotalesGround;
    @SerializedName("td_landed")
    private String derribosIntentados;
    @SerializedName("td_total")
    private String derribosTotales;
    @SerializedName("sub")
    private String intentosSubmision;

    public EstadisticasPeleaPeleador() {}
    protected EstadisticasPeleaPeleador(Parcel in) {
        idPeleador = in.readString();
        nombre = in.readString();
        knockdowns = in.readString();
        golpesTotales = in.readString();
        golpesIntentados = in.readString();
        golpesTotalesCabeza = in.readString();
        golpesIntentadosCabeza = in.readString();
        golpesTotalesCuerpo = in.readString();
        golpesIntentadosCuerpo = in.readString();
        golpesTotalesPierna = in.readString();
        golpesIntentadosPierna = in.readString();
        golpesTotalesDistancia = in.readString();
        golpesIntentadosDistancia = in.readString();
        golpesTotalesClinch = in.readString();
        golpesIntentadosClinch = in.readString();
        golpesTotalesGround = in.readString();
        golpesIntentadosGround = in.readString();
        derribosTotales = in.readString();
        derribosIntentados = in.readString();
        intentosSubmision = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idPeleador);
        dest.writeString(nombre);
        dest.writeString(knockdowns);
        dest.writeString(golpesTotales);
        dest.writeString(golpesIntentados);
        dest.writeString(golpesTotalesCabeza);
        dest.writeString(golpesIntentadosCabeza);
        dest.writeString(golpesTotalesCuerpo);
        dest.writeString(golpesIntentadosCuerpo);
        dest.writeString(golpesTotalesPierna);
        dest.writeString(golpesIntentadosPierna);
        dest.writeString(golpesTotalesDistancia);
        dest.writeString(golpesIntentadosDistancia);
        dest.writeString(golpesTotalesClinch);
        dest.writeString(golpesIntentadosClinch);
        dest.writeString(golpesTotalesGround);
        dest.writeString(golpesIntentadosGround);
        dest.writeString(derribosTotales);
        dest.writeString(derribosIntentados);
        dest.writeString(intentosSubmision);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EstadisticasPeleaPeleador> CREATOR = new Creator<EstadisticasPeleaPeleador>() {
        @Override
        public EstadisticasPeleaPeleador createFromParcel(Parcel in) {
            return new EstadisticasPeleaPeleador(in);
        }
        @Override
        public EstadisticasPeleaPeleador[] newArray(int size) {
            return new EstadisticasPeleaPeleador[size];
        }
    };

    // --- Getters y Setters
    public String getIdPeleador() { return idPeleador; }
    public void setIdPeleador(String idPeleador) { this.idPeleador = idPeleador; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getKnockdowns() { return knockdowns; }
    public void setKnockdowns(String knockdowns) { this.knockdowns = knockdowns; }

    public String getGolpesTotales() { return golpesTotales; }
    public void setGolpesTotales(String golpesTotales) { this.golpesTotales = golpesTotales; }

    public String getGolpesIntentados() { return golpesIntentados; }
    public void setGolpesIntentados(String golpesIntentados) { this.golpesIntentados = golpesIntentados; }

    public String getGolpesTotalesCabeza() { return golpesTotalesCabeza; }
    public void setGolpesTotalesCabeza(String golpesTotalesCabeza) { this.golpesTotalesCabeza = golpesTotalesCabeza; }

    public String getGolpesIntentadosCabeza() { return golpesIntentadosCabeza; }
    public void setGolpesIntentadosCabeza(String golpesIntentadosCabeza) { this.golpesIntentadosCabeza = golpesIntentadosCabeza; }

    public String getGolpesTotalesCuerpo() { return golpesTotalesCuerpo; }
    public void setGolpesTotalesCuerpo(String golpesTotalesCuerpo) { this.golpesTotalesCuerpo = golpesTotalesCuerpo; }

    public String getGolpesIntentadosCuerpo() { return golpesIntentadosCuerpo; }
    public void setGolpesIntentadosCuerpo(String golpesIntentadosCuerpo) { this.golpesIntentadosCuerpo = golpesIntentadosCuerpo; }

    public String getGolpesTotalesPierna() { return golpesTotalesPierna; }
    public void setGolpesTotalesPierna(String golpesTotalesPierna) { this.golpesTotalesPierna = golpesTotalesPierna; }

    public String getGolpesIntentadosPierna() { return golpesIntentadosPierna; }
    public void setGolpesIntentadosPierna(String golpesIntentadosPierna) { this.golpesIntentadosPierna = golpesIntentadosPierna; }

    public String getGolpesTotalesDistancia() { return golpesTotalesDistancia; }
    public void setGolpesTotalesDistancia(String golpesTotalesDistancia) { this.golpesTotalesDistancia = golpesTotalesDistancia; }

    public String getGolpesIntentadosDistancia() { return golpesIntentadosDistancia; }
    public void setGolpesIntentadosDistancia(String golpesIntentadosDistancia) { this.golpesIntentadosDistancia = golpesIntentadosDistancia; }

    public String getGolpesTotalesClinch() { return golpesTotalesClinch; }
    public void setGolpesTotalesClinch(String golpesTotalesClinch) { this.golpesTotalesClinch = golpesTotalesClinch; }

    public String getGolpesIntentadosClinch() { return golpesIntentadosClinch; }
    public void setGolpesIntentadosClinch(String golpesIntentadosClinch) { this.golpesIntentadosClinch = golpesIntentadosClinch; }

    public String getGolpesTotalesGround() { return golpesTotalesGround; }
    public void setGolpesTotalesGround(String golpesTotalesGround) { this.golpesTotalesGround = golpesTotalesGround; }

    public String getGolpesIntentadosGround() { return golpesIntentadosGround; }
    public void setGolpesIntentadosGround(String golpesIntentadosGround) { this.golpesIntentadosGround = golpesIntentadosGround; }

    public String getDerribosTotales() { return derribosTotales; }
    public void setDerribosTotales(String derribosTotales) { this.derribosTotales = derribosTotales; }

    public String getDerribosIntentados() { return derribosIntentados; }
    public void setDerribosIntentados(String derribosIntentados) { this.derribosIntentados = derribosIntentados; }

    public String getIntentosSubmision() { return intentosSubmision; }
    public void setIntentosSubmision(String intentosSubmision) { this.intentosSubmision = intentosSubmision; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstadisticasPeleaPeleador that = (EstadisticasPeleaPeleador) o;
        return idPeleador != null ? idPeleador.equals(that.idPeleador) : that.idPeleador == null;
    }

    @Override
    public int hashCode() {
        return idPeleador != null ? idPeleador.hashCode() : 0;
    }

}
