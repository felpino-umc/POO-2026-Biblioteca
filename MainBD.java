import java.sql.SQLException;
import java.util.Optional;

/**
 * Demuestra las 4 operaciones CRUD sobre Libro con JDBC.
 * (Se llama MainBD para no pisar el Main del proyecto, que demuestra el dominio.)
 * Depende de la interfaz LibroDAO, no de la implementacion.
 */
public class MainBD {

    public static void main(String[] args) {
        LibroDAO dao = new LibroDAOImpl();

        try {
            // READ inicial
            System.out.println("=== READ: catalogo actual ===");
            dao.listarTodos().forEach(l -> System.out.println(
                    "  " + l.getIsbn() + " | " + l.getTitulo() + " | " + l.getAutor().getNombre()
                    + " | " + (l.isDisponible() ? "disponible" : "prestado")));

            // CREATE
            System.out.println("\n=== CREATE ===");
            Autor cortazar = new Autor("Julio Cortázar", "Argentina", 1914);
            Libro rayuela = new Libro("Rayuela", "978-0-00-000008-8", 1963, cortazar);
            dao.crear(rayuela);
            System.out.println("Creado: " + rayuela.getTitulo() + " (" + rayuela.getIsbn() + ")");

            // READ por ISBN
            System.out.println("\n=== READ por ISBN ===");
            Optional<Libro> encontrado = dao.buscarPorIsbn(rayuela.getIsbn());
            encontrado.ifPresentOrElse(
                    l -> System.out.println("Encontrado: " + l.getTitulo() + " | " + l.getAutor()),
                    () -> System.out.println("No encontrado"));

            // UPDATE
            System.out.println("\n=== UPDATE ===");
            rayuela.setTitulo("Rayuela (edicion aniversario)");
            rayuela.setAnioPublicacion(1964);
            System.out.println("Actualizado: " + dao.actualizar(rayuela));
            dao.buscarPorIsbn(rayuela.getIsbn())
               .ifPresent(l -> System.out.println("Ahora: " + l.getTitulo() + " | " + l.getAnioPublicacion()));

            // DELETE
            System.out.println("\n=== DELETE ===");
            System.out.println("Eliminado: " + dao.eliminar(rayuela.getIsbn()));
            System.out.println("Sigue existiendo? " + dao.buscarPorIsbn(rayuela.getIsbn()).isPresent());

        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
        }
    }
}
