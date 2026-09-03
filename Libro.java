public class Libro {

    // Atributos propios de Libro.
    String titulo;
    String isbn;
    int anioPublicacion;
    boolean disponible;
    Autor autor; // Asociacion: un Libro tiene un Autor.

    // Contador compartido por todos los objetos Libro.
    static int totalLibros = 0;

    // Constructor vacio conservado para mantener compatibilidad con la C1.
    public Libro() {
    }

    // Constructor parametrizado. Cada libro se crea disponible por defecto.
    public Libro(String titulo, String isbn, int anioPublicacion, Autor autor) {
        this.titulo = titulo;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
        this.disponible = true;
        this.autor = autor;
        totalLibros++;
    }

    // Muestra la informacion del libro y utiliza el toString() de Autor.
    void mostrarInfo() {
        System.out.println(
                "Libro: " + titulo
                        + " | ISBN: " + isbn
                        + " | Anio: " + anioPublicacion
                        + " | Disponible: " + disponible
        );

        if (autor != null) {
            System.out.println("-> Autor: " + autor);
        } else {
            System.out.println("-> Autor: Sin autor asignado");
        }
    }

    // Cambia el estado del libro cuando se realiza un prestamo.
    void prestar() {
        if (disponible) {
            disponible = false;
            System.out.println(
                    "El libro \"" + titulo + "\" ha sido prestado con exito."
            );
        } else {
            System.out.println(
                    "El libro \"" + titulo + "\" ya esta prestado."
            );
        }
    }

    // Cambia el estado del libro cuando se registra una devolucion.
    void devolver() {
        if (!disponible) {
            disponible = true;
            System.out.println(
                    "El libro \"" + titulo + "\" ha sido devuelto."
            );
        } else {
            System.out.println(
                    "El libro \"" + titulo + "\" ya estaba disponible."
            );
        }
    }
}
