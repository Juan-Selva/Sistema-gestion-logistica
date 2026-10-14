package tda.colaprioridad;

public interface ColaPrioridadTDA {
    void InicializarCola();

    // Precondición: la cola debe estar inicializada
    void AcolarPrioridad(int x, int prioridad);

    // Precondición: la cola debe estar inicializada y no estar vacía
    void Desacolar();

    // Precondición: la cola debe estar inicializada y no estar vacía
    int Primero();

    // Precondición: la cola debe estar inicializada
    boolean ColaVacia();

    // Precondición: la cola debe estar inicializada y no estar vacía
    int Prioridad();
}
