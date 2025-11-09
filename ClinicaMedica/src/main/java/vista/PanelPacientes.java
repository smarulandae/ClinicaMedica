package vista;

import Controlador.ControladorPaciente;
import modelo.Paciente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelPacientes extends JPanel {
    private ControladorPaciente controlador;
    private JTable tablaPacientes;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtApellido, txtCedula, txtTelefono, txtDireccion;
    private JButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar;
    
    public PanelPacientes() {
        try {
            controlador = new ControladorPaciente();
            inicializarComponentes();
            cargarPacientes();
        } catch (Exception e) {
            System.err.println("Error crítico al inicializar PanelPacientes: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al inicializar el panel de pacientes",
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
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Paciente"));
        
        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);
        
        panel.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        panel.add(txtApellido);
        
        panel.add(new JLabel("Cédula:"));
        txtCedula = new JTextField();
        panel.add(txtCedula);
        
        panel.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panel.add(txtTelefono);
        
        panel.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panel.add(txtDireccion);
        
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
        
        btnGuardar.addActionListener(e -> guardarPaciente());
        btnActualizar.addActionListener(e -> actualizarPaciente());
        btnEliminar.addActionListener(e -> eliminarPaciente());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        
        return panel;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Pacientes"));
        
        String[] columnas = {"ID", "Nombre", "Apellido", "Cédula", "Teléfono", "Dirección"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaPacientes = new JTable(modeloTabla);
        tablaPacientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tablaPacientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaPacientes.getSelectedRow() != -1) {
                cargarPacienteSeleccionado();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaPacientes);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void guardarPaciente() {
        try {
            // Validar campos obligatorios
            if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Debe ingresar el nombre del paciente",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtApellido.getText() == null || txtApellido.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Debe ingresar el apellido del paciente",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtCedula.getText() == null || txtCedula.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Debe ingresar la cédula del paciente",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!txtCedula.getText().matches("\\d+")) {
                JOptionPane.showMessageDialog(this, 
                    "La cédula solo puede contener números",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String telefono = (txtTelefono.getText() != null) ? txtTelefono.getText() : "";
            String direccion = (txtDireccion.getText() != null) ? txtDireccion.getText() : "";
            
            boolean exito = controlador.registrarPaciente(
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                txtCedula.getText().trim(),
                telefono.trim(),
                direccion.trim()
            );
            
            if (exito) {
                JOptionPane.showMessageDialog(this, "Paciente registrado exitosamente");
                limpiarCampos();
                cargarPacientes();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error: Verifique los datos o la cédula ya está registrada",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NullPointerException e) {
            System.err.println("Error: Campo nulo al guardar paciente - " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error: Algunos campos están vacíos",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error inesperado al guardar paciente: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error inesperado: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarPaciente() {
        try {
            int filaSeleccionada = tablaPacientes.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un paciente de la tabla");
                return;
            }
            
            // Validar campos obligatorios
            if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Debe ingresar el nombre del paciente",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtApellido.getText() == null || txtApellido.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Debe ingresar el apellido del paciente",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtCedula.getText() == null || txtCedula.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Debe ingresar la cédula del paciente",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            String telefono = (txtTelefono.getText() != null) ? txtTelefono.getText() : "";
            String direccion = (txtDireccion.getText() != null) ? txtDireccion.getText() : "";
            
            boolean exito = controlador.actualizarPaciente(
                id,
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                txtCedula.getText().trim(),
                telefono.trim(),
                direccion.trim()
            );
            
            if (exito) {
                JOptionPane.showMessageDialog(this, "Paciente actualizado exitosamente");
                limpiarCampos();
                cargarPacientes();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al actualizar el paciente",
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
    
    private void eliminarPaciente() {
        try {
            int filaSeleccionada = tablaPacientes.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un paciente de la tabla");
                return;
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar este paciente?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
                if (controlador.eliminarPaciente(id)) {
                    JOptionPane.showMessageDialog(this, "Paciente eliminado correctamente");
                    limpiarCampos();
                    cargarPacientes();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "No se pudo eliminar el paciente",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error: Índice fuera de rango - " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error al acceder a los datos de la tabla",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error al eliminar paciente: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error inesperado: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarPacientes() {
        try {
            modeloTabla.setRowCount(0);
            List<Paciente> pacientes = controlador.obtenerTodosLosPacientes();
            
            for (Paciente p : pacientes) {
                if (p != null) {
                    Object[] fila = {
                        p.getId(),
                        (p.getNombre() != null) ? p.getNombre() : "",
                        (p.getApellido() != null) ? p.getApellido() : "",
                        (p.getCedula() != null) ? p.getCedula() : "",
                        (p.getTelefono() != null) ? p.getTelefono() : "",
                        (p.getDireccion() != null) ? p.getDireccion() : ""
                    };
                    modeloTabla.addRow(fila);
                }
            }
            
        } catch (NullPointerException e) {
            System.err.println("Error: Dato nulo al cargar pacientes - " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error al cargar pacientes: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al cargar la lista de pacientes",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarPacienteSeleccionado() {
        try {
            int fila = tablaPacientes.getSelectedRow();
            if (fila >= 0 && fila < modeloTabla.getRowCount()) {
                Object nombre = modeloTabla.getValueAt(fila, 1);
                Object apellido = modeloTabla.getValueAt(fila, 2);
                Object cedula = modeloTabla.getValueAt(fila, 3);
                Object telefono = modeloTabla.getValueAt(fila, 4);
                Object direccion = modeloTabla.getValueAt(fila, 5);
                
                txtNombre.setText((nombre != null) ? nombre.toString() : "");
                txtApellido.setText((apellido != null) ? apellido.toString() : "");
                txtCedula.setText((cedula != null) ? cedula.toString() : "");
                txtTelefono.setText((telefono != null) ? telefono.toString() : "");
                txtDireccion.setText((direccion != null) ? direccion.toString() : "");
            }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error: Índice fuera de rango al cargar selección - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al cargar paciente seleccionado: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void limpiarCampos() {
        try {
            txtNombre.setText("");
            txtApellido.setText("");
            txtCedula.setText("");
            txtTelefono.setText("");
            txtDireccion.setText("");
            tablaPacientes.clearSelection();
        } catch (Exception e) {
            System.err.println("Error al limpiar campos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}