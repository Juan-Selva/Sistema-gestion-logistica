package servicio;

import modelo.Paquete;
import modelo.PaqueteTDA;
import modelo.TipoCarga;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CamionTest {

    private Camion camion;
    private PaqueteTDA<TipoCarga> paquete1;
    private PaqueteTDA<TipoCarga> paquete2;
    private PaqueteTDA<TipoCarga> paquete3;

    @BeforeEach
    void setUp() {
        camion = new Camion("ABC-123");

        paquete1 = new Paquete<>();
        paquete1.inicializar(1, 10.0, "Buenos Aires", TipoCarga.ELECTRONICA, false);

        paquete2 = new Paquete<>();
        paquete2.inicializar(2, 20.0, "Córdoba", TipoCarga.ALIMENTOS, false);

        paquete3 = new Paquete<>();
        paquete3.inicializar(3, 30.0, "Rosario", TipoCarga.FRAGILES, false);
    }

    @Test
    void camionNuevo_debeEstarVacio() {
        assertTrue(camion.estaVacio());
    }

    @Test
    void cargarPaquete_camionDejaDeEstarVacio() {
        camion.cargarPaquete(paquete1);
        assertFalse(camion.estaVacio());
    }

    @Test
    void descargarPaquete_comportamientoLIFO() {
        camion.cargarPaquete(paquete1);
        camion.cargarPaquete(paquete2);
        camion.cargarPaquete(paquete3);

        // El último en entrar debe ser el primero en salir
        assertEquals(3, camion.descargarPaquete().getId());
        assertEquals(2, camion.descargarPaquete().getId());
        assertEquals(1, camion.descargarPaquete().getId());
    }

    @Test
    void deshacerUltimaCarga_debeRemoverElUltimoPaquete() {
        camion.cargarPaquete(paquete1);
        camion.cargarPaquete(paquete2);

        PaqueteTDA<?> desecho = camion.deshacerUltimaCarga();

        assertEquals(2, desecho.getId());
        assertEquals(1, camion.verProximo().getId());
    }

    @Test
    void deshacerUltimaCarga_camionVacio_debeRetornarNull() {
        assertNull(camion.deshacerUltimaCarga());
    }

    @Test
    void descargarPaquete_camionVacio_debeRetornarNull() {
        assertNull(camion.descargarPaquete());
    }

    @Test
    void verProximo_noDebeRemoverElPaquete() {
        camion.cargarPaquete(paquete1);
        camion.verProximo();
        assertFalse(camion.estaVacio());
    }

    @Test
    void descargarTodos_camionDebeQuedarVacio() {
        camion.cargarPaquete(paquete1);
        camion.cargarPaquete(paquete2);
        camion.descargarPaquete();
        camion.descargarPaquete();
        assertTrue(camion.estaVacio());
    }
}
