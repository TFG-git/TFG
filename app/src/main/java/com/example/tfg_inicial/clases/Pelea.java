package com.example.tfg_inicial.clases;

public class Pelea {

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
