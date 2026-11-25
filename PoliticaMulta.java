import java.time.LocalDate;

/**
 * Contrato para calcular la multa por atraso de un préstamo.
 *
 * En una biblioteca la regla de cobro puede cambiar (plazo, valor por día,
 * descuentos), mientras que el acto de devolver un libro es el mismo. Por eso
 * la regla queda detrás de una interfaz: quien cobra depende de esta
 * abstracción y no de una regla concreta.
 */
public interface PoliticaMulta {

    /** Multa en pesos; 0 si el libro se devuelve dentro del plazo. */
    long calcular(Prestamo prestamo, LocalDate fechaDevolucion);

    /** Nombre legible de la política, para mostrarlo en consola. */
    String getNombre();
}
