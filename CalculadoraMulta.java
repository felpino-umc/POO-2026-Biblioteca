import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/*
 * Servicio de cálculo de multas por atraso.
 *
 * Esta clase existe para demostrar DEPENDENCIA (Cátedra 4): quien la usa
 * (Biblioteca.devolver) la crea como variable local, invoca calcular(...) y
 * la descarta. Nadie la mantiene como atributo.
 */
public class CalculadoraMulta {

    // Reglas de negocio compartidas por todas las multas.
    public static final int PLAZO_DIAS = 14;
    public static final int MULTA_POR_DIA = 500; // pesos por día de atraso

    // Recibe el préstamo como parámetro (uso temporal, no se almacena).
    public long calcular(Prestamo prestamo, LocalDate fechaDevolucion) {
        if (prestamo == null || fechaDevolucion == null) {
            throw new IllegalArgumentException("El préstamo y la fecha de devolución no pueden ser nulos");
        }

        LocalDate fechaLimite = prestamo.getFechaPrestamo().plusDays(PLAZO_DIAS);
        long diasAtraso = ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion);

        if (diasAtraso <= 0) {
            return 0; // devuelto dentro del plazo
        }
        return diasAtraso * MULTA_POR_DIA;
    }
}
