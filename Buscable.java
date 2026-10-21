/*
 * Contrato de todo lo que se puede buscar en la biblioteca por un texto libre.
 * Cada clase decide en qué datos propios busca el término.
 */
public interface Buscable {

    // Retorna true si el término aparece en los datos de la clase (sin distinguir mayúsculas).
    boolean coincideCon(String termino);
}