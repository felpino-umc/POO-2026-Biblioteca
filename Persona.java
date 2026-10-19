public abstract class Persona {
    // Atributo común
    private String nombre;

    // Constructor vacío (sobrecarga #1)
    public Persona() {
    }

    // Constructor parametrizado (sobrecarga #2)
    public Persona(String nombre) {
        this.nombre = nombre;
    }

    // Getter y setter
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
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

    public void mostrarInfo() {
        System.out.println(toString());
    }

    // Método abstracto: cada hija concreta decide cómo mostrar su rol.
    // Persona ya no se puede instanciar directamente (es abstract), y toda
    // clase que herede de ella está obligada a implementar este método.
    abstract void mostrarRol();
}