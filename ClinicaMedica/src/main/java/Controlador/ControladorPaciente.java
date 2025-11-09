package Controlador;

import modelo.Paciente;
import java.util.List;

import DAO.PacienteDAO;

import java.util.ArrayList;

public class ControladorPaciente {
    private PacienteDAO dao;
    
    public ControladorPaciente() {
        try {
            this.dao = PacienteDAO.obtenerInstancia();
        } catch (Exception e) {
            System.err.println("Error al inicializar PacienteDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public boolean registrarPaciente(String nombre, String apellido, String cedula,
                                     String telefono, String direccion) {
        try {
            // Validaciones de nulos
            if (nombre == null || apellido == null || cedula == null) {
                System.err.println("Error: Datos nulos al registrar paciente");
                return false;
            }
            
            // Validaciones de campos vacíos
            if (nombre.trim().isEmpty() || apellido.trim().isEmpty() || 
                cedula.trim().isEmpty()) {
                System.err.println("Error: Campos obligatorios vacíos");
                return false;
            }
            
            // Validar teléfono y dirección (pueden ser vacíos pero no nulos)
            if (telefono == null) {
                telefono = "";
            }
            if (direccion == null) {
                direccion = "";
            }
            
            Paciente paciente = new Paciente(0, nombre, apellido, cedula, 
                                             telefono, direccion);
            return dao.crear(paciente);
            
        } catch (NullPointerException e) {
            System.err.println("Error: Referencia nula al registrar paciente - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado al registrar paciente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Paciente> obtenerTodosLosPacientes() {
        try {
            return dao.obtenerTodos();
        } catch (Exception e) {
            System.err.println("Error al obtener todos los pacientes: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public boolean actualizarPaciente(int id, String nombre, String apellido,
                                      String cedula, String telefono, String direccion) {
        try {
            // Validar ID
            if (id <= 0) {
                System.err.println("Error: ID inválido");
                return false;
            }
            
            // Validaciones de nulos
            if (nombre == null || apellido == null || cedula == null) {
                System.err.println("Error: Datos nulos al actualizar paciente");
                return false;
            }
            
            // Validaciones de campos vacíos
            if (nombre.trim().isEmpty() || apellido.trim().isEmpty() || 
                cedula.trim().isEmpty()) {
                System.err.println("Error: Campos obligatorios vacíos");
                return false;
            }
            
            if (telefono == null) {
                telefono = "";
            }
            if (direccion == null) {
                direccion = "";
            }
            
            Paciente paciente = new Paciente(id, nombre, apellido, cedula, 
                                             telefono, direccion);
            return dao.actualizar(paciente);
            
        } catch (NullPointerException e) {
            System.err.println("Error: Referencia nula al actualizar paciente - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado al actualizar paciente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminarPaciente(int id) {
        try {
            if (id <= 0) {
                System.err.println("Error: ID inválido para eliminar paciente");
                return false;
            }
            return dao.eliminar(id);
        } catch (Exception e) {
            System.err.println("Error al eliminar paciente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}