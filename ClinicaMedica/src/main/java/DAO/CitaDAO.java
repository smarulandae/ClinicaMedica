package DAO;

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
    private static CitaDAO instancia; // Instancia única
    private List<Cita> citas;
    private int siguienteId;
    private final Gson gson = new GsonBuilder()
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
    private static final String RUTA_ARCHIVO = "data/DatosCitas.json";

    
    // Constructor privado
    private CitaDAO() {
        citas = cargarCitas();
        siguienteId = calcularSiguienteId();
    }
    
    // Método para obtener la única instancia
    public static CitaDAO obtenerInstancia() {
        if (instancia == null) {
            instancia = new CitaDAO();
        }
        return instancia;
    }
    
    private List<Cita> cargarCitas(){
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            archivo.getParentFile().mkdirs(); // Crea carpeta /data si no existe
            guardarCitas(new ArrayList<>()); // Crea archivo vacío
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<ArrayList<Cita>>() {}.getType();
            List<Cita> lista = gson.fromJson(reader, tipoLista);
            return (lista != null) ? lista : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void guardarCitas(List<Cita> lista){
        try(FileWriter writer = new FileWriter(RUTA_ARCHIVO)){
            gson.toJson(lista, writer);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public boolean crear(Cita cita) {
        // Validar que no haya cita duplicada (mismo médico, fecha y hora)
        if (existeCita(cita)) {
            return false;
        }
        cita.setId(siguienteId++);
        citas.add(cita);
        guardarCitas(citas);
        return true;
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
                guardarCitas(citas);
                return true;
            }
        }
        return false;
    }
    
    public boolean eliminar(int id) {
        boolean eliminado = citas.removeIf(c -> c.getId() == id);
        if (eliminado){
            guardarCitas(citas);
        }
        return eliminado;
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

    private int calcularSiguienteId() {
        return citas.stream()
                .mapToInt(Cita::getId)
                .max()
                .orElse(0) + 1;
    }
}