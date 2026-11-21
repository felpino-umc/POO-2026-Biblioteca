import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Responsabilidad unica (SRP): entregar conexiones JDBC a la base de datos.
 * No conoce libros, autores ni SQL de negocio.
 */
public class ConexionBD {

    // === Datos de conexion (completar con los del docente si cambian) ===
    private static final String HOST = "localhost";
    private static final int    PUERTO = 3306;
    private static final String BASE_DATOS = "POO_2026_Biblioteca";
    private static final String USUARIO = "poo_user";
    private static final String PASSWORD = "CambiaEstaClave123!";

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DATOS
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Santiago";

    private ConexionBD() {
        // clase utilitaria: no se instancia
    }

    /** Cada llamada abre una conexion nueva; quien la pide la cierra (try-with-resources). */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
