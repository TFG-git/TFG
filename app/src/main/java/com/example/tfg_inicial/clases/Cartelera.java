package com.example.tfg_inicial.clases;

import java.util.ArrayList;

public class Cartelera {

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
