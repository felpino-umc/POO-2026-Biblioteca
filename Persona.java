public class Persona {
    // Atributo común
    protected String nombre;

    // Constructor parametrizado
    public Persona(String nombre) {
        this.nombre = nombre;
    }

    // Método toString()
    @Override
    public String toString() {
        return "Persona: " + nombre;
    }

    // Método presentarse()
    public void presentarse() {
        System.out.println("Hola, soy " + nombre);
    }
}