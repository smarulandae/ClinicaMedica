package controlador;

import dao.MedicoDAO;
import modelo.Especialidad;
import modelo.Medico;
import java.util.List;
import java.util.ArrayList;

public class ControladorMedico {
    private MedicoDAO dao;
    
    public ControladorMedico() {
        try {
            this.dao = MedicoDAO.obtenerInstancia();
        } catch (Exception e) {
            System.err.println("Error al inicializar MedicoDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public boolean registrarMedico(String nombre, String apellido, String licencia,
                                   Especialidad especialidad, String telefono) {
        try {
            // Validaciones de nulos
            if (nombre == null || apellido == null || licencia == null || especialidad == null) {
                System.err.println("Error: Datos nulos al registrar médico");
                return false;
            }
            
            // Validaciones de campos vacíos
            if (nombre.trim().isEmpty() || apellido.trim().isEmpty() || 
                licencia.trim().isEmpty()) {
                System.err.println("Error: Campos obligatorios vacíos");
                return false;
            }
            
            // Validar teléfono (puede ser vacío pero no nulo)
            if (telefono == null) {
                telefono = "";
            }
            
            Medico medico = new Medico(0, nombre, apellido, licencia, 
                                       especialidad, telefono);
            return dao.crear(medico);
            
        } catch (NullPointerException e) {
            System.err.println("Error: Referencia nula al registrar médico - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado al registrar médico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Medico> obtenerTodosLosMedicos() {
        try {
            return dao.obtenerTodos();
        } catch (Exception e) {
            System.err.println("Error al obtener todos los médicos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public boolean actualizarMedico(int id, String nombre, String apellido,
                                    String licencia, Especialidad especialidad, 
                                    String telefono) {
        try {
            // Validar ID
            if (id <= 0) {
                System.err.println("Error: ID inválido");
                return false;
            }
            
            // Validaciones de nulos
            if (nombre == null || apellido == null || licencia == null || especialidad == null) {
                System.err.println("Error: Datos nulos al actualizar médico");
                return false;
            }
            
            // Validaciones de campos vacíos
            if (nombre.trim().isEmpty() || apellido.trim().isEmpty() || 
                licencia.trim().isEmpty()) {
                System.err.println("Error: Campos obligatorios vacíos");
                return false;
            }
            
            if (telefono == null) {
                telefono = "";
            }
            
            Medico medico = new Medico(id, nombre, apellido, licencia, 
                                       especialidad, telefono);
            return dao.actualizar(medico);
            
        } catch (NullPointerException e) {
            System.err.println("Error: Referencia nula al actualizar médico - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado al actualizar médico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminarMedico(int id) {
        try {
            if (id <= 0) {
                System.err.println("Error: ID inválido para eliminar médico");
                return false;
            }
            return dao.eliminar(id);
        } catch (Exception e) {
            System.err.println("Error al eliminar médico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}