import java.time.Year;

// Libro cumple dos contratos a la vez: se puede prestar y se puede buscar.
public class Libro implements Prestable, Buscable {

    // Atributos propios de Libro.
    private String titulo;
    private final String isbn; // final: identifica al libro y no cambia despues de crearlo
    private int anioPublicacion;
    private boolean disponible;
    private Autor autor; // Asociacion: un Libro tiene un Autor.

    // Contador compartido por todos los objetos Libro.
    private static int totalLibros = 0;

    // No hay constructor vacio: isbn es final y debe asignarse siempre al crear el libro.
    // Cada libro se crea disponible por defecto.
    public Libro(String titulo, String isbn, int anioPublicacion, Autor autor) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("El ISBN no puede estar vacio");
        }
        this.isbn = isbn;
        setTitulo(titulo); // los setters reutilizan su validacion
        setAnioPublicacion(anioPublicacion);
        setAutor(autor);
        this.disponible = true;
        totalLibros++;
    }

    // Getters y setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio");
        }
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        if (anioPublicacion < 1 || anioPublicacion > Year.now().getValue()) {
            throw new IllegalArgumentException(
                    "El anio de publicacion debe estar entre 1 y " + Year.now().getValue());
        }
        this.anioPublicacion = anioPublicacion;
    }

    // Sin setDisponible: el estado solo cambia con prestar() y devolver().
    @Override
    public boolean isDisponible() {
        return disponible;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        if (autor == null) {
            throw new IllegalArgumentException("El libro debe tener un autor");
        }
        this.autor = autor;
    }

    public static int getTotalLibros() {
        return totalLibros;
    }

    // Muestra la informacion del libro y utiliza el toString() de Autor.
    void mostrarInfo() {
        System.out.println(
                "Libro: " + titulo
                        + " | ISBN: " + isbn
                        + " | Anio: " + anioPublicacion
                        + " | Disponible: " + disponible
        );
        System.out.println("-> Autor: " + autor);
    }

    // Cambia el estado del libro cuando se realiza un prestamo.
    @Override
    public void prestar() {
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
    @Override
    public void devolver() {
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

    // Un libro se encuentra por titulo, ISBN o nombre de su autor.
    @Override
    public boolean coincideCon(String termino) {
        String buscado = termino.toLowerCase();
        return titulo.toLowerCase().contains(buscado)
                || isbn.toLowerCase().contains(buscado)
                || autor.getNombre().toLowerCase().contains(buscado);
    }
}
