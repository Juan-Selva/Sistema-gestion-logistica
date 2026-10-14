package modelo;

/**
 * TDA genérico que representa un paquete a transportar.
 * T define el tipo de contenido (ej. TipoCarga).
 */
public interface PaqueteTDA<T> {

    // Precondición: debe llamarse antes de cualquier otra operación
    void inicializar(int id, double peso, String destino, T contenido, boolean urgente);

    // Precondición: el paquete debe estar inicializado
    int getId();

    // Precondición: el paquete debe estar inicializado
    double getPeso();

    // Precondición: el paquete debe estar inicializado
    String getDestino();

    // Precondición: el paquete debe estar inicializado
    T getContenido();

    // Precondición: el paquete debe estar inicializado
    boolean isUrgente();
}
