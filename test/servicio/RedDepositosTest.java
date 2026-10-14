package servicio;

import modelo.Deposito;
import modelo.DepositoTDA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RedDepositosTest {

    private RedDepositos red;

    private DepositoTDA deposito(int id, String nombre, LocalDateTime fecha) {
        Deposito d = new Deposito();
        d.inicializar(id, nombre, false, fecha);
        return d;
    }

    @BeforeEach
    void setUp() {
        red = new RedDepositos();
    }

    // --- Estado inicial ---

    @Test
    void redNueva_debeEstarVacia() {
        assertTrue(red.estaVacia());
    }

    // --- agregarDeposito / buscarDeposito ---

    @Test
    void agregarDeposito_redDejaDeEstarVacia() {
        red.agregarDeposito(deposito(10, "Rosario", null));
        assertFalse(red.estaVacia());
    }

    @Test
    void buscarDeposito_depositoExistente_retornaDeposito() {
        DepositoTDA d = deposito(10, "Rosario", null);
        red.agregarDeposito(d);
        DepositoTDA encontrado = red.buscarDeposito(10);
        assertNotNull(encontrado);
        assertEquals(10, encontrado.getId());
        assertEquals("Rosario", encontrado.getNombre());
    }

    @Test
    void buscarDeposito_idInexistente_retornaNull() {
        red.agregarDeposito(deposito(10, "Rosario", null));
        assertNull(red.buscarDeposito(99));
    }

    // --- eliminarDeposito ---

    @Test
    void eliminarDeposito_depositoEliminado_noPuedeEncontrarse() {
        red.agregarDeposito(deposito(10, "Rosario", null));
        red.eliminarDeposito(10);
        assertNull(red.buscarDeposito(10));
    }

    @Test
    void eliminarUnicoDeposito_redQuedaVacia() {
        red.agregarDeposito(deposito(10, "Rosario", null));
        red.eliminarDeposito(10);
        assertTrue(red.estaVacia());
    }

    // --- listarDepositos (in-orden = orden ascendente por ID) ---

    @Test
    void listarDepositos_retornaOrdenAscendentePorId() {
        red.agregarDeposito(deposito(50, "Hub Central", null));
        red.agregarDeposito(deposito(20, "Córdoba", null));
        red.agregarDeposito(deposito(80, "Mendoza", null));

        List<DepositoTDA> lista = red.listarDepositos();

        assertEquals(3, lista.size());
        assertEquals(20, lista.get(0).getId());
        assertEquals(50, lista.get(1).getId());
        assertEquals(80, lista.get(2).getId());
    }

    // --- realizarAuditoria ---

    @Test
    void realizarAuditoria_depositoSinAuditoria_marcaVisitado() {
        red.agregarDeposito(deposito(10, "Rosario", null));
        red.realizarAuditoria();
        assertTrue(red.buscarDeposito(10).isVisitado());
    }

    @Test
    void realizarAuditoria_depositoAuditadoHaceMasDe30Dias_marcaVisitado() {
        LocalDateTime fechaVieja = LocalDateTime.now().minusDays(31);
        red.agregarDeposito(deposito(10, "Rosario", fechaVieja));
        red.realizarAuditoria();
        assertTrue(red.buscarDeposito(10).isVisitado());
    }

    @Test
    void realizarAuditoria_depositoAuditadoHacemenos30Dias_noMarcaVisitado() {
        LocalDateTime fechaReciente = LocalDateTime.now().minusDays(10);
        red.agregarDeposito(deposito(10, "Rosario", fechaReciente));
        red.realizarAuditoria();
        assertFalse(red.buscarDeposito(10).isVisitado());
    }

    @Test
    void realizarAuditoria_soloMarcaDepositosViejos() {
        LocalDateTime reciente = LocalDateTime.now().minusDays(5);
        LocalDateTime vieja = LocalDateTime.now().minusDays(60);

        red.agregarDeposito(deposito(50, "Hub Central", reciente));
        red.agregarDeposito(deposito(20, "Córdoba", vieja));
        red.agregarDeposito(deposito(80, "Mendoza", null));

        red.realizarAuditoria();

        assertFalse(red.buscarDeposito(50).isVisitado());
        assertTrue(red.buscarDeposito(20).isVisitado());
        assertTrue(red.buscarDeposito(80).isVisitado());
    }

    // --- depositosEnNivel ---

    @Test
    void depositosEnNivel_nivel0_retornaRaiz() {
        red.agregarDeposito(deposito(50, "Hub Central", null));
        red.agregarDeposito(deposito(20, "Córdoba", null));
        red.agregarDeposito(deposito(80, "Mendoza", null));

        List<DepositoTDA> nivel0 = red.depositosEnNivel(0);

        assertEquals(1, nivel0.size());
        assertEquals(50, nivel0.get(0).getId());
    }

    @Test
    void depositosEnNivel_nivel1_retornaHijosDeRaiz() {
        red.agregarDeposito(deposito(50, "Hub Central", null));
        red.agregarDeposito(deposito(20, "Córdoba", null));
        red.agregarDeposito(deposito(80, "Mendoza", null));

        List<DepositoTDA> nivel1 = red.depositosEnNivel(1);

        assertEquals(2, nivel1.size());
        assertTrue(nivel1.stream().anyMatch(d -> d.getId() == 20));
        assertTrue(nivel1.stream().anyMatch(d -> d.getId() == 80));
    }

    @Test
    void depositosEnNivel_nivelInexistente_retornaListaVacia() {
        red.agregarDeposito(deposito(50, "Hub Central", null));

        List<DepositoTDA> nivel5 = red.depositosEnNivel(5);

        assertTrue(nivel5.isEmpty());
    }

    @Test
    void depositosEnNivel_nivel2_retornaNietos() {
        // Árbol: 50 -> izq: 20 -> der: 30 | 50 -> der: 80
        red.agregarDeposito(deposito(50, "Hub Central", null));
        red.agregarDeposito(deposito(20, "Córdoba", null));
        red.agregarDeposito(deposito(80, "Mendoza", null));
        red.agregarDeposito(deposito(30, "Rosario", null));

        List<DepositoTDA> nivel2 = red.depositosEnNivel(2);

        assertEquals(1, nivel2.size());
        assertEquals(30, nivel2.get(0).getId());
    }
}
