package vista;

import controlador.ControladorCita;
import controlador.ControladorMedico;
import controlador.ControladorPaciente;
import modelo.Cita;
import modelo.Medico;
import modelo.Paciente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
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
    private JButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar;
    
    public PanelCitas() {
        controlador = new ControladorCita();
        controladorPaciente = new ControladorPaciente();
        controladorMedico = new ControladorMedico();
        inicializarComponentes();
        cargarCombos();
        cargarCitas();
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
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
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
        
        btnGuardar.addActionListener(e -> guardarCita());
        btnActualizar.addActionListener(e -> actualizarCita());
        btnEliminar.addActionListener(e -> eliminarCita());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        
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
    
    /*
    // El panel de citas no ctualiza
    public void actualizarCombos() {
        cargarCombos();
        JOptionPane.showMessageDialog(this, 
            "Listas actualizadas:\n" +
            "Pacientes: " + comboPaciente.getItemCount() + "\n" +
            "Médicos: " + comboMedico.getItemCount(),
            "Actualización", JOptionPane.INFORMATION_MESSAGE);
    }
    */
    
    private void cargarCombos() {
        // Cargar pacientes
        comboPaciente.removeAllItems();
        List<Paciente> pacientes = controladorPaciente.obtenerTodosLosPacientes();
        for (Paciente p : pacientes) {
            comboPaciente.addItem(p);
        }
        
        // Cargar médicos
        comboMedico.removeAllItems();
        List<Medico> medicos = controladorMedico.obtenerTodosLosMedicos();
        for (Medico m : medicos) {
            comboMedico.addItem(m);
        }
    }
    
    private void guardarCita() {
        try {
            Paciente paciente = (Paciente) comboPaciente.getSelectedItem();
            Medico medico = (Medico) comboMedico.getSelectedItem();
            LocalDate fecha = LocalDate.parse(txtFecha.getText());
            LocalTime hora = LocalTime.parse(txtHora.getText());
            
            boolean exito = controlador.agendarCita(paciente, medico, fecha, hora, txtMotivo.getText());
            
            if (exito) {
                JOptionPane.showMessageDialog(this, "Cita agendada exitosamente");
                limpiarCampos();
                cargarCitas();
            } else {
                JOptionPane.showMessageDialog(this, "Error: Verifique los datos o cita duplicada",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en formato de fecha/hora",
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
        // Aquí se cargarían los datos seleccionados
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
        cargarCombos();
    }
}
