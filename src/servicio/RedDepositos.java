package servicio;

import modelo.DepositoTDA;
import tda.arbolBinario.ABB;
import tda.arbolBinario.ABBTDA;
import tda.diccionario.DiccionarioSimpleGenericoDinamico;
import tda.diccionario.DiccionarioSimpleGenericoTDA;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona la red de depósitos regionales usando un ABB ordenado por ID.
 * El ABB mantiene la jerarquía por ID y el diccionario permite recuperar
 * el objeto DepositoTDA completo en O(log n) promedio / O(n) peor caso.
 *
 * Complejidad:
 *   agregarDeposito    → O(log n) promedio, O(n) peor caso
 *   eliminarDeposito   → O(log n) promedio, O(n) peor caso
 *   buscarDeposito     → O(n) por el diccionario
 *   realizarAuditoria  → O(n) recorrido post-orden
 *   depositosEnNivel   → O(n) recorrido por nivel
 */
public class RedDepositos {

    private final ABBTDA arbol;
    private final DiccionarioSimpleGenericoTDA<DepositoTDA> almacen;

    public RedDepositos() {
        this.arbol = new ABB();
        this.arbol.InicializarArbol();
        this.almacen = new DiccionarioSimpleGenericoDinamico<>();
        this.almacen.InicializarDiccionario();
    }

    // Agrega un depósito a la red. O(log n) promedio.
    public void agregarDeposito(DepositoTDA deposito) {
        arbol.AgregarElem(deposito.getId());
        almacen.Agregar(deposito.getId(), deposito);
    }

    // Elimina un depósito de la red por ID. O(log n) promedio.
    public void eliminarDeposito(int id) {
        arbol.EliminarElem(id);
        almacen.Eliminar(id);
    }

    // Busca y retorna un depósito por ID. O(n).
    public DepositoTDA buscarDeposito(int id) {
        return almacen.Recuperar(id);
    }

    // Verifica si la red no tiene depósitos. O(1).
    public boolean estaVacia() {
        return arbol.ArbolVacio();
    }

    /**
     * Auditoría post-orden: recorre el árbol y marca como visitado
     * los depósitos que no han sido auditados en los últimos 30 días. O(n).
     */
    public void realizarAuditoria() {
        auditarPostOrden(arbol, LocalDateTime.now().minusDays(30));
    }

    private void auditarPostOrden(ABBTDA nodo, LocalDateTime umbral) {
        if (!nodo.ArbolVacio()) {
            auditarPostOrden(nodo.HijoIzq(), umbral);
            auditarPostOrden(nodo.HijoDer(), umbral);
            DepositoTDA deposito = almacen.Recuperar(nodo.Raiz());
            if (deposito.getFechaUltimaAuditoria() == null ||
                    deposito.getFechaUltimaAuditoria().isBefore(umbral)) {
                deposito.setVisitado(true);
            }
        }
    }

    /**
     * Retorna todos los depósitos ordenados por ID (recorrido in-orden). O(n).
     */
    public List<DepositoTDA> listarDepositos() {
        List<DepositoTDA> resultado = new ArrayList<>();
        listarInOrden(arbol, resultado);
        return resultado;
    }

    private void listarInOrden(ABBTDA nodo, List<DepositoTDA> resultado) {
        if (!nodo.ArbolVacio()) {
            listarInOrden(nodo.HijoIzq(), resultado);
            resultado.add(almacen.Recuperar(nodo.Raiz()));
            listarInOrden(nodo.HijoDer(), resultado);
        }
    }

    /**
     * Retorna los depósitos que se encuentran en el nivel N del árbol.
     * El nivel 0 es la raíz. O(n).
     */
    public List<DepositoTDA> depositosEnNivel(int nivel) {
        List<DepositoTDA> resultado = new ArrayList<>();
        depositosEnNivelRec(arbol, nivel, 0, resultado);
        return resultado;
    }

    // Método recursivo para obtener depósitos en un nivel específico.
    private void depositosEnNivelRec(ABBTDA nodo, int nivelBuscado, int nivelActual, List<DepositoTDA> resultado) {
        if (!nodo.ArbolVacio()) {
            if (nivelActual == nivelBuscado) {
                resultado.add(almacen.Recuperar(nodo.Raiz()));
            } else {
                depositosEnNivelRec(nodo.HijoIzq(), nivelBuscado, nivelActual + 1, resultado);
                depositosEnNivelRec(nodo.HijoDer(), nivelBuscado, nivelActual + 1, resultado);
            }
        }
    }
}
