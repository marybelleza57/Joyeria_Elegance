package com.joyas.elegance.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Clientes cliente;

    @Column(name = "numero_pedido", unique = true, nullable = false)
    private String numeroPedido;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('PENDIENTE','CONFIRMADO','EN_TRANSITO','ENTREGADO','CANCELADO','DEV_SOLICITADA','DEV_APROBADA','DEV_RECHAZADA')")
    private EstadoPedido estado;

    @Column(name = "direccion_envio", nullable = false, columnDefinition = "TEXT")
    private String direccionEnvio;

    @Column(name = "telefono_contacto", length = 20)
    private String telefonoContacto;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(name = "motivo_cancelacion", length = 100)
    private String motivoCancelacion;

    @Column(name = "fecha_entrega")
    private java.time.LocalDate fechaEntrega;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public Pedido() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Clientes getCliente() { return cliente; }
    public void setCliente(Clientes cliente) { this.cliente = cliente; }

    public String getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(String numeroPedido) { this.numeroPedido = numeroPedido; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public java.time.LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(java.time.LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getMotivoCancelacion() { return motivoCancelacion; }
    public void setMotivoCancelacion(String motivoCancelacion) { this.motivoCancelacion = motivoCancelacion; }
}
