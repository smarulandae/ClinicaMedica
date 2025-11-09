abstract class Animal {
    // Constructor de la clase abstracta
    Animal() {
        System.out.println("Constructor de Animal");
        hacerSonido(); // Se llama al método abstracto
    }

    abstract void hacerSonido(); // Método abstracto
}

class Perro extends Animal {
    String sonido = "Guau";

    Perro() {
        System.out.println("Constructor de Perro");
    }

    @Override
    void hacerSonido() {
        System.out.println("El perro dice: " + sonido.toUpperCase());
    }
}

public class Main {
    public static void main(String[] args) {
        new Perro();
    }
}
