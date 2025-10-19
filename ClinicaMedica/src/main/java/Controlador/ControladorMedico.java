package controlador;

import dao.MedicoDAO;
import modelo.Especialidad;
import modelo.Medico;
import java.util.List;

public class ControladorMedico {
    private MedicoDAO dao;
    
    public ControladorMedico() {
        this.dao = new MedicoDAO();
    }
    
    public boolean registrarMedico(String nombre, String apellido, String licencia,
                                   Especialidad especialidad, String telefono) {
        if (nombre.trim().isEmpty() || apellido.trim().isEmpty() || 
            licencia.trim().isEmpty() || especialidad == null) {
            return false;
        }
        
        Medico medico = new Medico(0, nombre, apellido, licencia, 
                                   especialidad, telefono);
        return dao.crear(medico);
    }
    
    public List<Medico> obtenerTodosLosMedicos() {
        return dao.obtenerTodos();
    }
    
    public boolean actualizarMedico(int id, String nombre, String apellido,
                                    String licencia, Especialidad especialidad, 
                                    String telefono) {
        if (nombre.trim().isEmpty() || apellido.trim().isEmpty() || 
            licencia.trim().isEmpty() || especialidad == null) {
            return false;
        }
        
        Medico medico = new Medico(id, nombre, apellido, licencia, 
                                   especialidad, telefono);
        return dao.actualizar(medico);
    }
    
    public boolean eliminarMedico(int id) {
        return dao.eliminar(id);
    }
}
