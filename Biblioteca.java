import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Biblioteca es el contenedor central del sistema (Cátedra 4).
 *
 * - AGREGACIÓN con Libro y Socio: los recibe ya creados desde afuera y los
 *   guarda por referencia en colecciones. Si la biblioteca desaparece,
 *   los libros y socios siguen existiendo.
 * - COMPOSICIÓN con Prestamo: los préstamos se crean únicamente dentro de
 *   prestar(...). Nadie fuera de Biblioteca hace new Prestamo(...), y su
 *   ciclo de vida completo (crear, registrar, devolver) ocurre aquí adentro.
 * - DEPENDENCIA con PoliticaMulta (S2, antes CalculadoraMulta): devolver(...)
 *   la recibe por parámetro, la usa para calcular la multa y no la guarda.
 */
// SRP: Biblioteca solo administra catálogo, socios y préstamos. La presentación
// por consola (catálogo y listado de préstamos) es responsabilidad de ReporteBiblioteca.
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

    // Protección de estado mutable (Cap. 4): las listas se exponen como VISTAS
    // INMODIFICABLES (no se pueden modificar desde afuera). Quien llama puede leer el catálogo, los socios y los
    // préstamos, pero no alterarlos sin pasar por agregarLibro/quitarLibro/
    // registrarSocio/prestar. Un add() o remove() sobre la vista lanza
    // UnsupportedOperationException. La vista refleja los cambios posteriores
    // de la biblioteca (a diferencia de List.copyOf, que sería una snapshot).
    public List<Libro> getLibros() {
        return Collections.unmodifiableList(libros);
    }

    public List<Socio> getSocios() {
        return Collections.unmodifiableList(socios);
    }

    public List<Prestamo> getPrestamos() {
        return Collections.unmodifiableList(prestamos);
    }

    public int getCantidadLibros() {
        return libros.size();
    }

    public int getCantidadSocios() {
        return socios.size();
    }

    public int getCantidadPrestamos() {
        return prestamos.size();
    }

    // --- AGREGACIÓN: incorporar y quitar objetos creados afuera
    public void agregarLibro(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser nulo");
        }
        // Se compara por ISBN: dos objetos Libro con el mismo ISBN son el mismo libro del catálogo.
        for (Libro existente : libros) {
            if (existente.getIsbn().equals(libro.getIsbn())) {
                System.out.println("El libro \"" + libro.getTitulo() + "\" ya está en el catálogo.");
                return;
            }
        }
        libros.add(libro);
    }

    public void quitarLibro(Libro libro) {
        // No se puede retirar un libro que está prestado: el préstamo activo quedaría sin catálogo.
        for (Prestamo prestamo : prestamos) {
            if (prestamo.isActivo() && prestamo.esDelLibro(libro)) {
                System.out.println("\"" + libro.getTitulo() + "\" tiene un préstamo activo y no se puede retirar.");
                return;
            }
        }
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

    // --- COMPOSICIÓN: los préstamos nacen y viven dentro de la biblioteca

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

    // --- DEVOLUCIÓN + DEPENDENCIA

    // DIP: Biblioteca depende de la abstracción PoliticaMulta, no de la clase
    // concreta CalculadoraMulta. Ya no hace new CalculadoraMulta(): quien llama
    // (Main) decide qué política aplicar y la entrega como parámetro. Así una
    // política nueva (p. ej. MultaReducida) funciona sin modificar Biblioteca.
    // RELACIÓN: dependencia — Biblioteca usa PoliticaMulta (la recibe por
    // parámetro en devolver(...), la usa y no la guarda como atributo).

    // Sobrecarga #1: devolución con fecha de hoy.
    public void devolver(Libro libro, PoliticaMulta politica) {
        devolver(libro, LocalDate.now(), politica);
    }

    // Sobrecarga #2: devolución con fecha explícita.
    public void devolver(Libro libro, LocalDate fechaDevolucion, PoliticaMulta politica) {
        if (libro == null || fechaDevolucion == null) {
            throw new IllegalArgumentException("El libro y la fecha de devolución no pueden ser nulos");
        }
        if (politica == null) {
            throw new IllegalArgumentException("La política de multa no puede ser nula");
        }
        for (Prestamo prestamo : prestamos) {
            if (prestamo.isActivo() && prestamo.esDelLibro(libro)) {

                // Uso temporal de la abstracción: al ser una abstracción valga la redundancia no importa qué clase concreta es.
                long multa = politica.calcular(prestamo, fechaDevolucion);

                prestamo.registrarDevolucion();

                if (multa > 0) {
                    System.out.println("Devolución con atraso (" + politica.getNombre() + "). Multa: $" + multa);
                } else {
                    System.out.println("Devolución dentro del plazo, sin multa.");
                }
                return;
            }
        }
        System.out.println("No hay un préstamo activo de \"" + libro.getTitulo() + "\" en " + nombre + ".");
    }

    // --- BÚSQUEDA ---
    // Reutiliza la interfaz Buscable (C3): libros y socios se buscan con el mismo contrato.
    // Devuelve una lista inmodificable: el resultado es de solo lectura.
    public List<Buscable> buscar(String termino) {
        if (termino == null || termino.isBlank()) {
            throw new IllegalArgumentException("El término de búsqueda no puede estar vacío");
        }
        ArrayList<Buscable> resultados = new ArrayList<>();
        for (Libro libro : libros) {
            if (libro.coincideCon(termino)) {
                resultados.add(libro);
            }
        }
        for (Socio socio : socios) {
            if (socio.coincideCon(termino)) {
                resultados.add(socio);
            }
        }
        return Collections.unmodifiableList(resultados);
    }

    // SRP (S2): mostrarCatalogo() y mostrarPrestamos() se movieron a
    // ReporteBiblioteca. Biblioteca ya no decide cómo se presentan los datos.
}
