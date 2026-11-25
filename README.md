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
- PoliticaMulta: Interfaz que define cómo se calcula la multa por atraso (dependencia).
- CalculadoraMulta / MultaReducida: Las dos políticas de multa (estándar y reducida).
- ReporteBiblioteca: Muestra por consola el catálogo y los préstamos.

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

Además, `Persona`, `Autor`, `Socio` y `Libro` ahora exponen sus atributos como `private` con getters/setters, en vez de exponer los atributos directamente como en la C1. `Persona` y `Autor` conservan constructores sobrecargados; `Libro` y `Socio` ya no tienen constructor vacío porque tienen atributos `final`.

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
| **Dependencia** | `Biblioteca.devolver` → `PoliticaMulta` | La política llega como parámetro en `devolver(...)`, se usa y no se guarda como atributo | Uso temporal de un servicio para calcular la multa por atraso |

Diferencia clave entre composición y agregación: en la composición el contenedor **crea** a sus partes con `new` y estas dependen de él; en la agregación el contenedor **recibe** objetos ya creados y estos siguen existiendo si el contenedor desaparece.

`Biblioteca` además ofrece `buscar(String)`, que reúne libros y socios que cumplan `Buscable.coincideCon`, y evita ISBN duplicados en el catálogo.

### Encapsulamiento en C4
Todos los atributos son `private`. `Biblioteca` expone sus listas solo como vistas inmodificables, para que nadie las modifique fuera de sus métodos.

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

## Persistencia con JDBC y MariaDB (AWS)

El catálogo de libros también se puede guardar en una base de datos MariaDB, alojada en una instancia EC2 de AWS (Ubuntu), usando JDBC con el driver `mysql-connector-j-8.3.0`. Es un ejercicio para practicar el patrón DAO sobre este proyecto.

### Clases agregadas

| Archivo | Rol | Principio |
|---|---|---|
| `ConexionBD.java` | Entrega conexiones JDBC; no conoce libros ni SQL de negocio | SRP |
| `LibroDAO.java` | Interfaz CRUD: `crear`, `buscarPorIsbn`, `listarTodos`, `actualizar`, `eliminar` | DIP, OCP |
| `LibroDAOImpl.java` | Implementación JDBC con `PreparedStatement` | |
| `MainBD.java` | Demuestra las cuatro operaciones CRUD sobre `Libro` | |

El resto del programa depende de la interfaz `LibroDAO` y no de JDBC, de modo que se puede agregar otra implementación sin modificar a quien la usa.

Único cambio en una clase existente: `Libro` incorpora un constructor de paquete que recibe `disponible`, para que el DAO reconstruya un libro leído de la base sin llamar a `prestar()` ni `devolver()`.

### Modelo de datos

| Clase Java | Tabla | Relación |
|---|---|---|
| `Persona` (abstracta) | `persona` | Base de la herencia |
| `Autor` | `autor` | Hereda de `persona` (misma clave primaria) |
| `Socio` | `socio` | Hereda de `persona` (misma clave primaria) |
| `Credencial` | `credencial` | Composición 1 a 1 con `socio` (`ON DELETE CASCADE`) |
| `Libro` | `libro` | N a 1 con `autor`; pertenece a una `biblioteca` |
| `Prestamo` | `prestamo` | Asocia `libro` y `socio`; pertenece a una `biblioteca` |
| `Biblioteca` | `biblioteca` | Contenedor central |

Las reglas del dominio también se reflejan en la base: plazo de 14 días y multa de $500 por día (vista `v_prestamos`), máximo de 3 préstamos activos por socio y disponibilidad del libro (triggers). `CalculadoraMulta` no tiene tabla; su lógica queda en la vista.

El script `biblioteca_mariadb.sql` crea las tablas, triggers y vistas, y carga datos de muestra (6 autores, 4 socios, 7 libros y 5 préstamos). Crea la base `POO_2026_Biblioteca` (la misma que usa `ConexionBD.java`) y **borra y recrea** cualquier base con ese nombre, así que no hay que ejecutarlo si ya contiene datos propios. Para cargarlo: `sudo mysql < biblioteca_mariadb.sql`.

### Configuración

Los datos de conexión están como constantes en `ConexionBD.java` (`HOST`, `BASE_DATOS`, `USUARIO`, `PASSWORD`). La contraseña del repositorio es solo un valor de ejemplo: la real se configura únicamente en la instancia y no se sube a Git.

El usuario necesita permisos sobre la base:

```sql
CREATE USER 'usuario'@'localhost' IDENTIFIED BY 'clave';
GRANT ALL PRIVILEGES ON nombre_base.* TO 'usuario'@'localhost';
FLUSH PRIVILEGES;
```

### Compilar y ejecutar con la base de datos

El archivo `mysql-connector-j-8.3.0.jar` debe estar en el classpath al ejecutar (no hace falta para compilar):

```bash
javac *.java
java -cp ".:mysql-connector-j-8.3.0.jar" MainBD
```

En Windows el separador del classpath es `;` en lugar de `:`.

