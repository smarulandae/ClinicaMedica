package DAO;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import modelo.Paciente;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {
    private static PacienteDAO instancia;
    private List<Paciente> pacientes;
    private int siguienteId;
    private final Gson gson = new Gson();
    private static final String RUTA_ARCHIVO = "data/DatosPacientes.json";

    private PacienteDAO() {
        pacientes = cargarPacientes();
        siguienteId = calcularSiguienteId();
    }

    public static PacienteDAO obtenerInstancia() {
        if (instancia == null) {
            instancia = new PacienteDAO();
        }
        return instancia;
    }

    private List<Paciente> cargarPacientes() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            archivo.getParentFile().mkdirs(); // Crea carpeta /data si no existe
            guardarPacientes(new ArrayList<>()); // Crea archivo vacío
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<ArrayList<Paciente>>() {}.getType();
            List<Paciente> lista = gson.fromJson(reader, tipoLista);
            return (lista != null) ? lista : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void guardarPacientes(List<Paciente> lista) {
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean crear(Paciente paciente) {
        if (existeCedula(paciente.getCedula())) {
            return false;
        }
        paciente.setId(siguienteId++);
        pacientes.add(paciente);
        guardarPacientes(pacientes);
        return true;
    }

    public List<Paciente> obtenerTodos() {
        return new ArrayList<>(pacientes);
    }

    public Paciente obtenerPorId(int id) {
        for (Paciente m : pacientes) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    public boolean actualizar(Paciente paciente) {
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getId() == paciente.getId()) {
                pacientes.set(i, paciente);
                guardarPacientes(pacientes);
                return true;
            }
        }
        return false;
    }

    public boolean eliminar(int id) {
        boolean eliminado = pacientes.removeIf(p -> p.getId() == id);
        if (eliminado) {
            guardarPacientes(pacientes);
        }
        return eliminado;
    }

    private boolean existeCedula(String cedula) {
        return pacientes.stream()
                .anyMatch(p -> p.getCedula().equals(cedula));
    }

    private int calcularSiguienteId() {
        return pacientes.stream()
                .mapToInt(Paciente::getId)
                .max()
                .orElse(0) + 1;
    }
}
