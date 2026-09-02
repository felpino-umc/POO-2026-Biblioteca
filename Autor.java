import java.time.Year;

public class Autor extends Persona {
    // Atributos propios de Autor
    private String nacionalidad;
    private int anioNacimiento;

    // Constructor parametrizado
    public Autor(String nombre, String nacionalidad, int anioNacimiento) {
        super(nombre); // Llama al constructor de Persona para asignar 'nombre'
        this.nacionalidad = nacionalidad;
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
}