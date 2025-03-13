package com.example.tfg_inicial.clases;

public class Peleador {

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
