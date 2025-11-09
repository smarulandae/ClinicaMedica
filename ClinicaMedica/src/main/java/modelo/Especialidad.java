package modelo;

public enum Especialidad {
    MEDICINA_GENERAL("Medicina General"),
    PEDIATRIA("Pediatría"),
    CARDIOLOGIA("Cardiología"),
    DERMATOLOGIA("Dermatología"),
    GINECOLOGIA("Ginecología"),
    TRAUMATOLOGIA("Traumatología"),
    RADIOLOGIA("Radiologia");
    
    private String nombre;
    
    Especialidad(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return nombre;
    }
}
