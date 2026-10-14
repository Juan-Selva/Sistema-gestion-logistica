package tda.colaprioridad;

public class ColaPrioridadDinamica implements ColaPrioridadTDA {


    private class NodoPrioridad {
        int info;
        int prioridad;
        NodoPrioridad sig;
    }

    private NodoPrioridad mayorPrioridad;

    
    @Override
    public void InicializarCola() {// O(1)
        mayorPrioridad = null;
    }

    @Override
    public void AcolarPrioridad(int x, int prioridad) {// O(n)
        NodoPrioridad nuevo = new NodoPrioridad();
        nuevo.info = x;
        nuevo.prioridad = prioridad;

        if (mayorPrioridad == null || prioridad > mayorPrioridad.prioridad) {
            nuevo.sig = mayorPrioridad;
            mayorPrioridad = nuevo;
        } else {
            NodoPrioridad aux = mayorPrioridad;
            while (aux.sig != null && aux.sig.prioridad >= prioridad) {
                aux = aux.sig;
            }
            nuevo.sig = aux.sig;
            aux.sig = nuevo;
        }
    }

    @Override
    public void Desacolar() { // O(1)
        mayorPrioridad = mayorPrioridad.sig;
    }

    @Override
    public int Primero() { // O(1)
        return mayorPrioridad.info;
    }

    @Override
    public boolean ColaVacia() { // O(1)
        return (mayorPrioridad == null);
    }

    @Override
    public int Prioridad() { // O(1)
        return mayorPrioridad.prioridad;
    }
}
