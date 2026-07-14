package com.joyas.elegance.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "notas_credito")
public class NotaCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(name = "monto_inicial", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoInicial;

    @Column(name = "monto_disponible", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoDisponible;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false, length = 20)
    private String estado = "ACTIVO"; // ACTIVO, USADO_PARCIAL, USADO_TOTAL, EXPIRADO

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Clientes cliente;

    @ManyToOne
    @JoinColumn(name = "pedido_origen_id")
    private Pedido pedidoOrigen;

    public NotaCredito() {
        this.fechaEmision = LocalDate.now();
        this.estado = "ACTIVO";
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public BigDecimal getMontoInicial() { return montoInicial; }
    public void setMontoInicial(BigDecimal montoInicial) { this.montoInicial = montoInicial; }

    public BigDecimal getMontoDisponible() { return montoDisponible; }
    public void setMontoDisponible(BigDecimal montoDisponible) { this.montoDisponible = montoDisponible; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Clientes getCliente() { return cliente; }
    public void setCliente(Clientes cliente) { this.cliente = cliente; }

    public Pedido getPedidoOrigen() { return pedidoOrigen; }
    public void setPedidoOrigen(Pedido pedidoOrigen) { this.pedidoOrigen = pedidoOrigen; }
}
