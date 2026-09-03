public class Main {
    public static void main(String[] args) {
        // 1. Crear un Autor
        Autor autor = new Autor("Gabriel García Márquez", "Colombiano", 1927);

        // 2. Crear un Libro y asociarlo con el autor
        Libro libro = new Libro("Cien años de soledad", "978-0307474728", 1967, autor);

        // 3. Crear un Socio
        Socio socio = new Socio("María López", 1042, "maria.lopez@correo.com");

        // 4. Crear un Prestamo con new, asociando el libro y el socio creados
        Prestamo prestamo = new Prestamo();
        prestamo.libro = libro;
        prestamo.socio = socio;
        prestamo.fechaPrestamo = "26/08/2026";
        libro.prestar();
        socio.registrarPrestamo();

        // 5. Invocar los métodos de cada objeto y mostrar resultados
        System.out.println("--- INFORMACIÓN INICIAL ---");
        autor.mostrarInfo();
        libro.mostrarInfo();
        socio.mostrarInfo();

        System.out.println("\n--- REGISTRO DE PRÉSTAMO ---");
        prestamo.mostrarInfo();

        System.out.println("\n--- DEVOLUCIÓN ---");
        prestamo.registrarDevolucion();
        
        System.out.println("\n--- ESTADO FINAL DEL LIBRO ---");
        libro.mostrarInfo();
    }
}
