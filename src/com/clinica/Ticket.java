package com.clinica;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ticket {
    private String codigo;
    private LocalDateTime hora;
    private Paciente paciente;
    private EstadoTurno estado;

    public Ticket(String codigo, Paciente paciente) {
        this.codigo = codigo;
        this.paciente = paciente;
        this.hora = LocalDateTime.now();
        this.estado = EstadoTurno.PENDIENTE;
    }

    public String getCodigo() {
        return codigo;
    }

    public LocalDateTime getHora() {
        return hora;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public void setEstado(EstadoTurno estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("%s | %s | %s | %s | %s", 
            codigo, 
            paciente.getIdentificacion(),
            paciente.getNombre(),
            hora.format(formatter), 
            estado);
    }
}
