package modelo;

/**
 * Implementación del TDA genérico Paquete<T>.
 * T define el tipo de contenido transportado (ej. TipoCarga).
 *
 * Complejidad espacial: O(1) por instancia.
 */
public class Paquete<T> implements PaqueteTDA<T> {

    private int id;
    private double peso;
    private String destino;
    private T contenido;
    private boolean urgente;

    //Constructor
    @Override
    public void inicializar(int id, double peso, String destino, T contenido, boolean urgente) {
        this.id = id;
        this.peso = peso;
        this.destino = destino;
        this.contenido = contenido;
        this.urgente = urgente;
    }

   
    @Override
    public int getId() {
        return id;
    }

    @Override
    public double getPeso() {
        return peso;
    }

    @Override
    public String getDestino() {
        return destino;
    }

    @Override
    public T getContenido() {
        return contenido;
    }

    
    @Override
    public boolean isUrgente() {
        return urgente;
    }

    @Override
    public String toString() {
        return String.format("Paquete[id=%d | peso=%.1f kg | destino=%s | tipo=%s | urgente=%s]",
                id, peso, destino, contenido, urgente ? "SI" : "NO");
    }
}
