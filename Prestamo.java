/* package POO-2026-Biblioteca;*/
//revision git 
// Omitimos la línea del package por ahora para no tener problemas al compilar por consola

public class Prestamo {

    // --- ATRIBUTOS ---
    Libro libro; // El libro prestado (objeto completo)[cite: 1]
    Socio socio; // Quién lo pidió (objeto completo)[cite: 1]
    String fechaPrestamo; // Ejemplo: "2026-08-27"[cite: 1]
    boolean devuelto; // true si ya se devolvió, false si sigue prestado[cite: 1]

    // --- MÉTODOS ---

    // Método para registrar la devolución
    void registrarDevolucion() {
        devuelto = true; // 1) deja "devuelto" en true[cite: 1]
        libro.devolver(); // 2) le avisa al libro que ya está disponible de nuevo[cite: 1]
    }

    // Método para mostrar la información en consola
    void mostrarInfo() {
        // Imprime: "Préstamo: [titulo del libro] a [nombre del socio] el [fecha] devuelto: [si/no]"[cite: 1]
        System.out.println("Préstamo: " + libro.titulo + " a " + socio.nombre +  " el " + fechaPrestamo + " devuelto: " + devuelto);
        
    }
}