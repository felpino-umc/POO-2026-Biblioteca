
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        // 1. Crear un Autor usando el constructor de la clase hija.
        Autor autor1 = new Autor(
                "Gabriel García Márquez",
                "Colombiano",
                1927
        );

        Autor autor2 = new Autor(
                "Hermann Hesse",
                "Alemán",
                1877
        );

        Autor autor3 = new Autor(
                "Isabel Allende",
                "Chilena",
                1942
        );

        // 2. Crear un Libro y asociarlo con el Autor.
        Libro libro1 = new Libro(
                "Cien años de soledad",
                "978-0307474728",
                1967,
                autor1
        );

        Libro libro2 = new Libro(
                "Siddhartha",
                "978-0142437186",
                1922,
                autor2
        );

        Libro libro3 = new Libro(
                "La casa de los espíritus",
                "978-0553383805",
                1982,
                autor3
        );
        // 3. Crear un Socio usando el constructor de la clase hija.
        Socio socio1 = new Socio(
                "María López",
                1042,
                "maria.lopez@correo.com"
        );

        Socio socio2 = new Socio(
                "Marcelo Muñoz",
                1044,
                "marcelo.munoz@correo.com"
        );

        Persona[] personas = new Persona[5];
        personas[0] = autor1;
        personas[1] = autor2;
        personas[2] = autor3;
        personas[3] = socio1;
        personas[4] = socio2;   

        for(int i= 0; i<personas.length; i++){
            System.out.println(personas[i].toString());
            personas[i].presentarse();
            personas[i].mostrarRol(); // aca deberian mostrar el distinto rol para cada instancia

            if(personas[i] instanceof Autor){
                Autor autor = (Autor) personas[i];
                System.out.println("Edad del autor: " + autor.calcularEdad() + " años");
            }else if(personas[i] instanceof Socio){
                Socio socio = (Socio) personas[i];
                System.out.println("Correo del socio: " + socio.getEmail());
            }



        }

        // 4. Crear un Prestamo y asociarlo con el Libro y el Socio.
        Prestamo prestamo = new Prestamo(
                libro1,
                socio1,
                LocalDate.of(2026, 7, 17)
        );
        
        // 5 Esto es solo exploratorio para ver el flujo de prestamo y devolucion
        Prestamo prestamo2 = new Prestamo(
                libro1,
                socio2,
                LocalDate.of(2026, 9, 18)
        );

        System.out.println("\n--- REGISTRO PRÉSTAMO DE MARÍA ---");
        prestamo.registrarPrestamo();
        prestamo.mostrarInfo();

        System.out.println("\n--- INTENTO PRÉSTAMO 2 CON EL MISMO LIBRO PERO OTRO SOCIO ---");
        prestamo2.registrarPrestamo();

        System.out.println("\n--- DEVOLUCIÓN DEL LIBRO QUE PIDIO MARÍA---");
        prestamo.registrarDevolucion();
        prestamo.mostrarInfo();

        System.out.println("\n--- SEGUNDO INTENTO PRÉSTAMO DEL MARCELO ---");
        prestamo2.registrarPrestamo();
        prestamo2.mostrarInfo();

    } 

}
