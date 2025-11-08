package DAO;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import modelo.Medico;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO {
    private static MedicoDAO instancia; // Instancia única
    private List<Medico> medicos;
    private int siguienteId;
    private final Gson gson = new Gson();
    private static final String RUTA_ARCHIVO = "data/DatosMedicos.json";
    
    // Constructor privado
    private MedicoDAO() {
        medicos = cargarMedicos();
        siguienteId = calcularSiguienteId();
    }
    
    // Método para obtener la única instancia
    public static MedicoDAO obtenerInstancia() {
        if (instancia == null) {
            instancia = new MedicoDAO();
        }
        return instancia;
    }

    private List<Medico> cargarMedicos() {
        File archivo = new File(RUTA_ARCHIVO);
        if(!archivo.exists()){
            archivo.getParentFile().mkdirs();
            guardarPacientes(new ArrayList<>());
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(archivo)){
            Type tipoLista = new TypeToken<ArrayList<Medico>>(){}.getType();
            List<Medico> lista = gson.fromJson(reader, tipoLista);
            return (lista != null) ? lista : new ArrayList<>();
        } catch (IOException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    private void guardarPacientes(List<Medico> lista) {
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean crear(Medico medico) {
        if (existeLicencia(medico.getLicencia())) {
            return false;
        }
        medico.setId(siguienteId++);
        medicos.add(medico);
        guardarPacientes(medicos);
        return true;
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
                guardarPacientes(medicos);
                return true;
            }
        }
        return false;
    }
    
    public boolean eliminar(int id) {
        boolean eliminado = medicos.removeIf(m -> m.getId() == id);
        if(eliminado){
            guardarPacientes(medicos);
        }
        return eliminado;
    }
    
    private boolean existeLicencia(String licencia) {
        return medicos.stream()
                .anyMatch(m -> m.getLicencia().equals(licencia));
    }

    private int calcularSiguienteId(){
        return medicos.stream()
                .mapToInt(Medico::getId)
                .max()
                .orElse(0) + 1;
    }
}