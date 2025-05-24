package com.example.demo.model;

public class HistorialMedico {
    private Long idMascota;
    private String fecha; // formato dd/mm/aaaa
    private String diagnostico;
    private String tratamiento;

    public HistorialMedico() {}

    public HistorialMedico(Long idMascota, String fecha, String diagnostico, String tratamiento) {
        this.idMascota = idMascota;
        this.fecha = fecha;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
    }

    public Long getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Long idMascota) {
        this.idMascota = idMascota;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }
}
