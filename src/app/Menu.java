package app;

import modelo.DepositoTDA;
import modelo.Paquete;
import modelo.PaqueteTDA;
import modelo.TipoCarga;
import persistencia.GestorJSON;
import servicio.Camion;
import servicio.CentroDistribucion;
import servicio.RedDepositos;
import servicio.RutaDepositos;

import java.util.List;
import java.util.Scanner;

public class Menu {


    private static final String RUTA_INVENTARIO = "resources/inventario.json";
    private static final String RUTA_DEPOSITOS  = "resources/depositos.json";

    private final Scanner scanner;
    private final Camion camion;
    private final CentroDistribucion centro;
    private final RedDepositos redDepositos;
    private final RutaDepositos rutaDepositos;
    private final GestorJSON gestorJSON;
    private int contadorId;

    //Constructor
    public Menu() {
        this.scanner = new Scanner(System.in);
        this.camion = new Camion("ABC-123");
        this.centro = new CentroDistribucion();
        this.redDepositos = new RedDepositos();
        this.rutaDepositos = new RutaDepositos();
        this.gestorJSON = new GestorJSON();
        this.contadorId = 100;
    }

    //O(n) para cargar el inventario inicial desde archivos JSON.
    public void iniciar() {
        cargarInventarioInicial();
        cargarDepositosIniciales();

        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerOpcion();
            procesarMenuPrincipal(opcion);
        } while (opcion != 0);

