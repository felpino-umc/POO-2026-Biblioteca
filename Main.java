public class Main {
    public static void main(String[] args) {

        // 1. Crear un Autor usando el constructor de la clase hija.
        Autor autor = new Autor(
                "Gabriel García Márquez",
                "Colombiano",
                1927
        );

        // 2. Crear un Libro y asociarlo con el Autor.
        Libro libro = new Libro(
                "Cien años de soledad",
                "978-0307474728",
                1967,
                autor
        );

        // 3. Crear un Socio usando el constructor de la clase hija.
        Socio socio = new Socio(
                "María López",
                1042,
                "maria.lopez@correo.com"
        );

        // 4. Crear un Prestamo y asociarlo con el Libro y el Socio.
        Prestamo prestamo = new Prestamo();
        prestamo.libro = libro;
        prestamo.socio = socio;
        prestamo.fechaPrestamo = "26/6";
        prestamo.devuelto = false;

        System.out.println("--- DEMOSTRACIÓN DE HERENCIA ---");

        // Invoca automáticamente los métodos toString().
        System.out.println(autor);
        System.out.println(socio);

        // Método heredado desde Persona.
        autor.presentarse();
        socio.presentarse();

        // Métodos propios de cada clase hija.
        System.out.println("Edad del autor: " + autor.calcularEdad());
        System.out.println(
                "¿Puede solicitar un préstamo?: "
                        + socio.puedeSolicitarPrestamo()
        );

        System.out.println("\n--- REGISTRO DEL PRÉSTAMO ---");
        libro.prestar();
        socio.registrarPrestamo();
        libro.mostrarInfo();
        prestamo.mostrarInfo();

        System.out.println("\n--- DEVOLUCIÓN ---");
        prestamo.registrarDevolucion();
        socio.registrarDevolucion();

        System.out.println("\n--- ESTADO FINAL ---");
        libro.mostrarInfo();
        System.out.println(socio);
    }
}
