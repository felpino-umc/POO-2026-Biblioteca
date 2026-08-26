public class Main {
    public static void main(String[] args) {
        // 1. Crear un Autor con new y asignar sus atributos
        Autor autor = new Autor();
        autor.nombre = "Gabriel García Márquez";
        autor.nacionalidad = "Colombiano";

        // 2. Crear un Libro con new y asignar sus atributos
        Libro libro = new Libro();
        libro.titulo = "Cien años de soledad";
        libro.isbn = "978-0307474728";
        libro.disponible = true;
        libro.autor = autor; // Asociamos el autor creado arriba

        // 3. Crear un Socio con new y asignar sus atributos
        Socio socio = new Socio();
        socio.nombre = "María López";
        socio.numeroSocio = 1042;

        // 4. Crear un Prestamo con new, asociando el libro y el socio creados
        Prestamo prestamo = new Prestamo();
        prestamo.libro = libro;
        prestamo.socio = socio;
        prestamo.fechaPrestamo = "26/08/2026";

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
