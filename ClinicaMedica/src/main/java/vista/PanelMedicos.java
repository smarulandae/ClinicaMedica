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
        try {
            controlador = new ControladorMedico();
            inicializarComponentes();
            cargarMedicos();
        } catch (Exception e) {
            System.err.println("Error crítico al inicializar PanelMedicos: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al inicializar el panel de médicos",
                "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
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
        
        panel.add(new JLabel("Registro interno:"));
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
        
        String[] columnas = {"ID", "Nombre", "Apellido", "Número Registro", "Especialidad", "Teléfono"};
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
        try {
            // Validar que los campos no estén vacíos
            if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Debe ingresar el nombre del médico",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtApellido.getText() == null || txtApellido.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Debe ingresar el apellido del médico",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtLicencia.getText() == null || txtLicencia.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Debe ingresar el número de registro del médico",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Especialidad especialidadSeleccionada = (Especialidad) comboEspecialidad.getSelectedItem();
            if (especialidadSeleccionada == null) {
                JOptionPane.showMessageDialog(this, 
                    "Debe seleccionar una especialidad",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String telefono = (txtTelefono.getText() != null) ? txtTelefono.getText() : "";
            
            boolean exito = controlador.registrarMedico(
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                txtLicencia.getText().trim(),
                especialidadSeleccionada,
                telefono.trim()
            );
            
            if (exito) {
                JOptionPane.showMessageDialog(this, "Médico registrado exitosamente");
                limpiarCampos();
                cargarMedicos();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error: Verifique los datos o el número de registro ya está registrado",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NullPointerException e) {
            System.err.println("Error: Campo nulo al guardar médico - " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error: Algunos campos están vacíos",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error inesperado al guardar médico: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error inesperado: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarMedico() {
        try {
            int filaSeleccionada = tablaMedicos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un médico de la tabla");
                return;
            }
            
            // Validar campos
            if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Debe ingresar el nombre del médico",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtApellido.getText() == null || txtApellido.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Debe ingresar el apellido del médico",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtLicencia.getText() == null || txtLicencia.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Debe ingresar el número de registro del médico",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            Especialidad especialidadSeleccionada = (Especialidad) comboEspecialidad.getSelectedItem();
            
            if (especialidadSeleccionada == null) {
                JOptionPane.showMessageDialog(this, 
                    "Debe seleccionar una especialidad",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String telefono = (txtTelefono.getText() != null) ? txtTelefono.getText() : "";
            
            boolean exito = controlador.actualizarMedico(
                id,
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                txtLicencia.getText().trim(),
                especialidadSeleccionada,
                telefono.trim()
            );
            
            if (exito) {
                JOptionPane.showMessageDialog(this, "Médico actualizado exitosamente");
                limpiarCampos();
                cargarMedicos();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al actualizar el médico",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error: Índice fuera de rango - " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error al acceder a los datos de la tabla",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassCastException e) {
            System.err.println("Error: Tipo de dato incorrecto - " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al leer los datos de la tabla",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error inesperado al actualizar: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error inesperado: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarMedico() {
        try {
            int filaSeleccionada = tablaMedicos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un médico de la tabla");
                return;
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar este médico?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
                if (controlador.eliminarMedico(id)) {
                    JOptionPane.showMessageDialog(this, "Médico eliminado correctamente");
                    limpiarCampos();
                    cargarMedicos();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "No se pudo eliminar el médico",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error: Índice fuera de rango - " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error al acceder a los datos de la tabla",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error al eliminar médico: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error inesperado: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarMedicos() {
        try {
            modeloTabla.setRowCount(0);
            List<Medico> medicos = controlador.obtenerTodosLosMedicos();
            
            for (Medico m : medicos) {
                if (m != null && m.getEspecialidad() != null) {
                    Object[] fila = {
                        m.getId(),
                        (m.getNombre() != null) ? m.getNombre() : "",
                        (m.getApellido() != null) ? m.getApellido() : "",
                        (m.getLicencia() != null) ? m.getLicencia() : "",
                        m.getEspecialidad().getNombre(),
                        (m.getTelefono() != null) ? m.getTelefono() : ""
                    };
                    modeloTabla.addRow(fila);
                }
            }
            
        } catch (NullPointerException e) {
            System.err.println("Error: Dato nulo al cargar médicos - " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error al cargar médicos: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al cargar la lista de médicos",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarMedicoSeleccionado() {
        try {
            int fila = tablaMedicos.getSelectedRow();
            if (fila >= 0 && fila < modeloTabla.getRowCount()) {
                Object nombre = modeloTabla.getValueAt(fila, 1);
                Object apellido = modeloTabla.getValueAt(fila, 2);
                Object licencia = modeloTabla.getValueAt(fila, 3);
                Object especialidadNombre = modeloTabla.getValueAt(fila, 4);
                Object telefono = modeloTabla.getValueAt(fila, 5);
                
                txtNombre.setText((nombre != null) ? nombre.toString() : "");
                txtApellido.setText((apellido != null) ? apellido.toString() : "");
                txtLicencia.setText((licencia != null) ? licencia.toString() : "");
                txtTelefono.setText((telefono != null) ? telefono.toString() : "");
                
                if (especialidadNombre != null) {
                    String especialidad = especialidadNombre.toString();
                    for (Especialidad e : Especialidad.values()) {
                        if (e.getNombre().equals(especialidad)) {
                            comboEspecialidad.setSelectedItem(e);
                            break;
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error: Índice fuera de rango al cargar selección - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al cargar médico seleccionado: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void limpiarCampos() {
        try {
            txtNombre.setText("");
            txtApellido.setText("");
            txtLicencia.setText("");
            txtTelefono.setText("");
            comboEspecialidad.setSelectedIndex(0);
            tablaMedicos.clearSelection();
        } catch (Exception e) {
            System.err.println("Error al limpiar campos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}