public class Socio extends Persona {

    // Atributos propios de Socio (nombre se hereda de Persona)
    private int numeroSocio;
    private String email;
    private int librosPrestadosActuales;
    
    // Si mantienes el contador global de socios de C1:
    private static int totalSocios = 0;
    private static final int MAX_LIBROS = 3; // Límite típico de préstamos

    // Constructor parametrizado
    public Socio(String nombre, int numeroSocio, String email) {
        super(nombre); // Llama al constructor de Persona pasándole el nombre
        this.numeroSocio = numeroSocio;
        this.email = email;
        this.librosPrestadosActuales = 0;
        totalSocios++;
    }

    // Regla de negocio de la C1: verifica si el socio no excede el límite permitido
    public boolean puedeSolicitarPrestamo() {
        return this.librosPrestadosActuales < MAX_LIBROS;
    }

    // Métodos auxiliares para actualizar préstamos (útiles para el flujo de biblioteca)
    public void registrarPrestamo() {
        this.librosPrestadosActuales++;
    }

    public void registrarDevolucion() {
        if (this.librosPrestadosActuales > 0) {
            this.librosPrestadosActuales--;
        }
    }

    // Sobrescritura de toString() reutilizando la lógica de la clase padre
    @Override
    public String toString() {
        return super.toString() + " | Socio #" + this.numeroSocio 
                + " | " + this.email 
                + " | Libros prestados: " + this.librosPrestadosActuales;
    }

    // Getters y Setters
    public int getNumeroSocio() {
        return numeroSocio;
    }

    public void setNumeroSocio(int numeroSocio) {
        this.numeroSocio = numeroSocio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLibrosPrestadosActuales() {
        return librosPrestadosActuales;
    }

    public static int getTotalSocios() {
        return totalSocios;
    }
}