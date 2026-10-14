package tda.pila;

public class PilaDinamica implements PilaTDA {

    private class Nodo {
        int info;
        Nodo sig;
    }

    private Nodo primero;

    @Override
    public void InicializarPila() { //O(1)
        primero = null;
    }

    @Override
    public void Apilar(int x) {//O(1)
        Nodo aux = new Nodo();
        aux.info = x;
        aux.sig = primero;
        primero = aux;
    }

    @Override
    public void Desapilar() {//O(1)
        primero = primero.sig;
    }

    @Override
    public boolean PilaVacia() {//O(1)
        return (primero == null);
    }

    @Override
    public int Tope() {//O(1)
        return primero.info;
    }
}
