package tda.conjunto;

public interface ConjuntoTDA {
    void InicializarConjunto();

    // Precondición: el conjunto debe estar inicializado
    boolean ConjuntoVacio();

    // Precondición: el conjunto debe estar inicializado
    void Agregar(int x);

    // Precondición: el conjunto debe estar inicializado y no estar vacío
    int Elegir();

    // Precondición: el conjunto debe estar inicializado
    void Sacar(int x);

    // Precondición: el conjunto debe estar inicializado
    boolean Pertenece(int x);
}
