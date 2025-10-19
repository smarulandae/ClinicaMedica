package modelo;

public class Medico {
    private int id;
    private String nombre;
    private String apellido;
    private String licencia;
    private Especialidad especialidad;
    private String telefono;
    
    public Medico() {}
    
    public Medico(int id, String nombre, String apellido, String licencia,
                  Especialidad especialidad, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.licencia = licencia;
        this.especialidad = especialidad;
        this.telefono = telefono;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getLicencia() { return licencia; }
    public void setLicencia(String licencia) { this.licencia = licencia; }
    
    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { 
        this.especialidad = especialidad; 
    }
    
    //Métodos
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    @Override
    public String toString() {
        return "Dr(a). " + nombre + " " + apellido + " - " + especialidad.getNombre();
    }
}
