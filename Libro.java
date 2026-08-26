public class Libro {

    // Atributos de instancia
    String titulo;
    String isbn;
    int anioPublicacion;
    boolean disponible;
    Autor autor; // Asociación: Un Libro "tiene un" Autor

    // Atributo estático (compartido por todos los objetos de la clase Libro)
    static int totalLibros = 0;

    // agregar constructor vacío para permitir la creación de objetos sin parámetros
    public Libro() {
        // Constructor vacío
    }

    // Constructor para inicializar el libro (por defecto se crea disponible)
    public Libro(String titulo, String isbn, int anioPublicacion, Autor autor) {
        this.titulo = titulo;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
        this.disponible = true; // Al crearse, está disponible por defecto
        this.autor = autor;

        // Incrementamos el contador global cada vez que se instancia un libro
        totalLibros++;
    }

    // Método para mostrar la información del libro y de su autor
    void mostrarInfo() {
        System.out.println("Libro: " + titulo + " | ISBN: " + isbn + " | Año: " + anioPublicacion + " | Disponible: "
                + disponible);
        System.out.print("-> Autor: ");
        if (autor != null) {
            autor.mostrarInfo(); // Llamamos al método de la clase Autor
        } else {
            System.out.println("Sin autor asignado");
        }
    }

    // Método para prestar el libro (cambia disponible a false)
    void prestar() {
        if (disponible) {
            disponible = false;
            System.out.println("¡El libro \"" + titulo + "\" ha sido prestado con éxito!");
        } else {
            System.out.println("Lo sentimos, el libro \"" + titulo + "\" ya está prestado.");
        }
    }

    // Método para devolver el libro (cambia disponible a true)
    void devolver() {
        if (!disponible) {
            disponible = true;
            System.out.println("¡El libro \"" + titulo + "\" ha sido devuelto!");
        } else {
            System.out.println("El libro \"" + titulo + "\" ya se encontraba disponible en la biblioteca.");
        }
    }
}