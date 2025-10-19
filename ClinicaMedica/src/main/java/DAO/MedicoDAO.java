package dao;

import modelo.Medico;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para Médico
 * Aplica SRP: Solo maneja persistencia de médicos
 */
public class MedicoDAO {
    private List<Medico> medicos;
    private int siguienteId;
    
    public MedicoDAO() {
        medicos = new ArrayList<>();
        siguienteId = 1;
    }
    
    public boolean crear(Medico medico) {
        if (existeLicencia(medico.getLicencia())) {
            return false;
        }
        medico.setId(siguienteId++);
        return medicos.add(medico);
    }
    
    public List<Medico> obtenerTodos() {
        return new ArrayList<>(medicos);
    }
    
    public Medico obtenerPorId(int id) {
        for (Medico m : medicos) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }
    
    public boolean actualizar(Medico medico) {
        for (int i = 0; i < medicos.size(); i++) {
            if (medicos.get(i).getId() == medico.getId()) {
                medicos.set(i, medico);
                return true;
            }
        }
        return false;
    }
    
    public boolean eliminar(int id) {
        return medicos.removeIf(m -> m.getId() == id);
    }
    
    private boolean existeLicencia(String licencia) {
        return medicos.stream()
                .anyMatch(m -> m.getLicencia().equals(licencia));
    }
}
