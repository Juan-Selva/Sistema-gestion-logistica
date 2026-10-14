package servicio;

import modelo.PaqueteTDA;
import tda.conjunto.ConjuntoTDA;
import tda.diccionario.DiccionarioSimpleGenericoDinamico;
import tda.diccionario.DiccionarioSimpleGenericoTDA;
import tda.pila.PilaDinamica;
import tda.pila.PilaTDA;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona la carga de paquetes en un camión usando una Pila (LIFO).
 * El último paquete cargado es el primero en descargarse.
 *
 * Usa PilaTDA para el orden LIFO de IDs y DiccionarioSimpleGenericoTDA
 * para recuperar el objeto PaqueteTDA completo a partir del ID. O(1).
 */

public class Camion {

    private final String patente;
    private final PilaTDA pilaCarga;
    private final DiccionarioSimpleGenericoTDA<PaqueteTDA<?>> almacen;

    //Constructor
    public Camion(String patente) {
        this.patente = patente;
        this.pilaCarga = new PilaDinamica();
        this.pilaCarga.InicializarPila();
        this.almacen = new DiccionarioSimpleGenericoDinamico<>();
        this.almacen.InicializarDiccionario();
    }

    //Carga un paquete en el camión. O(1).
    public void cargarPaquete(PaqueteTDA<?> paquete) {
        pilaCarga.Apilar(paquete.getId());
        almacen.Agregar(paquete.getId(), paquete);
    }

    //Deshace la última carga en caso de error de destino. O(1).
    public PaqueteTDA<?> deshacerUltimaCarga() {
        if (estaVacio()) return null;
        int id = pilaCarga.Tope();
        pilaCarga.Desapilar();
        PaqueteTDA<?> paquete = almacen.Recuperar(id);
        almacen.Eliminar(id);
        return paquete;
    }

    //Descarga el próximo paquete (LIFO). O(1).
    public PaqueteTDA<?> descargarPaquete() {
        return deshacerUltimaCarga();
    }

    //Consulta el próximo paquete a descargar sin removerlo. O(1).
    public PaqueteTDA<?> verProximo() {
        if (estaVacio()) return null;
        return almacen.Recuperar(pilaCarga.Tope());
    }

    // Verifica si el camión está vacío. O(1).
    public boolean estaVacio() {
        return pilaCarga.PilaVacia();
    }

    // Retorna todos los paquetes almacenados sin modificar la estructura. O(n).
    public List<PaqueteTDA<?>> getPaquetes() {
        List<PaqueteTDA<?>> lista = new ArrayList<>();
        ConjuntoTDA claves = almacen.Claves();
        while (!claves.ConjuntoVacio()) {
            int id = claves.Elegir();
            lista.add(almacen.Recuperar(id));
            claves.Sacar(id);
        }
        return lista;
    }

    // Getter para la patente del camión O(1).
    public String getPatente() {
        return patente;
    }
}
