package servicio;

import tda.conjunto.ConjuntoDinamico;
import tda.conjunto.ConjuntoTDA;
import tda.grafo.GrafoDinamico;
import tda.grafo.GrafoTDA;

import java.util.HashMap;
import java.util.Map;


/**
 * Gestiona la red de rutas entre depósitos usando un grafo dirigido con pesos.
 * Las conexiones se agregan en ambas direcciones para modelar rutas bidireccionales.
 * Implementa Dijkstra para encontrar la distancia mínima entre dos depósitos.
 *
 * Complejidad:
 *   agregarDeposito  → O(1)
 *   agregarRuta      → O(1) amortizado (GrafoDinamico)
 *   existeRuta       → O(n) en lista de adyacencia
 *   distanciaMinima  → O(V^2) con búsqueda lineal del mínimo
 */
public class RutaDepositos {

    private final GrafoTDA grafo;

    public RutaDepositos() {
        this.grafo = new GrafoDinamico();
        this.grafo.InicializarGrafo();
    }

    // Agrega un depósito como vértice del grafo. O(1).
    public void agregarDeposito(int id) {
        grafo.AgregarVertice(id);
    }

    // Agrega una ruta bidireccional entre dos depósitos con distancia en km. O(1).
    public void agregarRuta(int idOrigen, int idDestino, int distanciaKm) {
        grafo.AgregarArista(idOrigen, idDestino, distanciaKm);
        grafo.AgregarArista(idDestino, idOrigen, distanciaKm);
    }

    // Verifica si existe una ruta directa entre dos depósitos. O(n).
    public boolean existeRuta(int idOrigen, int idDestino) {
        return grafo.ExisteArista(idOrigen, idDestino);
    }

    /**
     * Calcula la distancia mínima en km entre dos depósitos usando Dijkstra.
     * Retorna -1 si no existe camino. O(V^2).
     */
    public int distanciaMinima(int origen, int destino) {
        Map<Integer, Integer> dist = new HashMap<>();
        ConjuntoTDA visitados = new ConjuntoDinamico();
        visitados.InicializarConjunto();

        ConjuntoTDA vertices = grafo.Vertices();
        while (!vertices.ConjuntoVacio()) {
            int v = vertices.Elegir();
            dist.put(v, Integer.MAX_VALUE);
            vertices.Sacar(v);
        }
        dist.put(origen, 0);

        int totalVertices = dist.size();
        for (int i = 0; i < totalVertices; i++) {
            int u = verticeConMinDistancia(dist, visitados);
            if (u == -1) break;
            if (u == destino) break;
            visitados.Agregar(u);

            ConjuntoTDA vecinos = grafo.Vertices();
            while (!vecinos.ConjuntoVacio()) {
                int v = vecinos.Elegir();
                vecinos.Sacar(v);
                if (!visitados.Pertenece(v) && grafo.ExisteArista(u, v)) {
                    int nuevaDist = dist.get(u) + grafo.PesoArista(u, v);
                    if (nuevaDist < dist.get(v)) {
                        dist.put(v, nuevaDist);
                    }
                }
            }
        }

        int resultado = dist.getOrDefault(destino, Integer.MAX_VALUE);
        return (resultado == Integer.MAX_VALUE) ? -1 : resultado;
    }

    // Retorna el vértice no visitado con menor distancia acumulada. O(V).
    private int verticeConMinDistancia(Map<Integer, Integer> dist, ConjuntoTDA visitados) {
        int minVertice = -1;
        int minDist = Integer.MAX_VALUE;
        for (Map.Entry<Integer, Integer> entry : dist.entrySet()) {
            if (!visitados.Pertenece(entry.getKey()) && entry.getValue() < minDist) {
                minDist = entry.getValue();
                minVertice = entry.getKey();
            }
        }
        return minVertice;
    }
}
