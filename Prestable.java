/*
 * Contrato de todo lo que se puede prestar en la biblioteca.
 * Quien lo implemente debe saber informar si está disponible, prestarse y devolverse.
 */
public interface Prestable {

    boolean isDisponible();

    void prestar();

    void devolver();
}