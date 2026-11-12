import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        // ============================================================
        // 1. CREACIÓN DE OBJETOS
        // ============================================================

        Autor autor1 = new Autor("Gabriel García Márquez", "Colombiano", 1927);
        Autor autor2 = new Autor("Hermann Hesse", "Alemán", 1877);
        Autor autor3 = new Autor("Isabel Allende", "Chilena", 1942);
        Autor autor4 = new Autor("Alberto Blest Gana", "Chileno", 1830);
        Autor autor5 = new Autor("Julio Verne", "Francés", 1828);

        Libro libro1 = new Libro("Cien años de soledad", "978-0307474728", 1967, autor1);
        Libro libro2 = new Libro("Siddhartha", "978-0142437186", 1922, autor2);
        Libro libro3 = new Libro("La casa de los espíritus", "978-0553384689", 1982, autor3);
        Libro libro4 = new Libro("Martin Rivas", "978-0553383888", 1982, autor4);
        Libro libro5 = new Libro("Veinte mil leguas de viaje submarino", "978-0553773805", 1870, autor5);

        Socio socio1 = new Socio("María López", 1042, "maria.lopez@correo.com");
        Socio socio2 = new Socio("Marcelo Muñoz", 1044, "marcelo.munoz@correo.com");

        // ============================================================
        // 2. POLIMORFISMO (Solemne 1)
        // ============================================================

        System.out.println("--- POLIMORFISMO CON Persona[] ---");

        Persona[] personas = { autor1, autor2, autor3, socio1, socio2, autor4, autor5 };

        for (int i = 0; i < personas.length; i++) {
            System.out.println(personas[i].toString());
            personas[i].presentarse();
            personas[i].mostrarRol();

            if (personas[i] instanceof Autor) {
                Autor autor = (Autor) personas[i];
                System.out.println("Edad del autor: " + autor.calcularEdad() + " años");
            } else if (personas[i] instanceof Socio) {
                Socio socio = (Socio) personas[i];
                System.out.println("Correo del socio: " + socio.getEmail());
            }
        }

        // ============================================================
        // 3. ASOCIACIÓN (C4)
        // ============================================================

        System.out.println("\n--- ASOCIACIÓN: Libro -> Autor ---");
        System.out.println("\"" + libro1.getTitulo() + "\" fue escrito por "
                + libro1.getAutor().getNombre()
                + " (" + libro1.getAutor().getNacionalidad() + ").");
        System.out.println("El autor existe por sí mismo y no es una \"parte\" del libro: "
                + autor1.getNombre() + " seguiría existiendo sin el libro.");

        // ============================================================
        // 4. AGREGACIÓN (C4)
        // ============================================================

        System.out.println("\n--- AGREGACIÓN: Biblioteca agrupa libros y socios ---");

        Biblioteca biblioteca = new Biblioteca("Biblioteca Central UMC");

        biblioteca.agregarLibro(libro1);
        biblioteca.agregarLibro(libro2);
        biblioteca.agregarLibro(libro3);
        biblioteca.agregarLibro(libro4);
        biblioteca.agregarLibro(libro5);

        biblioteca.registrarSocio(socio1);
        biblioteca.registrarSocio(socio2);

        biblioteca.mostrarCatalogo();
        System.out.println("Socios registrados: " + biblioteca.getCantidadSocios());

        // Otro objeto con el mismo ISBN es el mismo libro: el catálogo lo rechaza.
        biblioteca.agregarLibro(new Libro("Copia", libro1.getIsbn(), 1967, autor1));

        // Comprobación de independencia: al quitar un libro del catálogo,
        // el objeto sigue existiendo pero no puede prestarse en esta biblioteca.
        biblioteca.quitarLibro(libro5);

        System.out.println("Fuera de la biblioteca, el objeto sigue existiendo: \""
                + libro5.getTitulo() + "\" de " + libro5.getAutor().getNombre());

        System.out.println("\n--- BÚSQUEDA EN LA BIBLIOTECA (Buscable) ---");
        System.out.println("Resultados de buscar 'mar': " + biblioteca.buscar("mar").size());

        // ============================================================
        // 5. COMPOSICIÓN (C4)
        // ============================================================

        System.out.println("\n--- COMPOSICIÓN: Socio crea su Credencial ---");
        System.out.println("Credencial de " + socio1.getNombre() + ": " + socio1.getCredencial());

        System.out.println("\n--- COMPOSICIÓN: la Biblioteca crea sus préstamos ---");

        System.out.println("\n* María solicita \"" + libro1.getTitulo() + "\":");
        biblioteca.prestar(libro1, socio1);

        System.out.println("\n* Marcelo intenta pedir el mismo libro:");
        biblioteca.prestar(libro1, socio2);

        System.out.println("\n* María devuelve el libro:");
        biblioteca.devolver(libro1);

        System.out.println("\n* Marcelo vuelve a pedirlo:");
        biblioteca.prestar(libro1, socio2);

        System.out.println();
        biblioteca.mostrarPrestamos();

        // ============================================================
        // 6. DEPENDENCIA (C4)
        // ============================================================

        System.out.println("--- DEPENDENCIA: cálculo de multa en la devolución ---");

        // Préstamo registrado con fecha pasada para simular un atraso
        // (el plazo de CalculadoraMulta es de 14 días).
        System.out.println("\n* María pidió \"" + libro2.getTitulo() + "\" el 1 de octubre:");
        biblioteca.prestar(libro2, socio1, LocalDate.of(2026, 10, 1));

        System.out.println("\n* Lo devuelve recién hoy:");
        biblioteca.devolver(libro2);

        // ============================================================
        // 7. VALIDACIONES (C3 + corrección C4)
        // ============================================================

        System.out.println("\n--- VALIDACIÓN DE EMAIL ---");
        try {
            socio1.setEmail("correo-sin-arroba-ni-dominio");
        } catch (IllegalArgumentException e) {
            System.out.println("Error controlado: " + e.getMessage());
        }

        System.out.println("\n--- VALIDACIÓN DE AÑO DE NACIMIENTO ---");
        try {
            autor1.setAnioNacimiento(LocalDate.now().getYear() + 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Error controlado: " + e.getMessage());
        }

        System.out.println("\n--- VALIDACIÓN DE FECHA DE PRÉSTAMO FUTURA ---");
        // Corrección C4: el constructor de Prestamo ahora reutiliza la
        // validación del setter, por lo que una fecha futura ya no crea
        // un préstamo en estado inválido (ver DEPURACION.md).
        try {
            biblioteca.prestar(libro3, socio1, LocalDate.of(2030, 1, 1));
        } catch (IllegalArgumentException e) {
            System.out.println("Error controlado: " + e.getMessage());
        }

        // ============================================================
        // 8. INTERFACES (C3)
        // ============================================================

        System.out.println("\n--- INTERFAZ PRESTABLE ---");

        Prestable prestable = libro3;

        System.out.println("Disponible al inicio: " + prestable.isDisponible());
        prestable.prestar();
        System.out.println("Disponible tras prestar: " + prestable.isDisponible());
        prestable.devolver();
        System.out.println("Disponible tras devolver: " + prestable.isDisponible());

        System.out.println("\n--- BÚSQUEDA POLIMÓRFICA ---");

        Buscable[] buscables = { libro1, libro2, libro3, socio1, socio2, libro4, libro5 };
        String termino = "mar";

        for (int i = 0; i < buscables.length; i++) {
            System.out.println("Elemento " + (i + 1)
                    + " coincide con \"" + termino + "\": "
                    + buscables[i].coincideCon(termino));
        }

        System.out.println("\n--- IMPLEMENTACIÓN DE DOS INTERFACES ---");

        Buscable buscable = libro3;

        System.out.println("Mismo objeto mediante ambas interfaces: " + (prestable == buscable));
        System.out.println("Disponible: " + prestable.isDisponible());
        System.out.println("Coincide con \"espíritus\": " + buscable.coincideCon("espíritus"));
    }
}
