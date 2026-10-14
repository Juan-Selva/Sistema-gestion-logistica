package tda.arbolBinario;

public interface ABBTDA {
    //Siempre que el arbol este inicializado y no este vacio
    int Raiz();

    //Siempre que el arbol este inicializado y no este vacio
    ABBTDA HijoIzq();

    //Siempre que el arbol este inicializado y no este vacio
    ABBTDA HijoDer();

    //Siempre que el arbol este inicializado
    boolean ArbolVacio();

    void InicializarArbol();

    //Siempre que el arbol este inicializado
    void AgregarElem(int x);

    //Siempre que el arbol este inicializado
    void EliminarElem(int x);
}
