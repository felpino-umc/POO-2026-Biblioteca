import java.time.LocalDate;

public class Prestamo {

    // --- ATRIBUTOS ---
    // RELACIÓN: asociación — Prestamo referencia a Libro y Socio
    // (ambos existen por sí mismos antes y después del préstamo; el préstamo
    // solo los vincula, no los crea ni los posee). Son final: un préstamo no
    // cambia de libro ni de socio una vez creado.
    private final Libro libro;
    private final Socio socio;
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

    // Protección de estado mutable (Cap. 4): Prestamo ya no expone getLibro()
    // ni getSocio(). Con esos getters, quien tuviera un préstamo podía hacer
    // getLibro().prestar() o getSocio().registrarPrestamo() y desincronizar el
    // estado. Ahora solo entrega los datos que se necesitan leer.
    public String getTituloLibro() {
        return libro.getTitulo();
    }

    public String getNombreSocio() {
        return socio.getNombre();
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    // Reemplaza la comparación prestamo.getLibro() == libro que hacía Biblioteca.
    public boolean esDelLibro(Libro libro) {
        return this.libro == libro;
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

    // SRP (S2): mostrarInfo() se movió a ReporteBiblioteca.mostrarPrestamo(...).
    // Prestamo solo registra el ciclo de vida del préstamo.
}
