package vista;

import Controlador.ControladorCita;
import Controlador.ControladorMedico;
import Controlador.ControladorPaciente;
import modelo.Cita;
import modelo.Medico;
import modelo.Paciente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PanelCitas extends JPanel {
    private ControladorCita controlador;
    private ControladorPaciente controladorPaciente;
    private ControladorMedico controladorMedico;
    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private JComboBox<Paciente> comboPaciente;
    private JComboBox<Medico> comboMedico;
    private JTextField txtFecha, txtHora, txtMotivo, txtDiagnostico;
    private JButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar, btnRefrescar;
    
    public PanelCitas() {
        System.out.println("\n");
        System.out.println("INICIANDO PANEL DE CITAS");
        System.out.println("");
        
        controlador = new ControladorCita();
        controladorPaciente = new ControladorPaciente();
        controladorMedico = new ControladorMedico();
        
        System.out.println("Controladores creados exitosamente");
        
        inicializarComponentes();
        System.out.println("Componentes inicializados correctamente");
        
        cargarCombos();
        System.out.println("Combos cargados correctamente");
        
        cargarCitas();
        System.out.println("Citas cargadas correctamente");
        
        System.out.println("\n");
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel panelFormulario = crearPanelFormulario();
        add(panelFormulario, BorderLayout.NORTH);
        
        JPanel panelTabla = crearPanelTabla();
        add(panelTabla, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Datos de la Cita"));
        
        panel.add(new JLabel("Paciente:"));
        comboPaciente = new JComboBox<>();
        panel.add(comboPaciente);
        
        panel.add(new JLabel("Médico:"));
        comboMedico = new JComboBox<>();
        panel.add(comboMedico);
        
        panel.add(new JLabel("Fecha (AAAA-MM-DD):"));
        txtFecha = new JTextField(LocalDate.now().toString());
        panel.add(txtFecha);
        
        panel.add(new JLabel("Hora (HH:MM):"));
        txtHora = new JTextField("09:00");
        panel.add(txtHora);
        
        panel.add(new JLabel("Motivo:"));
        txtMotivo = new JTextField();
        panel.add(txtMotivo);
        
        panel.add(new JLabel("Diagnóstico:"));
        txtDiagnostico = new JTextField();
        panel.add(txtDiagnostico);
        
        // El panel de citas no se actualiza automáticamente
        // Botón para refrescar combos manualmente:
        panel.add(new JLabel(""));
        btnRefrescar = new JButton("Actualizar Listas");
        panel.add(btnRefrescar);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        
        panel.add(new JLabel());
        panel.add(panelBotones);
        
        // Eventos
        btnGuardar.addActionListener(e -> guardarCita());
        btnActualizar.addActionListener(e -> actualizarCita());
        btnEliminar.addActionListener(e -> eliminarCita());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnRefrescar.addActionListener(e -> {
            System.out.println("\nActualizando");
            actualizarCombos();
        });
        
        return panel;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Citas"));
        
        String[] columnas = {"ID", "Paciente", "Médico", "Fecha", "Hora", "Motivo", "Diagnóstico"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaCitas = new JTable(modeloTabla);
        tablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tablaCitas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaCitas.getSelectedRow() != -1) {
                cargarCitaSeleccionada();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaCitas);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Método público para actualizar los combos desde afuera importante
     */
    public void actualizarCombos() {
        cargarCombos();
    }
    
    
    // Con este nuevo método se actualiza la carga de médico y pacientes
    private void cargarCombos() {
        System.out.println("\nCargando combos");
        
        // Cargar pacientes
        System.out.println("\nLimpiando combo de pacientes");
        comboPaciente.removeAllItems();
        
        System.out.println("Obteniendo lista de pacientes del controlador");
        List<Paciente> pacientes = controladorPaciente.obtenerTodosLosPacientes();
        
        System.out.println("TOTAL DE PACIENTES: " + pacientes.size());
        
        if (pacientes.isEmpty()) {
            System.out.println("Lista de pacientes vacía");
            comboPaciente.setEnabled(false);
            comboPaciente.setToolTipText("No hay pacientes registrados");
        } else {
            System.out.println("Agregando apciente al combo");
            comboPaciente.setEnabled(true);
            comboPaciente.setToolTipText("");
            for (int i = 0; i < pacientes.size(); i++) {
                Paciente p = pacientes.get(i);
                System.out.println("  [" + (i+1) + "] ID: " + p.getId() + " - " + p.toString());
                comboPaciente.addItem(p);
            }
            System.out.println("Pacientes agregados al combo: " + comboPaciente.getItemCount());
        }
        
        System.out.println("CAMBIO A MÉDICOS");
        
        // Cargar médicos
        System.out.println("Limpiando combo de médicos");
        comboMedico.removeAllItems();
        
        System.out.println("Obteniendo lista de médicos del controlador");
        List<Medico> medicos = controladorMedico.obtenerTodosLosMedicos();
        
        System.out.println("TOTAL DE MÉDICOS: " + medicos.size());
        
        if (medicos.isEmpty()) {
            System.out.println("Lista de médicos vacía");
            comboMedico.setEnabled(false);
            comboMedico.setToolTipText("No hay médicos registrados");
        } else {
            System.out.println("Agregando médicos al combo");
            comboMedico.setEnabled(true);
            comboMedico.setToolTipText("");
            for (int i = 0; i < medicos.size(); i++) {
                Medico m = medicos.get(i);
                System.out.println("  [" + (i+1) + "] ID: " + m.getId() + " - " + m.toString());
                comboMedico.addItem(m);
            }
            System.out.println("Médicos agregados al combo: " + comboMedico.getItemCount());
        }
        
        System.out.println("Resultado final:");
        System.out.println("Pacientes en combo: " + comboPaciente.getItemCount());
        System.out.println("Médicos en combo: " + comboMedico.getItemCount());
        System.out.println("\n");
    }
    
    private void guardarCita() {
        System.out.println("Guardando Cita");
        
        // Validar que haya pacientes y médicos
        if (comboPaciente.getItemCount() == 0 || comboMedico.getItemCount() == 0) {
            System.out.println("No hay pacientes o médicos disponibles");
            JOptionPane.showMessageDialog(this, 
                "Primero debe registrar pacientes y médicos",
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Paciente paciente = (Paciente) comboPaciente.getSelectedItem();
            Medico medico = (Medico) comboMedico.getSelectedItem();
            
            if (paciente == null || medico == null) {
                System.out.println("Paciente o médico es null");
                JOptionPane.showMessageDialog(this, 
                    "Seleccione un paciente y un médico",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            LocalDate fecha = LocalDate.parse(txtFecha.getText());
            LocalTime hora = LocalTime.parse(txtHora.getText());
            
            System.out.println("Datos de la cita:");
            System.out.println("Paciente: " + paciente.toString());
            System.out.println("Médico: " + medico.toString());
            System.out.println("Fecha: " + fecha);
            System.out.println("Hora: " + hora);
            System.out.println("Motivo: " + txtMotivo.getText());
            
            boolean exito = controlador.agendarCita(paciente, medico, fecha, hora, txtMotivo.getText());
            
            if (exito) {
                System.out.println("Cita guardada");
                JOptionPane.showMessageDialog(this, "Cita agendada exitosamente");
                limpiarCampos();
                cargarCitas();
            } else {
                System.out.println("Error al guardar cita");
                JOptionPane.showMessageDialog(this, "Verifique los datos o cita duplicada",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            System.out.println("Excepción al guardar: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error en formato de fecha/hora\n" +
                    "Formato fecha: AAAA-MM-DD (ej: 2025-10-19)\n" +
                    "Formato hora: HH:MM (ej: 09:00)",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarCita() {
        int filaSeleccionada = tablaCitas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita");
            return;
        }
        
        try {
            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            Paciente paciente = (Paciente) comboPaciente.getSelectedItem();
            Medico medico = (Medico) comboMedico.getSelectedItem();
            LocalDate fecha = LocalDate.parse(txtFecha.getText());
            LocalTime hora = LocalTime.parse(txtHora.getText());
            
            boolean exito = controlador.actualizarCita(id, paciente, medico, fecha, hora,
                    txtMotivo.getText(), txtDiagnostico.getText());
            
            if (exito) {
                JOptionPane.showMessageDialog(this, "Cita actualizada exitosamente");
                limpiarCampos();
                cargarCitas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en formato de fecha/hora",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarCita() {
        int filaSeleccionada = tablaCitas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita");
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar esta cita?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            if (controlador.eliminarCita(id)) {
                JOptionPane.showMessageDialog(this, "Cita eliminada");
                limpiarCampos();
                cargarCitas();
            }
        }
    }
    
    private void cargarCitas() {
        modeloTabla.setRowCount(0);
        List<Cita> citas = controlador.obtenerTodasLasCitas();
        
        for (Cita c : citas) {
            Object[] fila = {
                c.getId(),
                c.getPaciente().toString(),
                c.getMedico().toString(),
                c.getFecha().toString(),
                c.getHora().toString(),
                c.getMotivo(),
                c.getDiagnostico()
            };
            modeloTabla.addRow(fila);
        }
    }
    
    private void cargarCitaSeleccionada() {
        int fila = tablaCitas.getSelectedRow();
        txtFecha.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtHora.setText(modeloTabla.getValueAt(fila, 4).toString());
        txtMotivo.setText(modeloTabla.getValueAt(fila, 5).toString());
        txtDiagnostico.setText(modeloTabla.getValueAt(fila, 6).toString());
    }
    
    private void limpiarCampos() {
        txtFecha.setText(LocalDate.now().toString());
        txtHora.setText("09:00");
        txtMotivo.setText("");
        txtDiagnostico.setText("");
        tablaCitas.clearSelection();
    }
}