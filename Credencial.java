import java.time.LocalDate;

/*
 * Credencial (carnet) de un Socio. Es la parte de una COMPOSICION (Catedra 4):
 * la crea el propio Socio dentro de su constructor, no se recibe desde afuera
 * y no tiene sentido sin el socio al que pertenece.
 */
public class Credencial {

    // --- ATRIBUTOS ---
    private final int numero;              // coincide con el numero de socio; no cambia
    private final LocalDate fechaEmision;  // se fija al crear la credencial
    private boolean vigente;

    // Constructor de paquete (sin public): solo las clases del proyecto, en la practica Socio,
    // pueden crear credenciales.
    Credencial(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El numero de credencial debe ser mayor que 0");
        }
        this.numero = numero;
        this.fechaEmision = LocalDate.now();
        this.vigente = true;
    }

    // --- GETTERS (sin setters de numero ni fecha: son inmutables) ---
    public int getNumero() {
        return numero;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public boolean isVigente() {
        return vigente;
    }

    // El estado solo cambia con este metodo.
    public void revocar() {
        vigente = false;
    }

    @Override
    public String toString() {
        return "Credencial #" + numero + " (emitida " + fechaEmision
                + ", " + (vigente ? "vigente" : "revocada") + ")";
    }
}