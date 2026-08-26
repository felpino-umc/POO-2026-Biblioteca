public class Socio {
    String nombre;
    int numeroSocio;
    String email;
    int librosPrestadosActuales;

    static int totalSocios;

    // Imprime nombre, número de socio y cuántos libros tiene prestados ahora
    void mostrarInfo() {
        System.out.println("Nombre: " + nombre);
        System.out.println("Número de Socio: " + numeroSocio);
        System.out.println("Libros prestados actualmente: " + librosPrestadosActuales);
    }

    // Retorna true si librosPrestadosActuales es menor a 3
    boolean puedeSolicitarPrestamo() {
        return librosPrestadosActuales < 3;
    }
}