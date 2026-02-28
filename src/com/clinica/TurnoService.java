package com.clinica;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TurnoService {
    private List<Ticket> listaMaestra;
    private int contador;

    public TurnoService(int inicioContador) {
        this.listaMaestra = new ArrayList<>();
        this.contador = inicioContador;
    }

    public Ticket generarTurno(Paciente p) {
        contador++;
        String prefijo = p.getTipoPaciente().getPrefijo();
        String codigo = prefijo + "-" + String.format("%03d", contador);
        Ticket ticket = new Ticket(codigo, p);
        listaMaestra.add(ticket);
        return ticket;
    }

    public void cambiarEstado(String codigo, EstadoTurno nuevo) {
        for (Ticket ticket : listaMaestra) {
            if (ticket.getCodigo().equals(codigo)) {
                ticket.setEstado(nuevo);
                return;
            }
        }
    }

    public List<Ticket> getListaVisible() {
        return listaMaestra.stream()
            .filter(t -> t.getEstado() == EstadoTurno.PENDIENTE)
            .collect(Collectors.toList());
    }

    public Ticket buscarPorCodigo(String codigo) {
        for (Ticket ticket : listaMaestra) {
            if (ticket.getCodigo().equals(codigo)) {
                return ticket;
            }
        }
        return null;
    }

    public Ticket buscarPorIdentificacion(String identificacion) {
        for (Ticket ticket : listaMaestra) {
            if (ticket.getPaciente().getIdentificacion().equals(identificacion) 
                && ticket.getEstado() == EstadoTurno.PENDIENTE) {
                return ticket;
            }
        }
        return null;
    }

    public List<Ticket> getListaMaestra() {
        return new ArrayList<>(listaMaestra);
    }
}
