package com.clinica;

public enum TipoPaciente {
    ADULTO_MAYOR("M"),
    EMBARAZADA("E"),
    REGULAR("N");

    private final String prefijo;

    TipoPaciente(String prefijo) {
        this.prefijo = prefijo;
    }

    public String getPrefijo() {
        return prefijo;
    }
}
