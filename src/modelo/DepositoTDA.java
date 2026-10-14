package modelo;

import java.time.LocalDateTime;

public interface DepositoTDA {

    // Precondición: debe llamarse antes de cualquier otra operación
    void inicializar(int id, String nombre, boolean visitado, LocalDateTime fechaUltimaAuditoria);

    // Precondición: el depósito debe estar inicializado
    int getId();

    // Precondición: el depósito debe estar inicializado
    String getNombre();

    // Precondición: el depósito debe estar inicializado
    boolean isVisitado();

    // Precondición: el depósito debe estar inicializado
    LocalDateTime getFechaUltimaAuditoria();

    // Precondición: el depósito debe estar inicializado
    void setVisitado(boolean visitado);

    // Precondición: el depósito debe estar inicializado
    void setFechaUltimaAuditoria(LocalDateTime fecha);
}
