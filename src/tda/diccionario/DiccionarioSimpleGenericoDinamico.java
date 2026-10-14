package tda.diccionario;

import tda.conjunto.ConjuntoDinamico;
import tda.conjunto.ConjuntoTDA;

/**
 * Implementación dinámica del Diccionario Simple Genérico.
 * Usa una lista enlazada de nodos (clave int → valor V).
 *
 * Complejidad:
 *   Agregar   → O(n)
 *   Eliminar  → O(n)
 *   Recuperar → O(n)
 *   Claves    → O(n)
 */
public class DiccionarioSimpleGenericoDinamico<V> implements DiccionarioSimpleGenericoTDA<V> {

    private class NodoClave {
        int clave;
        V valor;
        NodoClave sigClave;
    }

    private NodoClave origen;

    @Override
    public void InicializarDiccionario() { //O(1)
        origen = null;
    }

    @Override
    public void Agregar(int clave, V valor) { //O(n)
        NodoClave nc = Clave2NodoClave(clave);
        if (nc == null) {
            nc = new NodoClave();
            nc.clave = clave;
            nc.sigClave = origen;
            origen = nc;
        }
        nc.valor = valor;
    }

    private NodoClave Clave2NodoClave(int clave) { //O(n)
        NodoClave aux = origen;
        while (aux != null && aux.clave != clave)
            aux = aux.sigClave;
        return aux;
    }

    @Override
    public void Eliminar(int clave) { //O(n)
        if (origen != null) {
            if (origen.clave == clave) {
                origen = origen.sigClave;
            } else {
                NodoClave aux = origen;
                while (aux.sigClave != null && aux.sigClave.clave != clave)
                    aux = aux.sigClave;
                if (aux.sigClave != null)
                    aux.sigClave = aux.sigClave.sigClave;
            }
        }
    }

    @Override
    public V Recuperar(int clave) { //O(n)
        NodoClave n = Clave2NodoClave(clave);
        return (n != null) ? n.valor : null;
    }

    @Override
    public ConjuntoTDA Claves() { //O(n)
        ConjuntoTDA c = new ConjuntoDinamico();
        c.InicializarConjunto();
        NodoClave aux = origen;
        while (aux != null) {
            c.Agregar(aux.clave);
            aux = aux.sigClave;
        }
        return c;
    }
}
