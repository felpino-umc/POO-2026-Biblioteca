import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/*
 * Política estándar de multas por atraso.
 *
 * Es una implementación concreta de PoliticaMulta. Desde S2 (DIP) Biblioteca
 * ya no la crea: Main la instancia y la entrega a Biblioteca.devolver(...)
 * como PoliticaMulta, que la usa por parámetro y no la guarda como atributo.
 */
// RELACIÓN: dependencia — CalculadoraMulta usa Prestamo (solo como parámetro, no lo guarda)
public class CalculadoraMulta implements PoliticaMulta {

    // Reglas propias de esta política: nadie de afuera necesita conocerlas.
    private static final int PLAZO_DIAS = 14;
    private static final int MULTA_POR_DIA = 500; // pesos por día de atraso

    // Recibe el préstamo como parámetro (uso temporal, no se almacena).
    @Override
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

    @Override
    public String getNombre() {
        return "Multa estándar";
    }
}
