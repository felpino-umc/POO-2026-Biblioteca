public class Socio extends Persona implements Buscable {

    // Atributos propios de Socio (nombre se hereda de Persona)
    private final int numeroSocio; // final: identifica al socio y no cambia después de crearlo
    private String email;
    private int librosPrestadosActuales;

    // Si mantienes el contador global de socios de C1:
    private static int totalSocios = 0; //static significa que el atributo pertenece a la clase y no al objeto.
    // static: una sola copia para toda la clase; final: su valor no cambia. Es una regla común a todos los socios.
    private static final int MAX_LIBROS = 3;

    // No hay constructor vacío: numeroSocio es final y debe asignarse siempre al crear el socio.
    public Socio(String nombre, int numeroSocio, String email) {
        super(nombre); // Llama al constructor de Persona pasándole el nombre
        this.numeroSocio = numeroSocio;
        setEmail(email); // reutiliza la validación del setter
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("El email no puede estar vacío y debe contener '@'");
        }
        this.email = email;
    }

    public int getLibrosPrestadosActuales() {
        return librosPrestadosActuales;
    }

    public void setLibrosPrestadosActuales(int librosPrestadosActuales) {
        if (librosPrestadosActuales < 0 || librosPrestadosActuales > MAX_LIBROS) {
            throw new IllegalArgumentException(
                    "Los libros prestados deben estar entre 0 y " + MAX_LIBROS);
        }
        this.librosPrestadosActuales = librosPrestadosActuales;
    }

    public static int getTotalSocios() {
        return totalSocios;
    }

    // Implementación del método abstracto de Persona: comportamiento propio de Socio
    @Override
    public void mostrarRol() {
        System.out.println(getNombre() + " participa como SOCIO #" + numeroSocio
                + ": puede solicitar préstamos en la biblioteca.");
    }

    // Implementación de Buscable: un socio se encuentra por nombre o email
    @Override
    public boolean coincideCon(String termino) {
        String buscado = termino.toLowerCase();
        return getNombre().toLowerCase().contains(buscado)
                || email.toLowerCase().contains(buscado);
    }
}