        scanner.close();
        System.out.println("Sistema cerrado. Hasta pronto.");
    }

    //Carga el inventario inicial desde un archivo JSON. O(n).
    private void cargarInventarioInicial() {
        try {
            List<PaqueteTDA<TipoCarga>> paquetes = gestorJSON.cargarPaquetes(RUTA_INVENTARIO);
            int maxId = 0;
            for (PaqueteTDA<TipoCarga> paquete : paquetes) {
                camion.cargarPaquete(paquete);
                if (paquete.getId() > maxId) maxId = paquete.getId();
            }
            contadorId = Math.max(contadorId, maxId);
            System.out.println("Inventario cargado: " + paquetes.size() + " paquetes en el camion.");
        } catch (Exception e) {
            System.out.println("No se pudo cargar el inventario: " + e.getMessage());
        }
    }

    //Menu principal
    //O(1) para mostrar el menú y O(1) para procesar la opción seleccionada.
    private void mostrarMenuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("  1. Camion");
        System.out.println("  2. Centro de Distribucion");
        System.out.println("  3. Red de Depositos");
        System.out.println("  0. Salir");
        System.out.print("Opcion: ");
    }

    //Procesa la opción seleccionada en el menú principal. O(1).
    private void procesarMenuPrincipal(int opcion) {
        switch (opcion) {
            case 1 -> menuCamion();
            case 2 -> menuCentro();
            case 3 -> menuDepositos();
            case 0 -> {}
            default -> System.out.println("Opcion invalida. Intente nuevamente.");
        }
    }

    // ── Menú Camión ───────────────────────────────────────────────────────────

    //O(1) para mostrar el menú del camión y O(1) para procesar la opción seleccionada.
    private void menuCamion() {
        int opcion;
        do {
            mostrarMenuCamion();
            opcion = leerOpcion();
            procesarMenuCamion(opcion);
        } while (opcion != 0);
    }

    //Muestra el menú del camión con sus opciones. O(1).
    private void mostrarMenuCamion() {
        System.out.println("\n--- CAMIÓN [Patente: " + camion.getPatente() + "] ---");
        System.out.println("  1. Cargar paquete");
        System.out.println("  2. Deshacer última carga");
        System.out.println("  3. Descargar paquete en Centro de Distribución");
        System.out.println("  4. Ver próximo paquete");
        System.out.println("  0. Volver");
        System.out.print("Opción: ");
    }

    //Procesa la opción seleccionada en el menú del camión. O(1).
    private void procesarMenuCamion(int opcion) {
        switch (opcion) {
            case 1 -> cargarEnCamion();
            case 2 -> deshacerCarga();
            case 3 -> descargarAlCentro();
            case 4 -> verProximoCamion();
            case 0 -> {}
            default -> System.out.println("Opción inválida. Intente nuevamente.");
        }
    }

    // ── Menú Centro de Distribución ───────────────────────────────────────────
    //O(1) para mostrar el menú del centro de distribución y O(1) para procesar la opción seleccionada.
    private void menuCentro() {
        int opcion;
        do {
            mostrarMenuCentro();
            opcion = leerOpcion();
            procesarMenuCentro(opcion);
        } while (opcion != 0);
    }

    //Muestra el menú del centro de distribución con sus opciones. O(1).
    private void mostrarMenuCentro() {
        System.out.println("\n--- CENTRO DE DISTRIBUCIÓN ---");
        System.out.println("  1. Recibir paquete");
        System.out.println("  2. Procesar siguiente paquete");
        System.out.println("  3. Ver siguiente paquete a procesar");
        System.out.println("  4. Eliminar siguiente paquete");
        System.out.println("  0. Volver");
        System.out.print("Opción: ");
    }

    //Procesa la opción seleccionada en el menú del centro de distribución. O(1).
    private void procesarMenuCentro(int opcion) {
        switch (opcion) {
            case 1 -> recibirEnCentro();
            case 2 -> procesarDelCentro();
            case 3 -> verProximoCentro();
            case 4 -> eliminarDelCentro();
            case 0 -> {}
            default -> System.out.println("Opción inválida. Intente nuevamente.");
        }
    }

    //Lee la opción ingresada por el usuario y maneja errores de formato. O(1).
    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ── Camión ────────────────────────────────────────────────────────────────
    //Carga un nuevo paquete en el camión solicitando sus datos al usuario. O(1) para ingresar datos y O(1) para cargar el paquete.
    private void cargarEnCamion() {
        try {
            PaqueteTDA<TipoCarga> paquete = ingresarPaquete();
            camion.cargarPaquete(paquete);
            System.out.println("Paquete cargado en camión: " + paquete);
            persistir();
        } catch (NumberFormatException e) {
            System.out.println("Peso invalido. Ingrese un numero (ej: 12.5).");
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo de carga invalido. Tipos disponibles: ELECTRONICA, ALIMENTOS, FRAGILES, BAZAR, INDUMENTARIA, TEXTILES, MUEBLES, JUGUETES, MEDICAMENTOS, OTROS.");
        } catch (Exception e) {
            System.out.println("Error al cargar paquete: " + e.getMessage());
        }
    }

    //Deshace la última carga realizada en el camión. O(1).
    private void deshacerCarga() {
        PaqueteTDA<?> paquete = camion.deshacerUltimaCarga();
        if (paquete != null) {
            System.out.println("Carga deshecha: " + paquete);
            persistir();
        } else {
            System.out.println("El camión está vacío, no hay carga que deshacer.");
        }
    }

    //Descarga el siguiente paquete del camión al centro de distribución. O(1) para descargar y O(n) para recibir en el centro.
    private void descargarAlCentro() {
        PaqueteTDA<?> paquete = camion.descargarPaquete();
        if (paquete != null) {
            centro.recibirPaquete(paquete);
            System.out.println("Paquete descargado al centro: " + paquete);
            if (paquete.getPeso() > 50.0 || paquete.isUrgente()) {
                System.out.println("Aviso: este paquete tiene prioridad alta en el centro" +
                        (paquete.getPeso() > 50.0 ? " (supera los 50 kg)" : "") +
                        (paquete.isUrgente() ? " (urgente)" : "") + ".");
            }
            persistir();
        } else {
            System.out.println("El camión está vacío.");
        }
    }

    //Consulta el próximo paquete a descargar del camión sin removerlo. O(1).
    private void verProximoCamion() {
        PaqueteTDA<?> paquete = camion.verProximo();
        if (paquete != null) {
            System.out.println("Próximo a descargar: " + paquete);
        } else {
            System.out.println("El camión está vacío.");
        }
    }

    // ── Centro de Distribución ────────────────────────────────────────────────
    //Recibe un nuevo paquete en el centro de distribución solicitando sus datos al usuario. O(1) para ingresar datos y O(n) para recibir el paquete.
    private void recibirEnCentro() {
        try {
            PaqueteTDA<TipoCarga> paquete = ingresarPaquete();
            centro.recibirPaquete(paquete);
            System.out.println("Paquete recibido en el centro: " + paquete);
        } catch (NumberFormatException e) {
            System.out.println("Peso invalido. Ingrese un numero (ej: 12.5).");
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo de carga invalido. Tipos disponibles: ELECTRONICA, ALIMENTOS, FRAGILES, BAZAR, INDUMENTARIA, TEXTILES, MUEBLES, JUGUETES, MEDICAMENTOS, OTROS.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Procesa y retira el siguiente paquete de mayor prioridad del centro de distribución. O(1).
    private void procesarDelCentro() {
        PaqueteTDA<?> paquete = centro.procesarSiguiente();
        if (paquete != null) {
            System.out.println("Procesando: " + paquete);
            if (paquete.getPeso() > 50.0 || paquete.isUrgente()) {
                System.out.println("Aviso: este paquete tenia prioridad alta" +
                        (paquete.getPeso() > 50.0 ? " (supera los 50 kg)" : "") +
                        (paquete.isUrgente() ? " (urgente)" : "") + ".");
            }
        } else {
            System.out.println("No hay paquetes en el centro de distribucion.");
        }
    }

    //Elimina el siguiente paquete de mayor prioridad del centro de distribución sin procesarlo. O(1).
    private void eliminarDelCentro() {
        PaqueteTDA<?> paquete = centro.procesarSiguiente();
        if (paquete != null) {
            System.out.println("Paquete eliminado del centro: " + paquete);
        } else {
            System.out.println("No hay paquetes en el centro de distribución.");
        }
    }

    //Consulta el siguiente paquete a procesar en el centro de distribución sin removerlo. O(1).
    private void verProximoCentro() {
        PaqueteTDA<?> paquete = centro.verSiguiente();
        if (paquete != null) {
            System.out.println("Próximo a procesar: " + paquete);
        } else {
            System.out.println("No hay paquetes en el centro de distribución.");
        }
    }


    // Carga depósitos y rutas desde depositos.json al iniciar. O(n).
    private void cargarDepositosIniciales() {
        try {
            GestorJSON.CargaDepositos datos = gestorJSON.cargarDepositos(RUTA_DEPOSITOS);
            for (DepositoTDA deposito : datos.depositos) {
                redDepositos.agregarDeposito(deposito);
                rutaDepositos.agregarDeposito(deposito.getId());
            }
            for (int[] conexion : datos.conexiones) {
                rutaDepositos.agregarRuta(conexion[0], conexion[1], conexion[2]);
            }
            System.out.println("Red de depositos cargada: " + datos.depositos.size() + " depositos.");
        } catch (Exception e) {
            System.out.println("No se pudo cargar la red de depositos: " + e.getMessage());
        }
    }

    // ── Menú Red de Depósitos ─────────────────────────────────────────────────
    private void menuDepositos() {
        int opcion;
        do {
            mostrarMenuDepositos();
            opcion = leerOpcion();
            procesarMenuDepositos(opcion);
        } while (opcion != 0);
    }

    private void mostrarMenuDepositos() {
        System.out.println("\n--- RED DE DEPOSITOS ---");
        System.out.println("  1. Listar todos los depositos (ordenados por ID)");
        System.out.println("  2. Buscar deposito por ID");
        System.out.println("  3. Realizar auditoria (post-orden)");
        System.out.println("  4. Ver depositos en nivel N");
        System.out.println("  5. Calcular ruta minima entre dos depositos");
        System.out.println("  0. Volver");
        System.out.print("Opcion: ");
    }

    private void procesarMenuDepositos(int opcion) {
        switch (opcion) {
            case 1 -> listarDepositos();
            case 2 -> buscarDeposito();
            case 3 -> realizarAuditoria();
            case 4 -> verDepositosPorNivel();
            case 5 -> calcularRutaMinima();
            case 0 -> {}
            default -> System.out.println("Opcion invalida. Intente nuevamente.");
        }
    }

    private void listarDepositos() {
        List<DepositoTDA> depositos = redDepositos.listarDepositos();
        if (depositos.isEmpty()) {
            System.out.println("No hay depositos registrados.");
            return;
        }
        System.out.println("Depositos ordenados por ID:");
        for (DepositoTDA d : depositos) {
            System.out.println("  " + d);
        }
    }

    private void buscarDeposito() {
        System.out.print("ID del deposito: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            DepositoTDA deposito = redDepositos.buscarDeposito(id);
            if (deposito != null) {
                System.out.println("Encontrado: " + deposito);
            } else {
                System.out.println("No existe un deposito con ID " + id + ".");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID invalido.");
        }
    }

    private void realizarAuditoria() {
        if (redDepositos.estaVacia()) {
            System.out.println("No hay depositos registrados.");
            return;
        }
        redDepositos.realizarAuditoria();
        System.out.println("Auditoria completada. Se marcaron como visitados los depositos sin auditoria en los ultimos 30 dias.");
    }

    private void verDepositosPorNivel() {
        System.out.print("Nivel a consultar (0 = raiz): ");
        try {
            int nivel = Integer.parseInt(scanner.nextLine().trim());
            List<DepositoTDA> depositos = redDepositos.depositosEnNivel(nivel);
            if (depositos.isEmpty()) {
                System.out.println("No hay depositos en el nivel " + nivel + ".");
            } else {
                System.out.println("Depositos en nivel " + nivel + ":");
                for (DepositoTDA d : depositos) {
                    System.out.println("  " + d);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Nivel invalido.");
        }
    }

    private void calcularRutaMinima() {
        System.out.print("ID deposito origen: ");
        try {
            int origen = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("ID deposito destino: ");
            int destino = Integer.parseInt(scanner.nextLine().trim());
            int distancia = rutaDepositos.distanciaMinima(origen, destino);
            if (distancia == -1) {
                System.out.println("No existe ruta entre los depositos " + origen + " y " + destino + ".");
            } else {
                System.out.println("Distancia minima entre " + origen + " y " + destino + ": " + distancia + " km.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID invalido.");
        }
    }

    // Guarda el estado actual del camión en el JSON. O(n).
    private void persistir() {
        try {
            gestorJSON.guardarPaquetes(RUTA_INVENTARIO, camion.getPaquetes());
        } catch (Exception e) {
            System.out.println("Advertencia: no se pudo guardar el inventario: " + e.getMessage());
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    // Solicita los datos de un paquete al usuario y retorna la instancia creada. O(1).
    private PaqueteTDA<TipoCarga> ingresarPaquete() {
        System.out.print("Peso (kg): ");
        double peso = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Destino: ");
        String destino = scanner.nextLine().trim();

        System.out.println("Tipos disponibles: ELECTRONICA, ALIMENTOS, FRAGILES, BAZAR, INDUMENTARIA, TEXTILES, MUEBLES, JUGUETES, MEDICAMENTOS, OTROS");
        System.out.print("Tipo: ");
        TipoCarga tipo = TipoCarga.valueOf(scanner.nextLine().trim().toUpperCase());

        System.out.print("Urgente? (si / no): ");
        boolean urgente = scanner.nextLine().trim().equalsIgnoreCase("si");

        if (peso > 50.0) {
            System.out.println("Aviso: el paquete supera los 50 kg, tendra prioridad alta en el centro de distribucion.");
        }

        contadorId++;
        PaqueteTDA<TipoCarga> paquete = new Paquete<>();
        paquete.inicializar(contadorId, peso, destino, tipo, urgente);
        return paquete;
    }
}
