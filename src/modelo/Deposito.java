package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deposito implements DepositoTDA {

    private int id;
    private String nombre;
    private boolean visitado;
    private LocalDateTime fechaUltimaAuditoria;

    @Override
    public void inicializar(int id, String nombre, boolean visitado, LocalDateTime fechaUltimaAuditoria) {
        this.id = id;
        this.nombre = nombre;
        this.visitado = visitado;
        this.fechaUltimaAuditoria = fechaUltimaAuditoria;
    }

    @Override
    public int getId() { return id; }

    @Override
    public String getNombre() { return nombre; }

    @Override
    public boolean isVisitado() { return visitado; }

    @Override
    public LocalDateTime getFechaUltimaAuditoria() { return fechaUltimaAuditoria; }

    @Override
    public void setVisitado(boolean visitado) { this.visitado = visitado; }

    @Override
    public void setFechaUltimaAuditoria(LocalDateTime fecha) { this.fechaUltimaAuditoria = fecha; }

    @Override
    public String toString() {
        String fechaStr = (fechaUltimaAuditoria != null)
                ? fechaUltimaAuditoria.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : "Sin auditoria";
        return String.format("Deposito[id=%d | nombre=%s | visitado=%s | ultimaAuditoria=%s]",
                id, nombre, visitado ? "SI" : "NO", fechaStr);
    }
}
