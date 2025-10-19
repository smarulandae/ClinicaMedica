package dao;

import modelo.Paciente;
import java.util.ArrayList;
import java.util.List;

/*
 * Clase de acceso a datos para Paciente
 */

public class PacienteDAO {
    private List<Paciente> pacientes;
    private int siguienteId;
    
    public PacienteDAO() {
        pacientes = new ArrayList<>();
        siguienteId = 1;
    }
    
    // Crear paciente
    public boolean crear(Paciente paciente) {
        // Validar que no exista la cédula
        if (existeCedula(paciente.getCedula())) {
            return false;
        }
        paciente.setId(siguienteId++);
        return pacientes.add(paciente);
    }
    
    // Leer todos los pacientes
    public List<Paciente> obtenerTodos() {
        return new ArrayList<>(pacientes);
    }
    
    // Leer paciente por ID
    public Paciente obtenerPorId(int id) {
        for (Paciente p : pacientes) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    
    // Actualizar paciente
    public boolean actualizar(Paciente paciente) {
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getId() == paciente.getId()) {
                pacientes.set(i, paciente);
                return true;
            }
        }
        return false;
    }
    
    // Eliminar paciente
    public boolean eliminar(int id) {
        return pacientes.removeIf(p -> p.getId() == id);
    }
    
    // Validar si existe cédula
    private boolean existeCedula(String cedula) {
        return pacientes.stream()
                .anyMatch(p -> p.getCedula().equals(cedula));
    }
}
