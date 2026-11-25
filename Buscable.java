/**
 * Contrato de todo lo que se puede buscar en la biblioteca por un texto libre.
 *
 * Quien atiende un mostrador busca "algo" sin saber si es un libro o un socio:
 * necesita preguntar si coincide y, luego, mostrar qué encontró. Cada clase
 * decide en qué datos propios busca y cómo se presenta.
 */
public interface Buscable {

    // Retorna true si el término aparece en los datos de la clase (sin distinguir mayúsculas).
    boolean coincideCon(String termino);

    // Texto corto para mostrar un resultado de búsqueda.
    String getEtiqueta();
}
