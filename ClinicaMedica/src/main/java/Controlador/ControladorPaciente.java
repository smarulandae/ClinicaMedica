package controlador;

import dao.PacienteDAO;
import modelo.Paciente;
import java.util.List;

public class ControladorPaciente {
    private PacienteDAO dao;
    
    public ControladorPaciente() {
        this.dao = new PacienteDAO();
    }
    
    public boolean registrarPaciente(String nombre, String apellido, String cedula,
                                     String telefono, String direccion) {
        // Validaciones
        if (nombre.trim().isEmpty() || apellido.trim().isEmpty() || 
            cedula.trim().isEmpty()) {
            return false;
        }
        
        Paciente paciente = new Paciente(0, nombre, apellido, cedula, 
                                         telefono, direccion);
        return dao.crear(paciente);
    }
    
    public List<Paciente> obtenerTodosLosPacientes() {
        return dao.obtenerTodos();
    }
    
    public boolean actualizarPaciente(int id, String nombre, String apellido,
                                      String cedula, String telefono, String direccion) {
        if (nombre.trim().isEmpty() || apellido.trim().isEmpty() || 
            cedula.trim().isEmpty()) {
            return false;
        }
        
        Paciente paciente = new Paciente(id, nombre, apellido, cedula, 
                                         telefono, direccion);
        return dao.actualizar(paciente);
    }
    
    public boolean eliminarPaciente(int id) {
        return dao.eliminar(id);
    }
}
