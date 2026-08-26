public class Socio {
    // TODO: declarar atributos (tipados, camelCase)
    String nombre;[cite: 1]
    int numeroSocio;[cite: 1]
    String email;[cite: 1]
    int librosPrestadosActuales;[cite: 1]

    // TODO: atributo estático (compartido por todos los socios)
    static int totalSocios;[cite: 1]

    // TODO: método mostrarInfo()
    // Imprime nombre, número de socio y cuántos libros tiene prestados ahora
    void mostrarInfo() {[cite: 1]
        System.out.println("Nombre: " + nombre);
        System.out.println("Número de Socio: " + numeroSocio);
        System.out.println("Libros prestados actualmente: " + librosPrestadosActuales);
    }

    // TODO: método puedeSolicitarPrestamo()
    // Retorna true si librosPrestadosActuales es menor a 3
    boolean puedeSolicitarPrestamo() {[cite: 1]
        return librosPrestadosActuales < 3;
    }
}