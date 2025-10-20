package controlador;

import dao.CitaDAO;
import modelo.Cita;
import modelo.Medico;
import modelo.Paciente;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ControladorCita {
    private CitaDAO dao;
    
    public ControladorCita() {
        this.dao = CitaDAO.obtenerInstancia(); // Usar instancia única
    }
    
    public boolean agendarCita(Paciente paciente, Medico medico, LocalDate fecha,
                               LocalTime hora, String motivo) {
        if (paciente == null || medico == null || fecha == null || 
            hora == null || motivo.trim().isEmpty()) {
            return false;
        }
        
        Cita cita = new Cita(0, paciente, medico, fecha, hora, motivo, "");
        return dao.crear(cita);
    }
    
    public List<Cita> obtenerTodasLasCitas() {
        return dao.obtenerTodas();
    }
    
    public boolean actualizarCita(int id, Paciente paciente, Medico medico,
                                  LocalDate fecha, LocalTime hora, String motivo,
                                  String diagnostico) {
        if (paciente == null || medico == null || fecha == null || 
            hora == null || motivo.trim().isEmpty()) {
            return false;
        }
        
        Cita cita = new Cita(id, paciente, medico, fecha, hora, 
                            motivo, diagnostico);
        return dao.actualizar(cita);
    }
    
    public boolean eliminarCita(int id) {
        return dao.eliminar(id);
    }
    
    public List<Cita> obtenerCitasPorPaciente(int pacienteId) {
        return dao.obtenerPorPaciente(pacienteId);
    }
}