import java.time.LocalDate;

public class Prestamo {

    // --- ATRIBUTOS ---
    private Libro libro;
    private Socio socio;
    private LocalDate fechaPrestamo;
    private boolean devuelto = false;
    private boolean activo = false;

    // -- CONSTRUCTOR ---
    public Prestamo(Libro libro, Socio socio, LocalDate fechaPrestamo) {

        if (libro == null || socio == null || fechaPrestamo == null) {
            throw new IllegalArgumentException("NINGUN CAMPO PUEDE SER NULO.");
        }
        this.libro = libro;
        this.socio = socio;
        setFechaPrestamo(fechaPrestamo); // reutiliza la validación del setter
    }

    // Getters y setters

    public Libro getLibro() {
        return libro;
    }

    public Socio getSocio() {
        return socio;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public boolean isActivo() {
        return activo;
    }

    // Privado: la fecha solo se asigna al crear el préstamo.
    // no se modifica desde afuera.
    private void setFechaPrestamo(LocalDate fechaPrestamo) {
        if (fechaPrestamo == null || fechaPrestamo.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de préstamo no puede ser nula ni ser del futuro.");
        }
        this.fechaPrestamo = fechaPrestamo;
    }

    // Método para registrar el préstamo
    public void registrarPrestamo() {

        if (activo) {
            System.out.println("Este préstamo ya está activo.");
            return;
        }

        if (devuelto) {
            System.out.println("Este préstamo ya fue realizado.");
            return;
        }

        if (!libro.isDisponible()) {
            System.out.println("El libro no está disponible.");
            return;
        }

        if (!socio.puedeSolicitarPrestamo()) {
            System.out.println("El socio alcanzó el máximo de préstamos.");
            return;
        }

        libro.prestar();
        socio.registrarPrestamo();
        activo = true;
    }

    // Método para registrar la devolución
    public void registrarDevolucion() {

        if (activo && !devuelto) {

            libro.devolver();
            socio.registrarDevolucion();

            devuelto = true;
            activo = false;
        } else {
            System.out.println("El préstamo no está activo o ya fue devuelto.");
        }
    }

    // Método para mostrar la información en consola
    public void mostrarInfo() {
        System.out.println("Préstamo de libro: " + libro.getTitulo());
        System.out.println("Socio: " + socio.getNombre());
        System.out.println("Fecha de préstamo: " + fechaPrestamo);
        System.out.println("Estado: " + (devuelto ? "Devuelto" : "No devuelto"));
    }
}
