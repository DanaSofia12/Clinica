package com.clinica;

public abstract class Paciente implements IPaciente {
    protected String identificacion;
    protected String nombre;

    public Paciente(String identificacion, String nombre) {
        this.identificacion = identificacion;
        this.nombre = nombre;
    }

    @Override
    public String getIdentificacion() {
        return identificacion;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public abstract TipoPaciente getTipoPaciente();
}
