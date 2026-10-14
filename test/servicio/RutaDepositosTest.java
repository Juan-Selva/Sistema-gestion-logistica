package servicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RutaDepositosTest {

    private RutaDepositos rutas;

    @BeforeEach
    void setUp() {
        rutas = new RutaDepositos();
        // Red de ejemplo:
        //   1 --100km-- 2 --200km-- 3
        //   |                       |
        //  150km                  250km
        //   |                       |
        //   4 --------300km-------- 5
        rutas.agregarDeposito(1);
        rutas.agregarDeposito(2);
        rutas.agregarDeposito(3);
        rutas.agregarDeposito(4);
        rutas.agregarDeposito(5);

        rutas.agregarRuta(1, 2, 100);
        rutas.agregarRuta(2, 3, 200);
        rutas.agregarRuta(1, 4, 150);
        rutas.agregarRuta(3, 5, 250);
        rutas.agregarRuta(4, 5, 300);
    }

    // --- existeRuta ---

    @Test
    void existeRuta_rutaDirecta_retornaTrue() {
        assertTrue(rutas.existeRuta(1, 2));
    }

    @Test
    void existeRuta_rutaBidireccional_retornaTrue() {
        assertTrue(rutas.existeRuta(2, 1));
    }

    @Test
    void existeRuta_sinConexionDirecta_retornaFalse() {
        assertFalse(rutas.existeRuta(1, 3));
    }

    @Test
    void existeRuta_mismoVertice_retornaFalse() {
        assertFalse(rutas.existeRuta(1, 1));
    }

    // --- distanciaMinima ---

    @Test
    void distanciaMinima_rutaDirecta_retornaDistanciaCorrecta() {
        assertEquals(100, rutas.distanciaMinima(1, 2));
    }

    @Test
    void distanciaMinima_caminoIndirecto_retornaMenorDistancia() {
        // 1->2->3 = 300 vs no existe directo
        assertEquals(300, rutas.distanciaMinima(1, 3));
    }

    @Test
    void distanciaMinima_dijkstraEligeMejorCamino() {
        // Hacia 5: camino 1->2->3->5 = 550 | camino 1->4->5 = 450
        assertEquals(450, rutas.distanciaMinima(1, 5));
    }

    @Test
    void distanciaMinima_bidireccional_mismaDistancia() {
        assertEquals(rutas.distanciaMinima(1, 5), rutas.distanciaMinima(5, 1));
    }

    @Test
    void distanciaMinima_mismoDeposito_retornaCero() {
        assertEquals(0, rutas.distanciaMinima(1, 1));
    }

    @Test
    void distanciaMinima_sinCamino_retornaMenosUno() {
        rutas.agregarDeposito(99); // vértice aislado
        assertEquals(-1, rutas.distanciaMinima(1, 99));
    }

    // --- agregarRuta ---

    @Test
    void agregarRuta_nuevaConexion_existeEnAmbasDirecciones() {
        rutas.agregarDeposito(6);
        rutas.agregarRuta(3, 6, 50);
        assertTrue(rutas.existeRuta(3, 6));
        assertTrue(rutas.existeRuta(6, 3));
    }

    @Test
    void agregarRuta_nuevaConexion_afectaDistanciaMinima() {
        // Atajo directo 1->3 = 50km (mejor que 1->2->3 = 300)
        rutas.agregarRuta(1, 3, 50);
        assertEquals(50, rutas.distanciaMinima(1, 3));
    }
}
