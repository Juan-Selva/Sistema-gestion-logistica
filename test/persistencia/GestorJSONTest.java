package persistencia;

import modelo.PaqueteTDA;
import modelo.TipoCarga;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestorJSONTest {

    private final GestorJSON gestorJSON = new GestorJSON();

    private String rutaInventario() {
        return "resources/inventario.json";
    }

    @Test
    void cargarPaquetes_archivoValido_debeRetornarListaNoVacia() throws IOException {
        List<PaqueteTDA<TipoCarga>> paquetes = gestorJSON.cargarPaquetes(rutaInventario());
        assertFalse(paquetes.isEmpty());
    }

    @Test
    void cargarPaquetes_debeCargarCantidadCorrecta() throws IOException {
        List<PaqueteTDA<TipoCarga>> paquetes = gestorJSON.cargarPaquetes(rutaInventario());
        assertEquals(12, paquetes.size());
    }

    @Test
    void cargarPaquetes_primerPaquete_debeTenerDatosCorrectos() throws IOException {
        List<PaqueteTDA<TipoCarga>> paquetes = gestorJSON.cargarPaquetes(rutaInventario());
        PaqueteTDA<TipoCarga> primero = paquetes.get(0);

        assertEquals(1, primero.getId());
        assertEquals(30.5, primero.getPeso());
        assertEquals("Buenos Aires", primero.getDestino());
        assertEquals(TipoCarga.ELECTRONICA, primero.getContenido());
        assertFalse(primero.isUrgente());
    }

    @Test
    void cargarPaquetes_paqueteUrgente_debeCargarUrgenteCorrectamente() throws IOException {
        List<PaqueteTDA<TipoCarga>> paquetes = gestorJSON.cargarPaquetes(rutaInventario());
        // El paquete id=2 es urgente en el inventario.json
        PaqueteTDA<TipoCarga> urgente = paquetes.stream()
                .filter(p -> p.getId() == 2)
                .findFirst()
                .orElse(null);

        assertNotNull(urgente);
        assertTrue(urgente.isUrgente());
    }

    @Test
    void cargarPaquetes_archivoInexistente_debeLanzarIOException() {
        assertThrows(IOException.class, () ->
                gestorJSON.cargarPaquetes("archivo_inexistente.json")
        );
    }
}
