import java.time.LocalDate;
import java.util.ArrayList;

/*
 * Biblioteca es el contenedor central del sistema (Cátedra 4).
 *
 * - AGREGACIÓN con Libro y Socio: los recibe ya creados desde afuera y los
 *   guarda por referencia en colecciones. Si la biblioteca desaparece,
 *   los libros y socios siguen existiendo.
 * - COMPOSICIÓN con Prestamo: los préstamos se crean únicamente dentro de
 *   prestar(...). Nadie fuera de Biblioteca hace new Prestamo(...), y su
 *   ciclo de vida completo (crear, registrar, devolver) ocurre aquí adentro.
 * - DEPENDENCIA con CalculadoraMulta: devolver(...) la usa como variable
 *   local para calcular la multa y la descarta; no se guarda como atributo.
 */
public class Biblioteca {

    // --- ATRIBUTOS ---
    private String nombre;

    // Agregación: colecciones de objetos creados externamente.
    private ArrayList<Libro> libros = new ArrayList<>();
    private ArrayList<Socio> socios = new ArrayList<>();

    // Composición: la biblioteca crea y administra sus préstamos.
    private ArrayList<Prestamo> prestamos = new ArrayList<>();

    // --- CONSTRUCTOR ---
    public Biblioteca(String nombre) {
        setNombre(nombre); // reutiliza la validación del setter
    }

    // --- GETTERS Y SETTERS ---
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la biblioteca no puede estar vacío");
        }
        this.nombre = nombre;
    }

    // Las colecciones no se exponen con getters: solo se informan cantidades,
    // para que nadie pueda modificar las listas por fuera de estos métodos.
    public int getCantidadLibros() {
        return libros.size();
    }

    public int getCantidadSocios() {
        return socios.size();
    }

    public int getCantidadPrestamos() {
        return prestamos.size();
    }

    // --- AGREGACIÓN: incorporar y quitar objetos creados afuera ---
    public void agregarLibro(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser nulo");
        }
        if (libros.contains(libro)) {
            System.out.println("El libro \"" + libro.getTitulo() + "\" ya está en el catálogo.");
            return;
        }
        libros.add(libro);
    }

    public void quitarLibro(Libro libro) {
        if (libros.remove(libro)) {
            System.out.println("\"" + libro.getTitulo() + "\" fue retirado del catálogo (el objeto sigue existiendo fuera de la biblioteca).");
        } else {
            System.out.println("Ese libro no está en el catálogo.");
        }
    }

    public void registrarSocio(Socio socio) {
        if (socio == null) {
            throw new IllegalArgumentException("El socio no puede ser nulo");
        }
        if (socios.contains(socio)) {
            System.out.println("El socio " + socio.getNombre() + " ya está registrado.");
            return;
        }
        socios.add(socio);
    }

    // --- COMPOSICIÓN: los préstamos nacen y viven dentro de la biblioteca ---

    // Sobrecarga #1: préstamo con fecha de hoy.
    public boolean prestar(Libro libro, Socio socio) {
        return prestar(libro, socio, LocalDate.now());
    }

    // Sobrecarga #2: préstamo con fecha explícita (por ejemplo, para registrar
    // un préstamo hecho días atrás). La validación de fecha la hace Prestamo.
    public boolean prestar(Libro libro, Socio socio, LocalDate fechaPrestamo) {
        if (libro == null || socio == null) {
            throw new IllegalArgumentException("El libro y el socio no pueden ser nulos");
        }
        if (!libros.contains(libro)) {
            System.out.println("\"" + libro.getTitulo() + "\" no pertenece al catálogo de " + nombre + ".");
            return false;
        }
        if (!socios.contains(socio)) {
            System.out.println(socio.getNombre() + " no está registrado como socio de " + nombre + ".");
            return false;
        }

        // Punto clave de la composición: el new Prestamo(...) ocurre SOLO aquí.
        Prestamo prestamo = new Prestamo(libro, socio, fechaPrestamo);
        prestamo.registrarPrestamo(); // valida disponibilidad y cupo del socio

        if (prestamo.isActivo()) {
            prestamos.add(prestamo);
            return true;
        }
        return false; // registrarPrestamo() ya informó el motivo por consola
    }

    // --- DEVOLUCIÓN + DEPENDENCIA ---

    // Sobrecarga #1: devolución con fecha de hoy.
    public void devolver(Libro libro) {
        devolver(libro, LocalDate.now());
    }

    // Sobrecarga #2: devolución con fecha explícita.
    public void devolver(Libro libro, LocalDate fechaDevolucion) {
        if (libro == null || fechaDevolucion == null) {
            throw new IllegalArgumentException("El libro y la fecha de devolución no pueden ser nulos");
        }
        for (Prestamo prestamo : prestamos) {
            if (prestamo.isActivo() && prestamo.getLibro() == libro) {

                // DEPENDENCIA: CalculadoraMulta se crea como variable local,
                // se usa y se descarta. No es atributo de Biblioteca.
                CalculadoraMulta calculadora = new CalculadoraMulta();
                long multa = calculadora.calcular(prestamo, fechaDevolucion);

                prestamo.registrarDevolucion();

                if (multa > 0) {
                    System.out.println("Devolución con atraso. Multa: $" + multa);
                } else {
                    System.out.println("Devolución dentro del plazo, sin multa.");
                }
                return;
            }
        }
        System.out.println("No hay un préstamo activo de \"" + libro.getTitulo() + "\" en " + nombre + ".");
    }

    // --- INFORMES ---
    public void mostrarCatalogo() {
        System.out.println("Catálogo de " + nombre + " (" + libros.size() + " libros):");
        for (Libro libro : libros) {
            System.out.println("  - " + libro.getTitulo()
                    + " | " + libro.getAutor().getNombre()
                    + " | " + (libro.isDisponible() ? "disponible" : "prestado"));
        }
    }

    public void mostrarPrestamos() {
        System.out.println("Préstamos emitidos por " + nombre + ": " + prestamos.size());
        for (Prestamo prestamo : prestamos) {
            prestamo.mostrarInfo();
            System.out.println("  ---");
        }
    }
}
