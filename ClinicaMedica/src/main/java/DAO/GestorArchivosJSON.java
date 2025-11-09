package DAO;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class GestorArchivosJSON {
    private static final String ARCHIVO = "data/DatosClinica.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Leer el contenido completo del JSON (estructura tipo Map)
    public static Map<String, Object> leerArchivo() {
        try (Reader reader = new FileReader(ARCHIVO)) {
            Type tipo = new TypeToken<Map<String, Object>>() {}.getType();
            return gson.fromJson(reader, tipo);
        } catch (FileNotFoundException e) {
            return new HashMap<>(); // si no existe el archivo, devolvemos vacío
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // Guardar la estructura completa en el archivo
    public static void guardarArchivo(Map<String, Object> datos) {
        try (Writer writer = new FileWriter(ARCHIVO)) {
            gson.toJson(datos, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