`MainBD` lista el catálogo, crea el libro *Rayuela* (y a su autor, si no existe), lo busca por ISBN, lo actualiza y lo elimina. Eliminar un libro con préstamos registrados falla a propósito, porque la clave foránea lo protege.

## Solemne 2 — Tabla de cumplimiento

### Capítulo 4 — Encapsulamiento

| Indicador | Ubicación en el código | Evidencia / cambio respecto de C4 |
|---|---|---|
| 1. Atributos privados | Todas las clases del dominio; por ejemplo [`Socio`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Socio.java#L3-L13) | No hay atributos de instancia públicos |
| 2. Al menos 5 setters con validación | [`Persona.setNombre`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Persona.java#L26), [`Autor.setNacionalidad`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Autor.java#L30), [`Autor.setAnioNacimiento`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Autor.java#L41), [`Libro.setTitulo`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Libro.java#L44), [`Libro.setAnioPublicacion`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Libro.java#L59), [`Libro.setAutor`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Libro.java#L78), [`Socio.setEmail`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Socio.java#L72), [`Socio.setLibrosPrestadosActuales`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Socio.java#L83), [`Biblioteca.setNombre`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Biblioteca.java#L46) | Lanzan `IllegalArgumentException` ante datos inválidos (se mantienen desde C4) |
| 3. Al menos 3 atributos `final` | [`Libro.isbn`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Libro.java#L8), [`Socio.numeroSocio`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Socio.java#L4), [`Socio.credencial`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Socio.java#L8), [`Credencial.numero` y `fechaEmision`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Credencial.java#L11-L12), [`Prestamo.libro` y `socio`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Prestamo.java#L10-L11) | En S2 `Prestamo.libro` y `socio` pasaron a `final` |
| 4. Al menos 2 constantes `private static final` | [`Socio.MAX_LIBROS`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Socio.java#L13), [`CalculadoraMulta.PLAZO_DIAS` y `MULTA_POR_DIA`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/CalculadoraMulta.java#L15-L16), [las de `MultaReducida`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/MultaReducida.java#L12-L13) | Las constantes de `CalculadoraMulta` eran `public`; ahora son privadas |
| 5. Estado mutable protegido | [`Biblioteca.getLibros/getSocios/getPrestamos`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Biblioteca.java#L59-L69), [`Biblioteca.buscar`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Biblioteca.java#L201), [`Socio.getCredencial`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Socio.java#L59-L67), [`Libro.getAutor`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Libro.java#L74); demostración en [`Main.demoEncapsulamiento`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Main.java#L206-L237) | Vistas inmodificables y copias defensivas (antes se entregaba la referencia interna); `Prestamo` ya no expone `getLibro()` ni `getSocio()` |

### Capítulo 5 — Abstracción e interfaces

| Indicador | Ubicación en el código | Evidencia / cambio respecto de C4 |
|---|---|---|
| 1. Clase abstracta con método abstracto | [`Persona`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Persona.java#L8) y [`mostrarRol()`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Persona.java#L47) | Se mantiene desde la Solemne 1; lo implementan `Autor` y `Socio` |
| 2. Justificación en Javadoc | [Javadoc de `Persona`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Persona.java#L1-L7) | El comentario `/* */` pasó a Javadoc `/** */` |
| 3. Dos interfaces con al menos 2 métodos | [`Prestable`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Prestable.java#L8-L15) (3 métodos) y [`Buscable`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Buscable.java#L8-L15) (2 métodos) | `Buscable` tenía solo `coincideCon`; se agregó `getEtiqueta()` y ambas tienen Javadoc |
| 4. Una clase implementa ambas | [`Libro implements Prestable, Buscable`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Libro.java#L4) | `Libro` además implementa `getEtiqueta()` |
| 5. Polimorfismo con tipo interfaz | [`Prestable prestable = libro3`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Main.java#L169-L175), [`Buscable[]`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Main.java#L179-L186) y [el recorrido de `buscar`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/44584d6/Main.java#L234) | En S2 los resultados de búsqueda se muestran con `getEtiqueta()`, sin `instanceof` |

### Capítulo 6 — Relaciones entre clases

| Indicador | Ubicación en el código | Evidencia / cambio respecto de C4 |
|---|---|---|
| **Composición**: `RELACIÓN: composición — Socio crea Credencial` | [constructor de `Socio`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/8d76123/Socio.java#L19-L21) | La credencial nace con el socio. En S2 `getCredencial()` entrega una copia. Secundaria: [`Biblioteca` crea `Prestamo`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/8d76123/Biblioteca.java#L31-L33), con el `new` en [`prestar`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/8d76123/Biblioteca.java#L146-L151) |
| **Agregación**: `RELACIÓN: agregación — Biblioteca agrupa Libro y Socio` | [atributos de `Biblioteca`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/8d76123/Biblioteca.java#L25-L27), [`agregarLibro`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/8d76123/Biblioteca.java#L84-L95) y [`registrarSocio`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/8d76123/Biblioteca.java#L113-L121) | Recibe objetos ya creados. En S2 se agregó el comentario y las listas se exponen como vistas inmodificables |
| **Asociación**: `RELACIÓN: asociación — Libro referencia a Autor` | [`Libro.autor`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/8d76123/Libro.java#L11-L13) y [`Prestamo.libro` y `socio`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/8d76123/Prestamo.java#L6-L11) | Objetos independientes. En S2 `Libro.getAutor()` entrega una copia y `Prestamo` ya no expone `getLibro()` ni `getSocio()` |
| **Dependencia**: `RELACIÓN: dependencia — Biblioteca usa PoliticaMulta` | [`Biblioteca.devolver`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/8d76123/Biblioteca.java#L159-L196) | En C4 creaba una `CalculadoraMulta` como variable local; ahora recibe una `PoliticaMulta` por parámetro y no la guarda |

`Main` ejecuta las cuatro relaciones en sus secciones 3 a 6.

### Capítulo 7 — Principios SRP, OCP y DIP

| Indicador | Ubicación en el código | Evidencia / cambio respecto de C4 |
|---|---|---|
| **SRP** (responsabilidad única) | [`ReporteBiblioteca`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/455cbab/ReporteBiblioteca.java#L1-L48): `mostrarCatalogo`, `mostrarPrestamos`, `mostrarPrestamo`; comentario en [`Biblioteca`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/455cbab/Biblioteca.java#L18-L20) | En C4, `Biblioteca` administraba catálogo, socios y préstamos y además imprimía los reportes; `Prestamo.mostrarInfo` también imprimía. Los reportes pasaron a una clase propia: [`75cd44f`](https://github.com/felpino-umc/POO-2026-Biblioteca/commit/75cd44f), [`3568f68`](https://github.com/felpino-umc/POO-2026-Biblioteca/commit/3568f68) |
| **OCP** (abierto/cerrado) | [`MultaReducida`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/455cbab/MultaReducida.java#L5-L34) `implements PoliticaMulta` | Regla de cobro nueva agregada como clase nueva, sin modificar `PoliticaMulta`, `CalculadoraMulta` ni `Biblioteca`. El commit [`b3c58bd`](https://github.com/felpino-umc/POO-2026-Biblioteca/commit/b3c58bd) contiene un solo archivo |
| **DIP** (inversión de dependencias) | [`Biblioteca.devolver(Libro, LocalDate, PoliticaMulta)`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/455cbab/Biblioteca.java#L159-L196); abstracción [`PoliticaMulta`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/455cbab/PoliticaMulta.java#L3-L18); la implementación se elige en [`Main.demoOcpDip`](https://github.com/felpino-umc/POO-2026-Biblioteca/blob/455cbab/Main.java#L240-L262) | En C4, `devolver` hacía `new CalculadoraMulta()`. Ahora recibe la interfaz por parámetro: [`3a2a6ba`](https://github.com/felpino-umc/POO-2026-Biblioteca/commit/3a2a6ba), [`0887c4c`](https://github.com/felpino-umc/POO-2026-Biblioteca/commit/0887c4c) |

**Responsabilidad de cada clase del dominio**

| Clase | Responsabilidad |
|---|---|
| `Persona` | Datos y comportamiento comunes a toda persona |
| `Autor` | Datos de quien escribe un libro |
| `Socio` | Datos del miembro y su cupo de préstamos |
| `Credencial` | Estado del carnet de un socio |
| `Libro` | Datos y disponibilidad de un libro |
| `Prestamo` | Ciclo de un préstamo (registrar y devolver) |
| `Biblioteca` | Administrar catálogo, socios y préstamos |
| `CalculadoraMulta` / `MultaReducida` | Una regla de cobro cada una |
| `ReporteBiblioteca` | Presentar por consola el estado de la biblioteca |

### Qué cambió respecto de C4

El tag [`C4`](https://github.com/felpino-umc/POO-2026-Biblioteca/tree/C4) marca el último commit de la Cátedra 4. La comparación completa está en [`C4...main`](https://github.com/felpino-umc/POO-2026-Biblioteca/compare/C4...main) e incluye también la capa JDBC, agregada después de C4 y fuera de esta refactorización.

- **Encapsulamiento:** constantes de multa privadas, listas de `Biblioteca` como vistas inmodificables, copias defensivas de `Autor` y `Credencial`, y `Prestamo` ya no expone `Libro` ni `Socio`.
- **Interfaces:** `Buscable` pasó de uno a dos métodos (`getEtiqueta()`).
- **Relaciones:** comentarios `RELACIÓN:` en el código.
- **Principios:** `PoliticaMulta`, `MultaReducida` y `ReporteBiblioteca`.

### Cómo verificar

```bash
javac -encoding UTF-8 *.java
java Main
git log --oneline C4..HEAD
```

En la salida de `java Main`, la sección *S2 · CAP. 7: OCP y DIP* muestra el mismo atraso cobrado con dos políticas: $20500 (estándar) y $10250 (reducida).
