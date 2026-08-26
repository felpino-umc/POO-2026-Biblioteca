public class Autor {
    
    // Atributos declarados correctamente con camelCase y sus tipos
    String nombre;
    String nacionalidad;
    int anioNacimiento;

    // Agregar constructor vacío para permitir la creación de objetos sin parámetros    
    public Autor() {
        // Constructor vacío
    }

    // Constructor para inicializar los atributos al crear el objeto
    public Autor(String nombre, String nacionalidad, int anioNacimiento) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.anioNacimiento = anioNacimiento;
    }

    // Método para mostrar la información por consola
    void mostrarInfo() {
        System.out.println(nombre + " | " + nacionalidad + " | " + anioNacimiento);
    }

    // Método para calcular la edad usando el año actual (2026)
    int calcularEdad() {
        int anioActual = 2026; 
        return anioActual - anioNacimiento;
    }
}