package tda.conjunto;

public class ConjuntoDinamico implements ConjuntoTDA {

    private class Nodo {
        int info;
        Nodo sig;
    }

    private Nodo c;

    @Override
    public void InicializarConjunto() { //O(1)
        c = null;
    }

    @Override
    public boolean ConjuntoVacio() { //O(1)
        return (c == null);
    }

    @Override
    public void Agregar(int x) { //O(n)
        if (!this.Pertenece(x)) {
            Nodo aux = new Nodo();
            aux.info = x;
            aux.sig = c;
            c = aux;
        }
    }

    @Override
    public int Elegir() { //O(1)
        return c.info;
    }

    @Override
    public void Sacar(int x) { //O(n)
        if (c != null) {
            if (c.info == x) {
                c = c.sig;
            } else {
                Nodo aux = c;
                while (aux.sig != null && aux.sig.info != x)
                    aux = aux.sig;
                if (aux.sig != null)
                    aux.sig = aux.sig.sig;
            }
        }
    }

    @Override
    public boolean Pertenece(int x) { //O(n)
        Nodo aux = c;
        while ((aux != null) && (aux.info != x))
            aux = aux.sig;
        return (aux != null);
    }
}
