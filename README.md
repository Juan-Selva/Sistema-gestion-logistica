# Sistema de Gestión Logística

## Descripción

Aplicación de consola que simula el flujo logístico de paquetes entre un camión, un centro de distribución y una red de depósitos regionales. Todas las estructuras de datos (pila, cola de prioridad, árbol binario de búsqueda, grafo, conjunto y diccionario) están implementadas desde cero sin usar `java.util.Collections`.

## Módulos

| Módulo | Estructura usada | Funcionalidad |
|---|---|---|
| **Camión** | Pila (LIFO) | Cargar paquetes y deshacer la última carga |
| **Centro de Distribución** | Cola de Prioridad + Diccionario | Procesar paquetes por prioridad (urgentes o > 50 kg primero) |
| **Red de Depósitos** | ABB + Grafo + Diccionario | Gestionar depósitos, auditoría post-orden y ruta mínima entre depósitos |

## TDAs implementados

- `PilaTDA` / `PilaDinamica`
- `ColaPrioridadTDA` / `ColaPrioridadDinamica`
- `ABBTDA` / `ABB` (árbol binario de búsqueda)
- `GrafoTDA` / `GrafoDinamico`
- `ConjuntoTDA` / `ConjuntoDinamico`
- `DiccionarioSimpleGenericoTDA` / `DiccionarioSimpleGenericoDinamico`

## Funcionalidades principales

- Cargar/descargar paquetes en el camión con soporte de deshacer
- Recibir y procesar paquetes en el centro según prioridad calculada automáticamente
- Listar depósitos ordenados por ID (recorrido in-orden)
- Buscar depósito por ID
- Auditoría post-orden: detecta depósitos sin auditar en los últimos 30 días
- Ver depósitos por nivel del árbol
- Calcular la ruta mínima entre dos depósitos (grafo ponderado)
- Persistencia automática del inventario en JSON (`resources/inventario.json`)
- Carga inicial de depósitos y conexiones desde `resources/depositos.json`

## Estructura del proyecto

```
src/
├── app/          # Menú interactivo de consola
├── modelo/       # Paquete, Depósito, TipoCarga
├── servicio/     # Camión, CentroDistribucion, RedDepositos, RutaDepositos
├── persistencia/ # GestorJSON (lectura/escritura con Gson)
└── tda/          # Implementaciones de todas las estructuras
resources/
├── inventario.json
└── depositos.json
test/             # Tests JUnit por módulo
```

## Requisitos

- Java 17+
- Gson 2.10.1 (incluido en `lib/`)
- JUnit Platform Console 1.10.2 (incluido en `lib/`)

## Ejecución

Compilar y ejecutar `App.java` desde el directorio raíz. El sistema carga automáticamente el inventario y la red de depósitos al iniciar.
