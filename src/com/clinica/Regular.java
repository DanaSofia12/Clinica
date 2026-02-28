package com.clinica;

public class Regular extends Paciente {
    public Regular(String identificacion, String nombre) {
        super(identificacion, nombre);
    }

    @Override
    public TipoPaciente getTipoPaciente() {
        return TipoPaciente.REGULAR;
    }
}
