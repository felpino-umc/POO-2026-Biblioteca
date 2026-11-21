import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementacion JDBC. Todo el SQL usa PreparedStatement (evita inyeccion SQL).
 * Mapeo: Libro -> tabla libro; su Autor -> tablas persona + autor (herencia).
 */
public class LibroDAOImpl implements LibroDAO {

    // La biblioteca de los datos de muestra (tabla biblioteca, id = 1).
    private static final int ID_BIBLIOTECA = 1;

    private static final String SQL_SELECT_BASE =
            "SELECT l.isbn, l.titulo, l.anio_publicacion, l.disponible, "
          + "       p.nombre, a.nacionalidad, a.anio_nacimiento "
          + "FROM libro l "
          + "JOIN autor a   ON a.persona_id = l.autor_id "
          + "JOIN persona p ON p.id = a.persona_id ";
    private static final String SQL_SELECT_ISBN = SQL_SELECT_BASE + "WHERE l.isbn = ?";
    private static final String SQL_SELECT_ALL  = SQL_SELECT_BASE + "ORDER BY l.id";

    private static final String SQL_INSERT =
            "INSERT INTO libro (isbn, titulo, anio_publicacion, disponible, autor_id, biblioteca_id) "
          + "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE libro SET titulo = ?, anio_publicacion = ?, disponible = ?, autor_id = ? "
          + "WHERE isbn = ?";
    private static final String SQL_DELETE = "DELETE FROM libro WHERE isbn = ?";

    private static final String SQL_AUTOR_ID =
            "SELECT p.id FROM persona p JOIN autor a ON a.persona_id = p.id WHERE p.nombre = ?";
    private static final String SQL_INSERT_PERSONA =
            "INSERT INTO persona (nombre, tipo) VALUES (?, 'AUTOR')";
    private static final String SQL_INSERT_AUTOR =
            "INSERT INTO autor (persona_id, nacionalidad, anio_nacimiento) VALUES (?, ?, ?)";

    @Override
    public void crear(Libro libro) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion()) {
            con.setAutoCommit(false); // autor + libro: todo o nada
            try {
                int autorId = buscarOCrearAutor(con, libro.getAutor());
                try (PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
                    ps.setString(1, libro.getIsbn());
                    ps.setString(2, libro.getTitulo());
                    ps.setInt(3, libro.getAnioPublicacion());
                    ps.setBoolean(4, libro.isDisponible());
                    ps.setInt(5, autorId);
                    ps.setInt(6, ID_BIBLIOTECA);
                    ps.executeUpdate();
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    @Override
    public Optional<Libro> buscarPorIsbn(String isbn) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ISBN)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Libro> listarTodos() throws SQLException {
        List<Libro> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public boolean actualizar(Libro libro) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion()) {
            con.setAutoCommit(false);
            try {
                int autorId = buscarOCrearAutor(con, libro.getAutor());
                int filas;
                try (PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
                    ps.setString(1, libro.getTitulo());
                    ps.setInt(2, libro.getAnioPublicacion());
                    ps.setBoolean(3, libro.isDisponible());
                    ps.setInt(4, autorId);
                    ps.setString(5, libro.getIsbn());
                    filas = ps.executeUpdate();
                }
                con.commit();
                return filas > 0;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    @Override
    public boolean eliminar(String isbn) throws SQLException {
        // Falla (SQLException) si el libro tiene prestamos registrados: la FK lo protege.
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setString(1, isbn);
            return ps.executeUpdate() > 0;
        }
    }

    /** Devuelve el id del autor (buscado por nombre); si no existe lo inserta en persona + autor. */
    private int buscarOCrearAutor(Connection con, Autor autor) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SQL_AUTOR_ID)) {
            ps.setString(1, autor.getNombre());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        int personaId;
        try (PreparedStatement ps = con.prepareStatement(SQL_INSERT_PERSONA, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, autor.getNombre());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                personaId = keys.getInt(1);
            }
        }
        try (PreparedStatement ps = con.prepareStatement(SQL_INSERT_AUTOR)) {
            ps.setInt(1, personaId);
            ps.setString(2, autor.getNacionalidad());
            ps.setInt(3, autor.getAnioNacimiento());
            ps.executeUpdate();
        }
        return personaId;
    }

    /** Convierte una fila (libro + autor) en objetos del dominio. */
    private Libro mapear(ResultSet rs) throws SQLException {
        Autor autor = new Autor(
                rs.getString("nombre"),
                rs.getString("nacionalidad"),
                rs.getInt("anio_nacimiento"));
        return new Libro(
                rs.getString("titulo"),
                rs.getString("isbn"),
                rs.getInt("anio_publicacion"),
                autor,
                rs.getBoolean("disponible"));
    }
}
