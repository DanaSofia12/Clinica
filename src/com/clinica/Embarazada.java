package com.clinica;

public class Embarazada extends Paciente {
    public Embarazada(String identificacion, String nombre) {
        super(identificacion, nombre);
    }

    @Override
    public TipoPaciente getTipoPaciente() {
        return TipoPaciente.EMBARAZADA;
    }
}
