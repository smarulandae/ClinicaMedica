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
        try {
            pacientes = cargarPacientes();
            siguienteId = calcularSiguienteId();
        } catch (Exception e) {
            System.err.println("Error crítico al inicializar PacienteDAO: " + e.getMessage());
            e.printStackTrace();
            pacientes = new ArrayList<>();
            siguienteId = 1;
        }
    }

    public static PacienteDAO obtenerInstancia() {
        try {
            if (instancia == null) {
                instancia = new PacienteDAO();
            }
            return instancia;
        } catch (Exception e) {
            System.err.println("Error al obtener instancia de PacienteDAO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo crear instancia de PacienteDAO", e);
        }
    }

    private List<Paciente> cargarPacientes() {
        try {
            File archivo = new File(RUTA_ARCHIVO);
            if (!archivo.exists()) {
                System.out.println("Archivo de pacientes no existe, creando nuevo...");
                File directorio = archivo.getParentFile();
                if (directorio != null && !directorio.exists()) {
                    if (!directorio.mkdirs()) {
                        System.err.println("Error: No se pudo crear el directorio data/");
                    }
                }
                guardarPacientes(new ArrayList<>());
                return new ArrayList<>();
            }

            try (FileReader reader = new FileReader(archivo)) {
                Type tipoLista = new TypeToken<ArrayList<Paciente>>() {}.getType();
                List<Paciente> lista = gson.fromJson(reader, tipoLista);
                return (lista != null) ? lista : new ArrayList<>();
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("Error: Archivo no encontrado - " + e.getMessage());
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error de I/O al cargar pacientes: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error inesperado al cargar pacientes: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void guardarPacientes(List<Paciente> lista) {
        try {
            File archivo = new File(RUTA_ARCHIVO);
            File directorio = archivo.getParentFile();
            
            if (directorio != null && !directorio.exists()) {
                if (!directorio.mkdirs()) {
                    System.err.println("Error: No se pudo crear el directorio");
                    return;
                }
            }
            
            try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
                gson.toJson(lista, writer);
            }
            
        } catch (IOException e) {
            System.err.println("Error de I/O al guardar pacientes: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado al guardar pacientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean crear(Paciente paciente) {
        try {
            if (paciente == null) {
                System.err.println("Error: Paciente es nulo");
                return false;
            }
            
            if (paciente.getCedula() == null || existeCedula(paciente.getCedula())) {
                System.err.println("Error: Cédula duplicada o nula");
                return false;
            }
            
            paciente.setId(siguienteId++);
            pacientes.add(paciente);
            guardarPacientes(pacientes);
            return true;
            
        } catch (NullPointerException e) {
            System.err.println("Error: Referencia nula al crear paciente - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado al crear paciente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Paciente> obtenerTodos() {
        try {
            return new ArrayList<>(pacientes);
        } catch (Exception e) {
            System.err.println("Error al obtener todos los pacientes: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Paciente obtenerPorId(int id) {
        try {
            for (Paciente p : pacientes) {
                if (p.getId() == id) {
                    return p;
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error al obtener paciente por ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean actualizar(Paciente paciente) {
        try {
            if (paciente == null) {
                System.err.println("Error: Paciente es nulo");
                return false;
            }
            
            for (int i = 0; i < pacientes.size(); i++) {
                if (pacientes.get(i).getId() == paciente.getId()) {
                    pacientes.set(i, paciente);
                    guardarPacientes(pacientes);
                    return true;
                }
            }
            return false;
            
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error: Índice fuera de rango - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado al actualizar paciente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        try {
            boolean eliminado = pacientes.removeIf(p -> p.getId() == id);
            if (eliminado) {
                guardarPacientes(pacientes);
            }
            return eliminado;
        } catch (Exception e) {
            System.err.println("Error al eliminar paciente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean existeCedula(String cedula) {
        try {
            if (cedula == null) {
                return false;
            }
            return pacientes.stream()
                    .anyMatch(p -> p.getCedula() != null && p.getCedula().equals(cedula));
        } catch (Exception e) {
            System.err.println("Error al verificar cédula: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private int calcularSiguienteId() {
        try {
            return pacientes.stream()
                    .mapToInt(Paciente::getId)
                    .max()
                    .orElse(0) + 1;
        } catch (Exception e) {
            System.err.println("Error al calcular siguiente ID: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}