package com.example.tfg_inicial.clases;

public class Usuario {
    private String idUsuario;
    private String nombre;
    private String nacionalidad;
    private String bio;
    private String fotoPerfilUrl;

    public Usuario() {}

    public Usuario(String idUsuario, String nombre, String nacionalidad, String bio, String fotoPerfilUrl) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.bio = bio;
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    public String getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getNacionalidad() { return nacionalidad; }
    public String getBio() { return bio; }
    public String getFotoPerfilUrl() { return fotoPerfilUrl; }

    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    public void setBio(String bio) { this.bio = bio; }
    public void setFotoPerfilUrl(String fotoPerfilUrl) { this.fotoPerfilUrl = fotoPerfilUrl; }
}
