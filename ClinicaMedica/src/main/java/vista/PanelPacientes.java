package vista;

import controlador.ControladorPaciente;
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
        controlador = new ControladorPaciente();
        inicializarComponentes();
        cargarPacientes();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de formulario
        JPanel panelFormulario = crearPanelFormulario();
        add(panelFormulario, BorderLayout.NORTH);
        
        // Panel de tabla
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
        
        // Evento de selección
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
        boolean exito = controlador.registrarPaciente(
            txtNombre.getText(),
            txtApellido.getText(),
            txtCedula.getText(),
            txtTelefono.getText(),
            txtDireccion.getText()
        );
        
        if (exito) {
            JOptionPane.showMessageDialog(this, "Paciente registrado exitosamente");
            limpiarCampos();
            cargarPacientes();
        } else {
            JOptionPane.showMessageDialog(this, "Error: Verifique los datos o cédula duplicada",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarPaciente() {
        int filaSeleccionada = tablaPacientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente");
            return;
        }
        
        int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        boolean exito = controlador.actualizarPaciente(
            id,
            txtNombre.getText(),
            txtApellido.getText(),
            txtCedula.getText(),
            txtTelefono.getText(),
            txtDireccion.getText()
        );
        
        if (exito) {
            JOptionPane.showMessageDialog(this, "Paciente actualizado exitosamente");
            limpiarCampos();
            cargarPacientes();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarPaciente() {
        int filaSeleccionada = tablaPacientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente");
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar este paciente?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            if (controlador.eliminarPaciente(id)) {
                JOptionPane.showMessageDialog(this, "Paciente eliminado");
                limpiarCampos();
                cargarPacientes();
            }
        }
    }
    
    private void cargarPacientes() {
        modeloTabla.setRowCount(0);
        List<Paciente> pacientes = controlador.obtenerTodosLosPacientes();
        
        for (Paciente p : pacientes) {
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                p.getApellido(),
                p.getCedula(),
                p.getTelefono(),
                p.getDireccion()
            };
            modeloTabla.addRow(fila);
        }
    }
    
    private void cargarPacienteSeleccionado() {
        int fila = tablaPacientes.getSelectedRow();
        txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtApellido.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtCedula.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtTelefono.setText(modeloTabla.getValueAt(fila, 4).toString());
        txtDireccion.setText(modeloTabla.getValueAt(fila, 5).toString());
    }
    
    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtCedula.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        tablaPacientes.clearSelection();
    }
}
