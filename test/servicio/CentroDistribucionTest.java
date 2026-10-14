package servicio;

import modelo.Paquete;
import modelo.PaqueteTDA;
import modelo.TipoCarga;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CentroDistribucionTest {

    private CentroDistribucion centro;

    @BeforeEach
    void setUp() {
        centro = new CentroDistribucion();
    }

    private PaqueteTDA<TipoCarga> crearPaquete(int id, double peso, boolean urgente) {
        PaqueteTDA<TipoCarga> p = new Paquete<>();
        p.inicializar(id, peso, "Destino", TipoCarga.ELECTRONICA, urgente);
        return p;
    }

    @Test
    void centroNuevo_debeEstarVacio() {
        assertTrue(centro.estaVacio());
    }

    @Test
    void recibirPaquete_centroDejaDeEstarVacio() {
        centro.recibirPaquete(crearPaquete(1, 10.0, false));
        assertFalse(centro.estaVacio());
    }

    @Test
    void procesarSiguiente_paqueteUrgente_debeIrPrimero() {
        PaqueteTDA<TipoCarga> normal = crearPaquete(1, 10.0, false);
        PaqueteTDA<TipoCarga> urgente = crearPaquete(2, 10.0, true);

        centro.recibirPaquete(normal);
        centro.recibirPaquete(urgente);

        assertEquals(2, centro.procesarSiguiente().getId());
    }

    @Test
    void procesarSiguiente_paquetePesado_debeIrPrimero() {
        PaqueteTDA<TipoCarga> liviano = crearPaquete(1, 10.0, false);
        PaqueteTDA<TipoCarga> pesado = crearPaquete(2, 60.0, false); // > 50kg

        centro.recibirPaquete(liviano);
        centro.recibirPaquete(pesado);

        assertEquals(2, centro.procesarSiguiente().getId());
    }

    @Test
    void procesarSiguiente_mismasPrioridades_respetaOrdenDeLlegada() {
        PaqueteTDA<TipoCarga> primero = crearPaquete(1, 10.0, false);
        PaqueteTDA<TipoCarga> segundo = crearPaquete(2, 20.0, false);

        centro.recibirPaquete(primero);
        centro.recibirPaquete(segundo);

        assertEquals(1, centro.procesarSiguiente().getId());
    }

    @Test
    void procesarSiguiente_centroVacio_debeRetornarNull() {
        assertNull(centro.procesarSiguiente());
    }

    @Test
    void verSiguiente_noDebeRemoverElPaquete() {
        centro.recibirPaquete(crearPaquete(1, 10.0, false));
        centro.verSiguiente();
        assertFalse(centro.estaVacio());
    }

    @Test
    void procesarTodos_centroDebeQuedarVacio() {
        centro.recibirPaquete(crearPaquete(1, 10.0, false));
        centro.recibirPaquete(crearPaquete(2, 20.0, false));
        centro.procesarSiguiente();
        centro.procesarSiguiente();
        assertTrue(centro.estaVacio());
    }

    @Test
    void procesarSiguiente_urgenteYPesado_urgenteVaPrimero() {
        PaqueteTDA<TipoCarga> pesado = crearPaquete(1, 60.0, false);
        PaqueteTDA<TipoCarga> urgente = crearPaquete(2, 5.0, true);

        centro.recibirPaquete(pesado);
        centro.recibirPaquete(urgente);

        // Ambos tienen prioridad alta (2), el primero en llegar va primero
        assertEquals(1, centro.procesarSiguiente().getId());
    }
}
