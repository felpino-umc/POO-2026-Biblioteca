import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Política de multa con descuento: mismo plazo de 14 días, la mitad del valor
 * por día de atraso.
 */
// OCP: extensión por clase nueva; PoliticaMulta, CalculadoraMulta y Biblioteca no se modificaron.
// RELACIÓN: dependencia — MultaReducida usa Prestamo (solo como parámetro, no lo guarda)
public class MultaReducida implements PoliticaMulta {

    private static final int PLAZO_DIAS = 14;
    private static final int MULTA_POR_DIA = 250; // pesos por día de atraso

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
        return "Multa reducida";
    }
}
