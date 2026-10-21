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
        setNacionalidad(nacionalidad); // reutiliza la validación del setter
        setAnioNacimiento(anioNacimiento); // reutiliza la validación del setter
    }

    // Getters y setters
    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        if (nacionalidad == null || nacionalidad.isBlank()) {
            throw new IllegalArgumentException("La nacionalidad no puede estar vacía");
        }
        this.nacionalidad = nacionalidad;
    }

    public int getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(int anioNacimiento) {
        if (anioNacimiento < 1 || anioNacimiento > Year.now().getValue()) {
            throw new IllegalArgumentException(
                    "El año de nacimiento debe estar entre 1 y " + Year.now().getValue());
        }
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
