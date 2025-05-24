package com.example.demo.dto;

public class ClienteDTO {
    private String nombre;
    private String telefono;
    private String correo;
    private String nombreMascota;

    public ClienteDTO() {}

    public ClienteDTO(String nombre, String telefono, String correo, String nombreMascota) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.nombreMascota = nombreMascota;
    }
    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getNombreMascota() { return nombreMascota; }
    public void setNombreMascota(String nombreMascota) { this.nombreMascota = nombreMascota; }
}
