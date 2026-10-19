/* package POO-2026-Biblioteca;*/

import java.time.LocalDate;

public class Prestamo {

    // --- ATRIBUTOS ---
    private Libro libro; 
    private Socio socio; 
    private LocalDate fechaPrestamo;
    private boolean devuelto = false; // esto para que cuando se cree un préstamo, por defecto no esté devuelto.
    private boolean activo = false;

    // -- CONSTRUCTOR ---
    public Prestamo(Libro libro, Socio socio, LocalDate fechaPrestamo) {

        if(libro == null || socio == null || fechaPrestamo == null) {
            throw new IllegalArgumentException("NINGUN CAMPO PUEDE SER NULO.");
        } 
       
        // un sout () return.
        // no cancela la creacion del objeto invalido.
        this.libro = libro;
        this.socio = socio;
        this.fechaPrestamo = fechaPrestamo;
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

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        if (fechaPrestamo == null || fechaPrestamo.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de préstamo no puede ser nula ni ser del futuro.");
        }
        this.fechaPrestamo = fechaPrestamo;

        // --LocalDate una joya de libreria. aca sirve el metodo isAfter
        // Y en las creaciones de prestamos del Main usamos LocalDate.of
        // tambien puede ser LocalDate.now()


    } 
    // este lo voy a eliminar si es que mas adelante
    // inventamos un tiempo limite de plazo para los prestamos.
    //eliminamos set de libro y socio el metodo de registrar prestamo
    // las devoluciones
    // son para modificar sus estados 
    

    // --- MÉTODOS ---

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

    if (activo && !devuelto) {  //!devuelto es como <> en sql, es una negacion por lo tanto es true si devuelto es false
    
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
        System.out.println("Estado: " + (devuelto ? "Devuelto" : "No devuelto")); // if/else mas chico 
    }
}