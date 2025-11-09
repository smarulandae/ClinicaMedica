package controlador;

import dao.CitaDAO;
import modelo.Cita;
import modelo.Medico;
import modelo.Paciente;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

public class ControladorCita {
    private CitaDAO dao;
    
    public ControladorCita() {
        try {
            this.dao = CitaDAO.obtenerInstancia();
        } catch (Exception e) {
            System.err.println("Error al inicializar CitaDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public boolean agendarCita(Paciente paciente, Medico medico, LocalDate fecha,
                               LocalTime hora, String motivo) {
        try {
            // Validaciones de nulos
            if (paciente == null || medico == null || fecha == null || 
                hora == null || motivo == null || motivo.trim().isEmpty()) {
                System.err.println("Error: Datos incompletos para agendar cita");
                return false;
            }
            
            Cita cita = new Cita(0, paciente, medico, fecha, hora, motivo, "");
            return dao.crear(cita);
            
        } catch (NullPointerException e) {
            System.err.println("Error: Referencia nula al agendar cita - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado al agendar cita: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Cita> obtenerTodasLasCitas() {
        try {
            return dao.obtenerTodas();
        } catch (Exception e) {
            System.err.println("Error al obtener todas las citas: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Retorna lista vacía en caso de error
        }
    }
    
    public boolean actualizarCita(int id, Paciente paciente, Medico medico,
                                  LocalDate fecha, LocalTime hora, String motivo,
                                  String diagnostico) {
        try {
            // Validaciones
            if (paciente == null || medico == null || fecha == null || 
                hora == null || motivo == null || motivo.trim().isEmpty()) {
                System.err.println("Error: Datos incompletos para actualizar cita");
                return false;
            }
            
            if (diagnostico == null) {
                diagnostico = ""; // Asignar valor por defecto
            }
            
            Cita cita = new Cita(id, paciente, medico, fecha, hora, 
                                motivo, diagnostico);
            return dao.actualizar(cita);
            
        } catch (NullPointerException e) {
            System.err.println("Error: Referencia nula al actualizar cita - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado al actualizar cita: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminarCita(int id) {
        try {
            if (id <= 0) {
                System.err.println("Error: ID inválido para eliminar cita");
                return false;
            }
            return dao.eliminar(id);
        } catch (Exception e) {
            System.err.println("Error al eliminar cita: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Cita> obtenerCitasPorPaciente(int pacienteId) {
        try {
            if (pacienteId <= 0) {
                System.err.println("Error: ID de paciente inválido");
                return new ArrayList<>();
            }
            return dao.obtenerPorPaciente(pacienteId);
        } catch (Exception e) {
            System.err.println("Error al obtener citas del paciente: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}