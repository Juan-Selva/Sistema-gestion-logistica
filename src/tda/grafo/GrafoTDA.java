package tda.grafo;

import tda.conjunto.ConjuntoTDA;

public interface GrafoTDA {
    void InicializarGrafo();

    //Siempre que el grafo este inicializado y no exista el nodo
    void AgregarVertice(int v);

    //Siempre que el grafo este inicializado y exista el nodo
    void EliminarVertice(int v);

    //Siempre que el grafo este inicializado
    ConjuntoTDA Vertices();

    //Siempre que el grafo este inicializado, no exista la arista, pero existan ambos nodos
    void AgregarArista(int v1, int v2, int peso);

    //Siempre que el grafo este inicializado y exista la arista
    void EliminarArista(int v1, int v2);

    //Siempre que el grafo este inicializado y existan los nodos
    boolean ExisteArista(int v1, int v2);

    //Siempre que el grafo este inicializado y exista la arista
    int PesoArista(int v1, int v2);
}
