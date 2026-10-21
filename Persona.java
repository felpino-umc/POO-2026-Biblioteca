/*
 * Persona es abstracta porque una "persona" genérica no existe en el dominio de
 * la biblioteca: siempre es un Autor o un Socio. Si fuera concreta se podría
 * hacer new Persona(...) y quedaría un objeto sin rol, sin sentido. Con abstract
 * el compilador lo impide y además obliga a cada subclase a implementar
 * mostrarRol().
 */
public abstract class Persona {
    // Atributo común
    private String nombre;

    // Constructor vacío (sobrecarga #1)
    public Persona() {
    }

    // Constructor parametrizado (sobrecarga #2)
    public Persona(String nombre) {
        setNombre(nombre); // reutiliza la validación del setter
    }

    // Getter y setter
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
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

    // Método abstracto: cada hija concreta decide cómo mostrar su rol.
    // Persona ya no se puede instanciar directamente (es abstract), y toda
    // clase que herede de ella está obligada a implementar este método.
    abstract void mostrarRol();
}
