package dao;

import modelo.Cita;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public class CitaDAO {
    private static CitaDAO instancia;
    private List<Cita> citas;
    private int siguienteId;
    private final Gson gson;
    private static final String RUTA_ARCHIVO = "data/DatosCitas.json";

    private CitaDAO() {
        try {
            gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, context) ->
                        new com.google.gson.JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, context) ->
                        LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
                .registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (time, type, context) ->
                        new com.google.gson.JsonPrimitive(time.format(DateTimeFormatter.ISO_LOCAL_TIME)))
                .registerTypeAdapter(LocalTime.class, (JsonDeserializer<LocalTime>) (json, type, context) ->
                        LocalTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_TIME))
                .setPrettyPrinting()
                .create();
            
            citas = cargarCitas();
            siguienteId = calcularSiguienteId();
            
        } catch (Exception e) {
            System.err.println("Error crítico al inicializar CitaDAO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo inicializar CitaDAO", e);
        }
    }
    
    public static CitaDAO obtenerInstancia() {
        try {
            if (instancia == null) {
                instancia = new CitaDAO();
            }
            return instancia;
        } catch (Exception e) {
            System.err.println("Error al obtener instancia de CitaDAO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo crear instancia de CitaDAO", e);
        }
    }
    
    private List<Cita> cargarCitas() {
        try {
            File archivo = new File(RUTA_ARCHIVO);
            if (!archivo.exists()) {
                System.out.println("Archivo de citas no existe, creando nuevo...");
                File directorio = archivo.getParentFile();
                if (directorio != null && !directorio.exists()) {
                    if (!directorio.mkdirs()) {
                        System.err.println("Error: No se pudo crear el directorio data/");
                    }
                }
                guardarCitas(new ArrayList<>());
                return new ArrayList<>();
            }

            try (FileReader reader = new FileReader(archivo)) {
                Type tipoLista = new TypeToken<ArrayList<Cita>>() {}.getType();
                List<Cita> lista = gson.fromJson(reader, tipoLista);
                return (lista != null) ? lista : new ArrayList<>();
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("Error: Archivo no encontrado - " + e.getMessage());
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error de I/O al cargar citas: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error inesperado al cargar citas: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void guardarCitas(List<Cita> lista) {
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
            System.err.println("Error de I/O al guardar citas: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado al guardar citas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean crear(Cita cita) {
        try {
            if (cita == null) {
                System.err.println("Error: Cita es nula");
                return false;
            }
            
            if (existeCita(cita)) {
                System.err.println("Error: Cita duplicada");
                return false;
            }
            
            cita.setId(siguienteId++);
            citas.add(cita);
            guardarCitas(citas);
            return true;
            
        } catch (NullPointerException e) {
            System.err.println("Error: Referencia nula al crear cita - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado al crear cita: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Cita> obtenerTodas() {
        try {
            return new ArrayList<>(citas);
        } catch (Exception e) {
            System.err.println("Error al obtener todas las citas: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public Cita obtenerPorId(int id) {
        try {
            for (Cita c : citas) {
                if (c.getId() == id) {
                    return c;
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error al obtener cita por ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean actualizar(Cita cita) {
        try {
            if (cita == null) {
                System.err.println("Error: Cita es nula");
                return false;
            }
            
            for (int i = 0; i < citas.size(); i++) {
                if (citas.get(i).getId() == cita.getId()) {
                    citas.set(i, cita);
                    guardarCitas(citas);
                    return true;
                }
            }
            return false;
            
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error: Índice fuera de rango - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado al actualizar cita: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminar(int id) {
        try {
            boolean eliminado = citas.removeIf(c -> c.getId() == id);
            if (eliminado) {
                guardarCitas(citas);
            }
            return eliminado;
        } catch (Exception e) {
            System.err.println("Error al eliminar cita: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Cita> obtenerPorPaciente(int pacienteId) {
        try {
            return citas.stream()
                    .filter(c -> c.getPaciente() != null && c.getPaciente().getId() == pacienteId)
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            System.err.println("Error: Referencia nula en filtrado - " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error al obtener citas por paciente: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    private boolean existeCita(Cita cita) {
        try {
            if (cita == null || cita.getMedico() == null || 
                cita.getFecha() == null || cita.getHora() == null) {
                return false;
            }
            
                    return citas.stream()
                .anyMatch(c ->
                        (c.getMedico().getId() == cita.getMedico().getId()
                        && c.getFecha().equals(cita.getFecha())
                        && c.getHora().equals(cita.getHora()))
                        ||
                        (c.getPaciente().getId() == cita.getPaciente().getId()
                        && c.getFecha().equals(cita.getFecha())
                        && c.getHora().equals(cita.getHora()))
                );
        } catch (Exception e) {
            System.err.println("Error al verificar existencia de cita: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private int calcularSiguienteId() {
        try {
            return citas.stream()
                    .mapToInt(Cita::getId)
                    .max()
                    .orElse(0) + 1;
        } catch (Exception e) {
            System.err.println("Error al calcular siguiente ID: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}