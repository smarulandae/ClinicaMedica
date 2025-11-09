package DAO;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import modelo.Medico;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO {
    private static MedicoDAO instancia;
    private List<Medico> medicos;
    private int siguienteId;
    private final Gson gson = new Gson();
    private static final String RUTA_ARCHIVO = "data/DatosMedicos.json";
    
    private MedicoDAO() {
        try {
            medicos = cargarMedicos();
            siguienteId = calcularSiguienteId();
        } catch (Exception e) {
            System.err.println("Error crítico al inicializar MedicoDAO: " + e.getMessage());
            e.printStackTrace();
            medicos = new ArrayList<>();
            siguienteId = 1;
        }
    }
    
    public static MedicoDAO obtenerInstancia() {
        try {
            if (instancia == null) {
                instancia = new MedicoDAO();
            }
            return instancia;
        } catch (Exception e) {
            System.err.println("Error al obtener instancia de MedicoDAO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo crear instancia de MedicoDAO", e);
        }
    }

    private List<Medico> cargarMedicos() {
        try {
            File archivo = new File(RUTA_ARCHIVO);
            if (!archivo.exists()) {
                System.out.println("Archivo de médicos no existe, creando nuevo...");
                File directorio = archivo.getParentFile();
                if (directorio != null && !directorio.exists()) {
                    if (!directorio.mkdirs()) {
                        System.err.println("Error: No se pudo crear el directorio data/");
                    }
                }
                guardarMedicos(new ArrayList<>());
                return new ArrayList<>();
            }
            
            try (FileReader reader = new FileReader(archivo)) {
                Type tipoLista = new TypeToken<ArrayList<Medico>>() {}.getType();
                List<Medico> lista = gson.fromJson(reader, tipoLista);
                return (lista != null) ? lista : new ArrayList<>();
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("Error: Archivo no encontrado - " + e.getMessage());
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error de I/O al cargar médicos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error inesperado al cargar médicos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    private void guardarMedicos(List<Medico> lista) {
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
            System.err.println("Error de I/O al guardar médicos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado al guardar médicos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean crear(Medico medico) {
        try {
            if (medico == null) {
                System.err.println("Error: Médico es nulo");
                return false;
            }
            
            if (medico.getLicencia() == null || existeLicencia(medico.getLicencia())) {
                System.err.println("Error: Licencia duplicada o nula");
                return false;
            }
            
            medico.setId(siguienteId++);
            medicos.add(medico);
            guardarMedicos(medicos);
            return true;
            
        } catch (NullPointerException e) {
            System.err.println("Error: Referencia nula al crear médico - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado al crear médico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Medico> obtenerTodos() {
        try {
            return new ArrayList<>(medicos);
        } catch (Exception e) {
            System.err.println("Error al obtener todos los médicos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public Medico obtenerPorId(int id) {
        try {
            for (Medico m : medicos) {
                if (m.getId() == id) {
                    return m;
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error al obtener médico por ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean actualizar(Medico medico) {
        try {
            if (medico == null) {
                System.err.println("Error: Médico es nulo");
                return false;
            }
            
            for (int i = 0; i < medicos.size(); i++) {
                if (medicos.get(i).getId() == medico.getId()) {
                    medicos.set(i, medico);
                    guardarMedicos(medicos);
                    return true;
                }
            }
            return false;
            
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error: Índice fuera de rango - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado al actualizar médico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminar(int id) {
        try {
            boolean eliminado = medicos.removeIf(m -> m.getId() == id);
            if (eliminado) {
                guardarMedicos(medicos);
            }
            return eliminado;
        } catch (Exception e) {
            System.err.println("Error al eliminar médico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean existeLicencia(String licencia) {
        try {
            if (licencia == null) {
                return false;
            }
            return medicos.stream()
                    .anyMatch(m -> m.getLicencia() != null && m.getLicencia().equals(licencia));
        } catch (Exception e) {
            System.err.println("Error al verificar licencia: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private int calcularSiguienteId() {
        try {
            return medicos.stream()
                    .mapToInt(Medico::getId)
                    .max()
                    .orElse(0) + 1;
        } catch (Exception e) {
            System.err.println("Error al calcular siguiente ID: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}