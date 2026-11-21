import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Contrato CRUD para Libro. El resto del programa depende de esta interfaz y
 * no de JDBC (DIP); se puede agregar otra implementacion sin modificar a quien
 * la usa (OCP). El ISBN es la clave natural del libro (en Java es final).
 */
public interface LibroDAO {

    /** Inserta el libro; si su autor no existe en la base, tambien lo crea. */
    void crear(Libro libro) throws SQLException;

    Optional<Libro> buscarPorIsbn(String isbn) throws SQLException;

    List<Libro> listarTodos() throws SQLException;

    /** Actualiza titulo, anio, autor y disponibilidad. @return true si actualizo una fila. */
    boolean actualizar(Libro libro) throws SQLException;

    /** @return true si elimino una fila. */
    boolean eliminar(String isbn) throws SQLException;
}
