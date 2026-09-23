import java.time.Year;

public class Autor extends Persona {
    // Atributos propios de Autor
    private String nacionalidad;
    private int anioNacimiento;

    // Constructor vacío (sobrecarga #1)
    public Autor() {
        super();
    }

    // Constructor parametrizado (sobrecarga #2)
    public Autor(String nombre, String nacionalidad, int anioNacimiento) {
        super(nombre); // Llama al constructor de Persona para asignar 'nombre'
        this.nacionalidad = nacionalidad;
        this.anioNacimiento = anioNacimiento;
    }

    // Getters y setters
    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public int getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(int anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    // Cálculo de edad
    public int calcularEdad() {
        int anioActual = Year.now().getValue();
        return anioActual - this.anioNacimiento;
    }

    // Sobrescritura de toString() reutilizando la clase madre (Persona)
    @Override
    public String toString() {
        return super.toString() + " | " + nacionalidad + " | " + anioNacimiento;
    }

    // Implementación del método abstracto de Persona: comportamiento propio de Autor
    @Override
    void mostrarRol() {
        System.out.println(getNombre() + " participa como AUTOR: escribe los libros del catálogo.");
    }
}