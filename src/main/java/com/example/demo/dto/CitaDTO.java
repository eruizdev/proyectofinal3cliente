package com.example.demo.dto;

public class CitaDTO {
    private String idCita;
    private String nombreDueno;
    private String hora;
    private String veterinarioAsignado;
    private String nombreMascota;
    private String fecha;
    private String motivo;

    public CitaDTO() {}

    public CitaDTO(String idCita, String nombreDueno, String hora, String veterinarioAsignado, 
                   String nombreMascota, String fecha, String motivo) {
        this.idCita = idCita;
        this.nombreDueno = nombreDueno;
        this.hora = hora;
        this.veterinarioAsignado = veterinarioAsignado;
        this.nombreMascota = nombreMascota;
        this.fecha = fecha;
        this.motivo = motivo;
    }
    // Getters y setters
    public String getIdCita() { return idCita; }
    public void setIdCita(String idCita) { this.idCita = idCita; }
    public String getNombreDueno() { return nombreDueno; }
    public void setNombreDueno(String nombreDueno) { this.nombreDueno = nombreDueno; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    public String getVeterinarioAsignado() { return veterinarioAsignado; }
    public void setVeterinarioAsignado(String veterinarioAsignado) { this.veterinarioAsignado = veterinarioAsignado; }
    public String getNombreMascota() { return nombreMascota; }
    public void setNombreMascota(String nombreMascota) { this.nombreMascota = nombreMascota; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
