package persistencia;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import modelo.Deposito;
import modelo.DepositoTDA;
import modelo.Paquete;
import modelo.PaqueteTDA;
import modelo.TipoCarga;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsable de toda la I/O con archivos JSON del sistema.
 * Maneja la carga y persistencia de paquetes (inventario.json)
 * y la carga de depósitos con sus conexiones (depositos.json).
 */

public class GestorJSON {

    // Carga los paquetes desde un archivo JSON. O(n) por el parseo de la lista.
    public List<PaqueteTDA<TipoCarga>> cargarPaquetes(String rutaArchivo) throws IOException {
        List<PaqueteTDA<TipoCarga>> resultado = new ArrayList<>();

        try (java.io.Reader reader = abrirReader(rutaArchivo)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray paquetesArray = root.getAsJsonArray("paquetes");

            for (JsonElement elemento : paquetesArray) {
                JsonObject obj = elemento.getAsJsonObject();

                int id = obj.get("id").getAsInt();
                double peso = obj.get("peso").getAsDouble();
                String destino = obj.get("destino").getAsString();
                TipoCarga tipo = TipoCarga.valueOf(obj.get("tipo").getAsString().toUpperCase());
                boolean urgente = obj.get("urgente").getAsBoolean();

                PaqueteTDA<TipoCarga> paquete = new Paquete<>();
                paquete.inicializar(id, peso, destino, tipo, urgente);
                resultado.add(paquete);
            }
        }

        return resultado;
    }

    /**
     * Resultado de la carga de depósitos: lista de depósitos y lista de conexiones.
     * Cada conexión es un arreglo [idOrigen, idDestino, distanciaKm].
     */
    public static class CargaDepositos {
        public final List<DepositoTDA> depositos;
        public final List<int[]> conexiones;

        public CargaDepositos(List<DepositoTDA> depositos, List<int[]> conexiones) {
            this.depositos = depositos;
            this.conexiones = conexiones;
        }
    }

    // Carga depósitos y sus conexiones desde depositos.json. O(n).
    public CargaDepositos cargarDepositos(String rutaArchivo) throws IOException {
        List<DepositoTDA> depositos = new ArrayList<>();
        List<int[]> conexiones = new ArrayList<>();

        try (java.io.Reader reader = abrirReader(rutaArchivo)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray depositosArray = root.getAsJsonArray("depositos");

            for (JsonElement elemento : depositosArray) {
                JsonObject obj = elemento.getAsJsonObject();

                int id = obj.get("id").getAsInt();
                String nombre = obj.get("nombre").getAsString();
                boolean visitado = obj.get("visitado").getAsBoolean();
                LocalDateTime fecha = obj.get("fechaUltimaAuditoria") != null && !obj.get("fechaUltimaAuditoria").isJsonNull()
                        ? LocalDateTime.parse(obj.get("fechaUltimaAuditoria").getAsString())
                        : null;

                DepositoTDA deposito = new Deposito();
                deposito.inicializar(id, nombre, visitado, fecha);
                depositos.add(deposito);

                if (obj.has("conexiones")) {
                    for (JsonElement conn : obj.getAsJsonArray("conexiones")) {
                        JsonObject c = conn.getAsJsonObject();
                        conexiones.add(new int[]{id, c.get("destino").getAsInt(), c.get("distancia").getAsInt()});
                    }
                }
            }
        }

        return new CargaDepositos(depositos, conexiones);
    }

    // Guarda la lista de paquetes en el archivo JSON. O(n).
    public void guardarPaquetes(String rutaArchivo, List<PaqueteTDA<?>> paquetes) throws IOException {
        File archivo = resolverArchivo(rutaArchivo);

        JsonObject root = new JsonObject();
        JsonArray array = new JsonArray();
        for (PaqueteTDA<?> p : paquetes) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id", p.getId());
            obj.addProperty("peso", p.getPeso());
            obj.addProperty("destino", p.getDestino());
            obj.addProperty("tipo", p.getContenido().toString());
            obj.addProperty("urgente", p.isUrgente());
            array.add(obj);
        }
        root.add("paquetes", array);

        try (FileWriter writer = new FileWriter(archivo, StandardCharsets.UTF_8)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(root, writer);
        }
    }

    private java.io.Reader abrirReader(String rutaArchivo) throws IOException {
        String nombreArchivo = new File(rutaArchivo).getName();

        // 1. Classpath (cuando IntelliJ copia resources/ a bin/)
        InputStream is = getClass().getClassLoader().getResourceAsStream(nombreArchivo);
        if (is != null) return new InputStreamReader(is, StandardCharsets.UTF_8);

        // 2. Relativo al directorio donde están los .class compilados (bin/../resources)
        File candidato = resolverArchivo(rutaArchivo);
        if (candidato.exists()) return new FileReader(candidato, StandardCharsets.UTF_8);

        // 3. Fallback: ruta relativa al directorio de trabajo
        return new FileReader(rutaArchivo, StandardCharsets.UTF_8);
    }

    private File resolverArchivo(String rutaArchivo) throws IOException {
        String nombreArchivo = new File(rutaArchivo).getName();
        try {
            File binDir = new File(GestorJSON.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            File candidato = new File(binDir, "../resources/" + nombreArchivo).getCanonicalFile();
            if (candidato.exists()) return candidato;
        } catch (Exception ignored) {}
        return new File(rutaArchivo);
    }
}
