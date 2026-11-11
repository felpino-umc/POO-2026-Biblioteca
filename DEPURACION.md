# Registro de depuración del proyecto de POO

**Proyecto:** POO-2026-Biblioteca
**Equipo o estudiante:** Felipe Pino, Matías Araya, Cristian Díaz
**Repositorio:** https://github.com/felpino-umc/POO-2026-Biblioteca

## Episodio de depuración 1

**1. Problema detectado**

Se esperaba que no se pudiera crear un `Prestamo` con fecha futura: esa regla
ya existía en `setFechaPrestamo`, que lanza `IllegalArgumentException` si la
fecha es posterior a hoy. Sin embargo, al crear un préstamo mediante el
constructor, la fecha se asignaba directamente sin pasar por esa validación,
por lo que este caso se ejecutaba sin ningún error:

```java
Prestamo p = new Prestamo(libro, socio, LocalDate.of(2030, 1, 1));
p.registrarPrestamo();
// Resultado: préstamo activo con "Fecha de préstamo: 2030-01-01"
```

El objeto quedaba en un estado inválido (un préstamo "realizado" en el futuro)
sin mensaje de error.

**2. Cómo se detectó**

Durante la preparación de la C4, al revisar `Prestamo` para integrarlo con la
nueva clase `Biblioteca`, se ejecutó un caso de prueba que creaba un préstamo
con `LocalDate.of(2030, 1, 1)`. El préstamo se registró normalmente y
`mostrarInfo()` imprimió la fecha 2030, cuando lo esperado era una
`IllegalArgumentException`.

**3. Decisión técnica adoptada**

Hacer que el constructor reutilice la validación del setter en lugar de asignar
el atributo directamente. Es el mismo criterio que ya se aplicó en el resto de
las clases tras la retroalimentación de la Solemne 1 ("constructores que
delegan en setters"): la regla queda escrita una sola vez y es imposible
saltársela al construir el objeto. Además se bajó la visibilidad de
`setFechaPrestamo` a `private`, porque la fecha de un préstamo es un hecho
histórico que no tiene que mutar desde afuera (mismo criterio que `Libro`,
que no expone `setDisponible`).

**4. Modificación realizada**

Archivo `Prestamo.java`, constructor:

- Antes: `this.fechaPrestamo = fechaPrestamo;`
- Después: `setFechaPrestamo(fechaPrestamo);`
- `setFechaPrestamo` pasó de `public` a `private`.

**5. Verificación del resultado**

Se recompiló (`javac *.java`) y se volvió a ejecutar el mismo caso de prueba:
ahora lanza `IllegalArgumentException` con el mensaje "La fecha de préstamo no
puede ser nula ni ser del futuro", y el objeto inválido no llega a crearse.
`Main` incluye este caso en la sección "VALIDACIÓN DE FECHA DE PRÉSTAMO
FUTURA", manejado con try/catch, y el flujo normal de préstamos y devoluciones
sigue funcionando igual.

**6. Evidencia del cambio en GitHub**

- Enlace al commit: [Se corrige validacion de una fecha futura en el constructor](https://github.com/felpino-umc/POO-2026-Biblioteca/commit/390b633f2548af301d9fe705175454a7fa10a90c)
- Integrantes que participaron: Matías Araya