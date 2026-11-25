/**
 * Contrato de todo lo que se puede prestar en la biblioteca.
 *
 * Hoy solo se prestan libros, pero una biblioteca también puede prestar otros
 * materiales. Quien lo implemente debe saber informar si está disponible,
 * prestarse y devolverse.
 */
public interface Prestable {

    boolean isDisponible();

    void prestar();

    void devolver();
}
