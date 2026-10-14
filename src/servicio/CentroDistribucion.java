package servicio;

import modelo.PaqueteTDA;
import tda.colaprioridad.ColaPrioridadDinamica;
import tda.colaprioridad.ColaPrioridadTDA;
import tda.diccionario.DiccionarioSimpleGenericoDinamico;
import tda.diccionario.DiccionarioSimpleGenericoTDA;

/**
 * Gestiona el procesamiento de paquetes en el centro de distribución.
 * Los paquetes urgentes o con peso > 50 kg tienen prioridad alta (2).
 * Los paquetes estándar tienen prioridad normal (1).
 *
 * Usa ColaPrioridadTDA para ordenar IDs por prioridad y
 * DiccionarioSimpleGenericoTDA para recuperar el objeto PaqueteTDA por ID.
 *
 * Complejidad:
 *   recibirPaquete    → O(n) por inserción ordenada en la cola
 *   procesarSiguiente → O(1)
 *   verSiguiente      → O(1)
 */
public class CentroDistribucion {

    private static final int PRIORIDAD_ALTA = 2;
    private static final int PRIORIDAD_NORMAL = 1;
    private static final double PESO_LIMITE_ALTA_PRIORIDAD = 50.0;

    private final ColaPrioridadTDA cola;
    private final DiccionarioSimpleGenericoTDA<PaqueteTDA<?>> almacen;

    //Constructor
    public CentroDistribucion() {
        this.cola = new ColaPrioridadDinamica();
        this.cola.InicializarCola();
        this.almacen = new DiccionarioSimpleGenericoDinamico<>();
        this.almacen.InicializarDiccionario();
    }

    //Recibe un paquete y lo encola según su prioridad. O(n).
    public void recibirPaquete(PaqueteTDA<?> paquete) {
        int prioridad = calcularPrioridad(paquete);
        cola.AcolarPrioridad(paquete.getId(), prioridad);
        almacen.Agregar(paquete.getId(), paquete);
    }

    //Calcula la prioridad de un paquete. O(1).
    private int calcularPrioridad(PaqueteTDA<?> paquete) {
        if (paquete.isUrgente() || paquete.getPeso() > PESO_LIMITE_ALTA_PRIORIDAD) {
            return PRIORIDAD_ALTA;
        }
        return PRIORIDAD_NORMAL;
    }

    //Procesa y retira el siguiente paquete de mayor prioridad. O(1).
    public PaqueteTDA<?> procesarSiguiente() {
        if (estaVacio()) return null;
        int id = cola.Primero();
        cola.Desacolar();
        PaqueteTDA<?> paquete = almacen.Recuperar(id);
        almacen.Eliminar(id);
        return paquete;
    }

   //Consulta el siguiente paquete a procesar sin removerlo. O(1).
    public PaqueteTDA<?> verSiguiente() {
        if (estaVacio()) return null;
        return almacen.Recuperar(cola.Primero());
    }

    //Verifica si el centro de distribución está vacío. O(1).
    public boolean estaVacio() {
        return cola.ColaVacia();
    }
}
