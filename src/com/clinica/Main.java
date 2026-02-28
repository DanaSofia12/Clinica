package com.clinica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Main extends JFrame {
    private TurnoService turnoService;
    private JTextField txtIdentificacion;
    private JTextField txtNombre;
    private JComboBox<TipoPaciente> cmbTipoPaciente;
    private JTable tblTickets;
    private DefaultTableModel tableModel;
    private JTextField txtContadorInicial;
    private boolean sistemaIniciado = false;

    public Main() {
        setTitle("Sistema de Turnos Clinica");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        mostrarDialogoInicio();
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Operaria", crearPanelOperaria());
        tabbedPane.addTab("Paciente", crearPanelPaciente());
        
        add(tabbedPane);
    }

    private JPanel crearPanelOperaria() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Lista de Turnos Pendientes:"));
        
        String[] columnas = {"Codigo", "Identificacion", "Nombre", "Hora", "Tipo"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblTickets = new JTable(tableModel);
        tblTickets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tblTickets);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAtender = new JButton("Atender Turno Seleccionado");
        btnAtender.addActionListener(e -> atenderTurno());
        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> actualizarLista());
        
        btnPanel.add(btnAtender);
        btnPanel.add(btnActualizar);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelPaciente() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Solicitar Turno:"), gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Numero de Identificacion:"), gbc);

        gbc.gridx = 1;
        txtIdentificacion = new JTextField(15);
        panel.add(txtIdentificacion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        txtNombre = new JTextField(15);
        panel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Tipo de Paciente:"), gbc);

        gbc.gridx = 1;
        cmbTipoPaciente = new JComboBox<>(TipoPaciente.values());
        panel.add(cmbTipoPaciente, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton btnGenerar = new JButton("Generar Turno");
        btnGenerar.addActionListener(e -> generarTurno());
        panel.add(btnGenerar, gbc);

        gbc.gridy = 5;
        JButton btnCancelar = new JButton("Cancelar Mi Turno");
        btnCancelar.addActionListener(e -> cancelarTurno());
        panel.add(btnCancelar, gbc);

        return panel;
    }

    private void mostrarDialogoInicio() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Numero inicial del contador:"));
        txtContadorInicial = new JTextField("0");
        panel.add(txtContadorInicial);
        
        JButton btnIniciar = new JButton("Iniciar Sistema");
        btnIniciar.addActionListener(e -> iniciarSistema());
        panel.add(btnIniciar);

        int result = JOptionPane.showConfirmDialog(
            this, 
            panel, 
            "Configuracion Inicial", 
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }
    }

    private void iniciarSistema() {
        try {
            int inicio = Integer.parseInt(txtContadorInicial.getText());
            turnoService = new TurnoService(inicio);
            sistemaIniciado = true;
            actualizarLista();
            JOptionPane.getRootFrame().dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un numero valido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarTurno() {
        if (!sistemaIniciado) {
            JOptionPane.showMessageDialog(this, "El sistema no ha sido iniciado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String identificacion = txtIdentificacion.getText().trim();
        String nombre = txtNombre.getText().trim();
        
        if (identificacion.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese identificacion y nombre", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TipoPaciente tipo = (TipoPaciente) cmbTipoPaciente.getSelectedItem();
        Paciente paciente = crearPaciente(identificacion, nombre, tipo);
        Ticket ticket = turnoService.generarTurno(paciente);

        JOptionPane.showMessageDialog(
            this, 
            "Turno generado exitosamente!\n\n" +
            "Codigo: " + ticket.getCodigo() + "\n" +
            "Paciente: " + nombre + "\n" +
            "Identificacion: " + identificacion + "\n" +
            "Tipo: " + tipo.name(),
            "Turno Asignado",
            JOptionPane.INFORMATION_MESSAGE
        );

        txtIdentificacion.setText("");
        txtNombre.setText("");
        actualizarLista();
    }

    private Paciente crearPaciente(String identificacion, String nombre, TipoPaciente tipo) {
        return switch (tipo) {
            case ADULTO_MAYOR -> new AdultoMayor(identificacion, nombre);
            case EMBARAZADA -> new Embarazada(identificacion, nombre);
            case REGULAR -> new Regular(identificacion, nombre);
        };
    }

    private void atenderTurno() {
        int selectedRow = tblTickets.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un turno para atender", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigo = (String) tableModel.getValueAt(selectedRow, 0);
        turnoService.cambiarEstado(codigo, EstadoTurno.ATENDIDO);
        
        JOptionPane.showMessageDialog(this, "Turno " + codigo + " atendido", "Exito", JOptionPane.INFORMATION_MESSAGE);
        actualizarLista();
    }

    private void cancelarTurno() {
        if (!sistemaIniciado) {
            JOptionPane.showMessageDialog(this, "El sistema no ha sido iniciado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String identificacion = txtIdentificacion.getText().trim();
        if (identificacion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese su numero de identificacion para cancelar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Ticket ticket = turnoService.buscarPorIdentificacion(identificacion);
        if (ticket == null) {
            JOptionPane.showMessageDialog(this, "No se encontró un turno pendiente para esta identificacion", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Esta seguro que desea cancelar el turno " + ticket.getCodigo() + "?",
            "Confirmar Cancelacion",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            turnoService.cambiarEstado(ticket.getCodigo(), EstadoTurno.CANCELADO);
            JOptionPane.showMessageDialog(this, "Turno cancelado exitosamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
            txtIdentificacion.setText("");
            txtNombre.setText("");
            actualizarLista();
        }
    }

    private void actualizarLista() {
        tableModel.setRowCount(0);
        List<Ticket> tickets = turnoService.getListaVisible();
        
        for (Ticket ticket : tickets) {
            Object[] row = {
                ticket.getCodigo(),
                ticket.getPaciente().getIdentificacion(),
                ticket.getPaciente().getNombre(),
                ticket.getHora().toLocalTime().toString(),
                ticket.getPaciente().getTipoPaciente().name()
            };
            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Main().setVisible(true);
        });
    }
}
