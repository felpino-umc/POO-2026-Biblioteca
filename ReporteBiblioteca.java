/**
 * Presenta por consola el estado de una {@link Biblioteca}.
 *
 * SRP: esta clase tiene una sola responsabilidad, mostrar información. Antes
 * (C4) Biblioteca administraba catálogo, socios y préstamos Y ADEMÁS imprimía
 * por consola (mostrarCatalogo, mostrarPrestamos), y Prestamo también imprimía
 * (mostrarInfo). Eran dos razones de cambio en la misma clase: una regla de
 * negocio y un formato de salida. Ahora, si el reporte debe pasar a HTML o a
 * un archivo, solo se modifica esta clase; Biblioteca y Prestamo no se tocan.
 *
 * RELACIÓN: dependencia — ReporteBiblioteca usa Biblioteca (la recibe por
 * parámetro en cada método, la lee y no la guarda como atributo).
 */
public class ReporteBiblioteca {

    public void mostrarCatalogo(Biblioteca biblioteca) {
        validar(biblioteca);
        System.out.println("Catálogo de " + biblioteca.getNombre()
                + " (" + biblioteca.getCantidadLibros() + " libros):");
        // getLibros() es una vista inmodificable: el reporte solo puede leer.
        for (Libro libro : biblioteca.getLibros()) {
            System.out.println("  - " + libro.getTitulo()
                    + " | " + libro.getAutor().getNombre()
                    + " | " + (libro.isDisponible() ? "disponible" : "prestado"));
        }
    }

    public void mostrarPrestamos(Biblioteca biblioteca) {
        validar(biblioteca);
        System.out.println("Préstamos emitidos por " + biblioteca.getNombre()
                + ": " + biblioteca.getCantidadPrestamos());
        for (Prestamo prestamo : biblioteca.getPrestamos()) {
            mostrarPrestamo(prestamo);
            System.out.println("  ---");
        }
    }

    // Lo que antes hacía Prestamo.mostrarInfo(), usando solo datos de lectura.
    public void mostrarPrestamo(Prestamo prestamo) {
        if (prestamo == null) {
            throw new IllegalArgumentException("El préstamo no puede ser nulo");
        }
        System.out.println("Préstamo de libro: " + prestamo.getTituloLibro());
        System.out.println("Socio: " + prestamo.getNombreSocio());
        System.out.println("Fecha de préstamo: " + prestamo.getFechaPrestamo());
        System.out.println("Estado: " + (prestamo.isDevuelto() ? "Devuelto" : "No devuelto"));
    }

    private void validar(Biblioteca biblioteca) {
        if (biblioteca == null) {
            throw new IllegalArgumentException("La biblioteca no puede ser nula");
        }
    }
}
