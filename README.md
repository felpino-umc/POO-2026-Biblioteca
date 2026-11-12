# Proyecto Biblioteca — POO 2026

## 👥 Autores y Equipo

* **Felipe Pino** — [GitHub](https://github.com/felpino-umc) | Correo: felipe.pino.c@alumni.umc.cl
* **Matías Araya** — [GitHub](https://github.com/matiasah2117) | Correo: matias.araya@alumni.umc.cl
* **Cristian Diaz** — [GitHub](https://github.com/cristiandiaz-dev) | Correo: cristian.diaz.a@alumni.umc.cl

## Entidades
- Persona: Clase base (abstracta) que representa los datos y comportamientos comunes de una persona, principalmente el nombre, `toString()`, `presentarse()` y el método abstracto `mostrarRol()`.
- Autor: Representa a un autor, con su nombre, nacionalidad y año de nacimiento.
- Libro: Representa un libro asociado a un autor, con ISBN, año de publicación y disponibilidad.
- Socio: Representa a un miembro de la biblioteca, con sus datos y cantidad de libros prestados.
- Prestamo: Registra el libro prestado, el socio, la fecha del préstamo y su estado de devolución.
- Biblioteca: Contenedor central (Cátedra 4): agrupa libros y socios, crea y administra los préstamos.
- Credencial: Carnet del socio, creado por el propio `Socio` (composición).
- CalculadoraMulta: Servicio que calcula la multa por atraso en una devolución (dependencia).

## Jerarquía de clases (Herencia)

En esta entrega se introduce herencia para centralizar responsabilidades comunes y evitar duplicación de código.

Persona queda como clase padre, y a partir de la Solemne 1 pasa a ser **abstracta**: nunca se crea un objeto `Persona` directamente, solo objetos de sus hijas concretas (`Autor` o `Socio`). Lo único que ambos comparten de verdad en el sistema es el nombre: tanto un autor como un socio son personas que se relacionan con la biblioteca, pero cada uno lo hace de una forma distinta. Por eso el nombre se declara una sola vez en Persona (con getter/setter) y las hijas lo heredan.

- Autor extiende Persona. Además del nombre guarda nacionalidad y año de nacimiento, y ofrece un método para calcular la edad. Es una persona del lado de la obra (quién escribió el libro), no del lado del préstamo.
- Socio extiende Persona. Además del nombre guarda número de socio, correo y cuántos libros tiene prestados en este momento. También decide si puede pedir un préstamo nuevo. Es una persona del lado del usuario de la biblioteca.

Libro y Prestamo no heredan de Persona porque no representan personas. Un libro es un material del catálogo y un préstamo es una operación entre un socio y un libro. Forzar esa herencia sería un mal diseño: compartirían un "nombre" que no significa lo mismo y métodos que no les corresponden.

En resumen, la herencia se usa solo donde hay una relación "es un": un Autor es una Persona y un Socio es una Persona. Libro y Prestamo no lo son.

## Clase abstracta y polimorfismo (Solemne 1)

`Persona` declara el método abstracto `mostrarRol()` (sin cuerpo), lo que obliga a toda clase hija concreta a implementarlo con `@Override`:

- `Autor.mostrarRol()`: informa que esa persona participa como autor (escribe libros del catálogo).
- `Socio.mostrarRol()`: informa que esa persona participa como socio (puede solicitar préstamos).

La misma llamada `mostrarRol()` se comporta distinto según el tipo real del objeto en memoria — eso es polimorfismo por enlace dinámico: Java decide en tiempo de ejecución cuál versión ejecutar, no en tiempo de compilación, mirando el objeto real y no el tipo declarado de la variable o del array que lo contiene.

Además, `Persona`, `Autor`, `Socio` y `Libro` ahora exponen sus atributos como `private` con getters/setters, y sus constructores están sobrecargados (uno vacío y uno parametrizado con `this`), en vez de exponer los atributos directamente como en la C1.

## Evaluación individual — nota importante

A diferencia de las entregas C1 y C2, la Solemne 1 se evalúa **de forma individual**: cada integrante presenta solo y debe poder explicar y defender el proyecto completo, no solo los archivos que programó él mismo.

## Encapsulamiento (Cátedra 3)

Todos los atributos son `private` y se accede a ellos mediante getters y setters. Los setters validan los datos y lanzan `IllegalArgumentException` si son inválidos, de modo que no pueda existir un objeto en un estado incorrecto:

- `Persona.setNombre`: el nombre no puede ser nulo ni estar en blanco.
- `Autor.setNacionalidad` y `Autor.setAnioNacimiento`: la nacionalidad no puede estar vacía y el año debe estar entre 1 y el año actual.
- `Socio.setEmail`: no puede estar vacío y debe contener `@`.
- `Socio.setLibrosPrestadosActuales`: debe estar entre 0 y el máximo de préstamos permitido.
- `Libro.setTitulo`, `Libro.setAnioPublicacion` y `Libro.setAutor`: el título no puede estar vacío, el año debe estar entre 1 y el año actual y el libro debe tener autor.

Los constructores parametrizados reutilizan estos setters, así la regla se escribe una sola vez.

Atributos inmutables: `Socio.numeroSocio` es `final` porque identifica al socio y no debe cambiar después de crearlo (por eso no tiene setter ni constructor vacío). `Libro.isbn` también es `final`, porque identifica al libro (no tiene setter ni constructor vacío). `Socio.MAX_LIBROS` es `static final`: una constante compartida por todos los socios.

`Libro` no tiene `setDisponible`: su disponibilidad solo cambia mediante `prestar()` y `devolver()`, para que nadie pueda saltarse esas reglas desde afuera.

## Abstracción (Cátedra 3)

`Persona` es abstracta porque una "persona" genérica no existe en el dominio de la biblioteca: siempre es un autor o un socio. Si fuera concreta, se podría hacer `new Persona(...)` y quedaría un objeto sin rol. Al ser `abstract`, el compilador lo impide y además obliga a cada clase hija a implementar `mostrarRol()`, que cada una resuelve de forma distinta.

## Interfaces (Cátedra 3)

- `Prestable`: contrato de lo que se puede prestar (`isDisponible()`, `prestar()`, `devolver()`).
- `Buscable`: contrato de lo que se puede buscar por un texto (`coincideCon(String termino)`).

`Libro` implementa `Prestable` y `Buscable` (se busca por título, ISBN o nombre del autor). `Socio` implementa `Buscable` (se busca por nombre o email). Una clase puede heredar de una sola clase pero implementar varias interfaces, por eso `Libro` cumple los dos contratos a la vez (`Prestable` y `Buscable`), y `Socio` hereda de `Persona` e implementa `Buscable`.

## Relaciones entre clases (Cátedra 4)

| Relación | Clases | Dónde se ve en el código | Por qué corresponde |
|---|---|---|---|
| **Composición** | `Socio` → `Credencial` | `new Credencial(numeroSocio)` dentro del constructor de `Socio`; no hay `setCredencial` ni parámetro `Credencial` | La credencial nace con el socio y no tiene sentido sin él |
| **Composición** | `Biblioteca` → `Prestamo` | `new Prestamo(...)` solo ocurre en `Biblioteca.prestar()`; los préstamos viven en una lista privada de la biblioteca | Los préstamos son registros que la biblioteca crea y administra |
| **Agregación** | `Biblioteca` → `Libro`, `Socio` | `agregarLibro`, `registrarSocio` y `quitarLibro` reciben objetos creados en `Main` y guardan su referencia | Un libro o un socio existen sin la biblioteca: al retirarlos del catálogo siguen existiendo |
| **Asociación** | `Libro` → `Autor`; `Prestamo` → `Libro` y `Socio` | Atributos `autor`, `libro` y `socio` | Una referencia entre objetos independientes, sin relación parte–todo: el autor existiría sin el libro |
| **Dependencia** | `Biblioteca.devolver` → `CalculadoraMulta` | La calculadora se crea como variable local en `devolver(...)`, se usa y se descarta; no es atributo | Uso temporal de un servicio para calcular la multa por atraso (plazo de 14 días, $500 por día) |

Diferencia clave entre composición y agregación: en la composición el contenedor **crea** a sus partes con `new` y estas dependen de él; en la agregación el contenedor **recibe** objetos ya creados y estos siguen existiendo si el contenedor desaparece.

`Biblioteca` además ofrece `buscar(String)`, que reúne libros y socios que cumplan `Buscable.coincideCon`, y evita ISBN duplicados en el catálogo.

### Encapsulamiento en C4
Todos los atributos son `private`. `Biblioteca` no expone sus listas con getters (solo cantidades), para que nadie las modifique fuera de sus métodos.

### Depuración
El registro de un episodio real de depuración está en [DEPURACION.md](DEPURACION.md).

## Funcionamiento del programa

`Main` ejecuta un ejemplo completo, organizado en secciones:

1. **Creación de objetos:** cinco autores, cinco libros y dos socios.
2. **Polimorfismo:** un array `Persona[]` recorrido con `for`; `mostrarRol()` se comporta distinto según el tipo real del objeto, y `instanceof` + casting da acceso a los métodos propios de `Autor` y `Socio`.
3. **Relaciones entre clases (Cátedra 4):** asociación, agregación, composición y dependencia, explicadas en la sección siguiente.
4. **Validaciones, interfaces y búsqueda polimórfica (Cátedra 3):** setters que lanzan `IllegalArgumentException`, `Prestable` y `Buscable[]` mezclando libros y socios.

`Prestamo.registrarDevolucion()` es el único lugar que encadena la devolución (`libro.devolver()` y `socio.registrarDevolucion()`), para que el contador de libros del socio no se descuente dos veces.

## Cómo compilar y ejecutar
```bash
javac *.java
java Main
```