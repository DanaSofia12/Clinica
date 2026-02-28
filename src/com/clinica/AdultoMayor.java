package com.clinica;

public class AdultoMayor extends Paciente {
    public AdultoMayor(String identificacion, String nombre) {
        super(identificacion, nombre);
    }

    @Override
    public TipoPaciente getTipoPaciente() {
        return TipoPaciente.ADULTO_MAYOR;
    }
}
