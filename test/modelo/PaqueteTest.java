package modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaqueteTest {

    private PaqueteTDA<TipoCarga> paquete;

    @BeforeEach
    void setUp() {
        paquete = new Paquete<>();
        paquete.inicializar(1, 30.5, "Buenos Aires", TipoCarga.ELECTRONICA, false);
    }

    @Test
    void getId_debeRetornarIdCorrecto() {
        assertEquals(1, paquete.getId());
    }

    @Test
    void getPeso_debeRetornarPesoCorrecto() {
        assertEquals(30.5, paquete.getPeso());
    }

    @Test
    void getDestino_debeRetornarDestinoCorrecto() {
        assertEquals("Buenos Aires", paquete.getDestino());
    }

    @Test
    void getContenido_debeRetornarTipoCorrecto() {
        assertEquals(TipoCarga.ELECTRONICA, paquete.getContenido());
    }

    @Test
    void isUrgente_paqueteNoUrgente_debeRetornarFalse() {
        assertFalse(paquete.isUrgente());
    }

    @Test
    void isUrgente_paqueteUrgente_debeRetornarTrue() {
        PaqueteTDA<TipoCarga> urgente = new Paquete<>();
        urgente.inicializar(2, 10.0, "Córdoba", TipoCarga.FRAGILES, true);
        assertTrue(urgente.isUrgente());
    }

    @Test
    void inicializar_distintosTiposDeCarga_debenFuncionar() {
        PaqueteTDA<TipoCarga> alimentos = new Paquete<>();
        alimentos.inicializar(3, 5.0, "Rosario", TipoCarga.ALIMENTOS, false);
        assertEquals(TipoCarga.ALIMENTOS, alimentos.getContenido());
    }
}
