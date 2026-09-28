/* package POO-2026-Biblioteca;*/
//revision git 
// Omitimos la línea del package por ahora para no tener problemas al compilar por consola

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
        this.libro = libro;
        this.socio = socio;
        this.fechaPrestamo = fechaPrestamo;
    }
    // Constructor vacio para explicar sobrecarga de constructores y el uso de setters
    //setters para asignar valores de una instancia que ya existe (ojito setter no es lo mismo que constructor)
    public Prestamo() {

    }

    // Getters y setters
    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
    
    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }
    //no agrego getter y setter para devuelto porque no quiero que se pueda cambiar el estado de devuelto desde afuera, solo desde registrarDevolucion
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
        System.out.println("Estado: " + (devuelto ? "Devuelto" : "No devuelto")); //esta parte la tiro copilot pero es un  if/else mas chico 
    }
}