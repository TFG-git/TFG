package com.example.tfg_inicial.clases;

public class Comentario {
    private String id;
    private String uidUsuario;
    private String texto;
    private long timestamp;

    public Comentario() {} // Obligatorio para Firebase

    public Comentario(String id, String uidUsuario, String texto, long timestamp) {
        this.id = id;
        this.uidUsuario = uidUsuario;
        this.texto = texto;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public String getUidUsuario() { return uidUsuario; }
    public String getTexto() { return texto; }
    public long getTimestamp() { return timestamp; }
}
