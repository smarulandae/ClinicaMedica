package dao;

import modelo.Cita;
//import java.time.LocalDate;
//import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CitaDAO {
    private List<Cita> citas;
    private int siguienteId;
    
    public CitaDAO() {
        citas = new ArrayList<>();
        siguienteId = 1;
    }
    
    public boolean crear(Cita cita) {
        // Validar que no haya cita duplicada (mismo médico, fecha y hora)
        if (existeCita(cita)) {
            return false;
        }
        cita.setId(siguienteId++);
        return citas.add(cita);
    }
    
    public List<Cita> obtenerTodas() {
        return new ArrayList<>(citas);
    }
    
    public Cita obtenerPorId(int id) {
        for (Cita c : citas) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }
    
    public boolean actualizar(Cita cita) {
        for (int i = 0; i < citas.size(); i++) {
            if (citas.get(i).getId() == cita.getId()) {
                citas.set(i, cita);
                return true;
            }
        }
        return false;
    }
    
    public boolean eliminar(int id) {
        return citas.removeIf(c -> c.getId() == id);
    }
    
    // Obtener citas por paciente
    public List<Cita> obtenerPorPaciente(int pacienteId) {
        return citas.stream()
                .filter(c -> c.getPaciente().getId() == pacienteId)
                .collect(Collectors.toList());
    }
    
    // Validar cita duplicada
    private boolean existeCita(Cita cita) {
        return citas.stream()
                .anyMatch(c -> c.getMedico().getId() == cita.getMedico().getId()
                        && c.getFecha().equals(cita.getFecha())
                        && c.getHora().equals(cita.getHora()));
    }
}
