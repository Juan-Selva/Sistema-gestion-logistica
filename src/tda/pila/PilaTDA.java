package tda.pila;

public interface PilaTDA {
    void InicializarPila();

    // Precondición: la pila debe estar inicializada
    void Apilar(int x);

    // Precondición: la pila debe estar inicializada y no estar vacía
    void Desapilar();

    // Precondición: la pila debe estar inicializada
    boolean PilaVacia();

    // Precondición: la pila debe estar inicializada y no estar vacía
    int Tope();
}
