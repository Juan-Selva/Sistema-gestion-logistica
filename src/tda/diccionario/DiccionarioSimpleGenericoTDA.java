package tda.diccionario;

import tda.conjunto.ConjuntoTDA;

/**
 * TDA Diccionario Simple Genérico.
 * Mapea claves enteras (int) a valores de tipo V.
 * Permite reemplazar estructuras externas como HashMap para tipos de dominio.
 */
public interface DiccionarioSimpleGenericoTDA<V> {

    void InicializarDiccionario();

    // Precondición: el diccionario debe estar inicializado
    void Agregar(int clave, V valor);

    // Precondición: el diccionario debe estar inicializado
    void Eliminar(int clave);

    // Precondición: el diccionario debe estar inicializado y la clave debe existir
    V Recuperar(int clave);

    // Precondición: el diccionario debe estar inicializado
    ConjuntoTDA Claves();
}
