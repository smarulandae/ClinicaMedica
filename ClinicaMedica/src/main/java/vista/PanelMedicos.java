package vista;

import controlador.ControladorMedico;
import modelo.Especialidad;
import modelo.Medico;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelMedicos extends JPanel {
    private ControladorMedico controlador;
    private JTable tablaMedicos;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtApellido, txtLicencia, txtTelefono;
    private JComboBox<Especialidad> comboEspecialidad;
    private JButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar;
    
    public PanelMedicos() {
        controlador = new ControladorMedico();
        inicializarComponentes();
        cargarMedicos();
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
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Médico"));
        
        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);
        
        panel.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        panel.add(txtApellido);
        
        panel.add(new JLabel("Licencia Médica:"));
        txtLicencia = new JTextField();
        panel.add(txtLicencia);
        
        panel.add(new JLabel("Especialidad:"));
        comboEspecialidad = new JComboBox<>(Especialidad.values());
        panel.add(comboEspecialidad);
        
        panel.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panel.add(txtTelefono);
        
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
        
        btnGuardar.addActionListener(e -> guardarMedico());
        btnActualizar.addActionListener(e -> actualizarMedico());
        btnEliminar.addActionListener(e -> eliminarMedico());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        
        return panel;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Médicos"));
        
        String[] columnas = {"ID", "Nombre", "Apellido", "Licencia", "Especialidad", "Teléfono"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaMedicos = new JTable(modeloTabla);
        tablaMedicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tablaMedicos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaMedicos.getSelectedRow() != -1) {
                cargarMedicoSeleccionado();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaMedicos);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void guardarMedico() {
        boolean exito = controlador.registrarMedico(
            txtNombre.getText(),
            txtApellido.getText(),
            txtLicencia.getText(),
            (Especialidad) comboEspecialidad.getSelectedItem(),
            txtTelefono.getText()
        );
        
        if (exito) {
            JOptionPane.showMessageDialog(this, "Médico registrado exitosamente");
            limpiarCampos();
            cargarMedicos();
        } else {
            JOptionPane.showMessageDialog(this, "Error: Verifique los datos o licencia duplicada",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarMedico() {
        int filaSeleccionada = tablaMedicos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un médico");
            return;
        }
        
        int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        boolean exito = controlador.actualizarMedico(
            id,
            txtNombre.getText(),
            txtApellido.getText(),
            txtLicencia.getText(),
            (Especialidad) comboEspecialidad.getSelectedItem(),
            txtTelefono.getText()
        );
        
        if (exito) {
            JOptionPane.showMessageDialog(this, "Médico actualizado exitosamente");
            limpiarCampos();
            cargarMedicos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarMedico() {
        int filaSeleccionada = tablaMedicos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un médico");
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar este médico?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            if (controlador.eliminarMedico(id)) {
                JOptionPane.showMessageDialog(this, "Médico eliminado");
                limpiarCampos();
                cargarMedicos();
            }
        }
    }
    
    private void cargarMedicos() {
        modeloTabla.setRowCount(0);
        List<Medico> medicos = controlador.obtenerTodosLosMedicos();
        
        for (Medico m : medicos) {
            Object[] fila = {
                m.getId(),
                m.getNombre(),
                m.getApellido(),
                m.getLicencia(),
                m.getEspecialidad().getNombre(),
                m.getTelefono()
            };
            modeloTabla.addRow(fila);
        }
    }
    
    private void cargarMedicoSeleccionado() {
        int fila = tablaMedicos.getSelectedRow();
        txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtApellido.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtLicencia.setText(modeloTabla.getValueAt(fila, 3).toString());
        String especialidad = modeloTabla.getValueAt(fila, 4).toString();
        for (Especialidad e : Especialidad.values()) {
            if (e.getNombre().equals(especialidad)) {
                comboEspecialidad.setSelectedItem(e);
                break;
            }
        }
        txtTelefono.setText(modeloTabla.getValueAt(fila, 5).toString());
    }
    
    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtLicencia.setText("");
        txtTelefono.setText("");
        comboEspecialidad.setSelectedIndex(0);
        tablaMedicos.clearSelection();
    }
}
