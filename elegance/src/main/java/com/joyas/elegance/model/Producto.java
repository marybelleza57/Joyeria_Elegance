package com.joyas.elegance.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(name = "categoria_id", columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID categoriaId;

    private String nombre;

    @Column(name = "enlace_amigable")
    private String enlaceAmigable;

    private BigDecimal precio;
    private String material;

    @Column(name = "imagen_principal")
    private String imagenPrincipal;

    private Integer cantidad;
    private Boolean destacado;
    private String estado;

    @Column(name = "orientacion_estilo")
    private String orientacionEstilo;

    public Producto() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCategoriaId() { return categoriaId; }
    public void setCategoriaId(UUID categoriaId) { this.categoriaId = categoriaId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEnlaceAmigable() { return enlaceAmigable; }
    public void setEnlaceAmigable(String enlaceAmigable) { this.enlaceAmigable = enlaceAmigable; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getImagenPrincipal() { return imagenPrincipal; }
    public void setImagenPrincipal(String imagenPrincipal) { this.imagenPrincipal = imagenPrincipal; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Boolean getDestacado() { return destacado; }
    public void setDestacado(Boolean destacado) { this.destacado = destacado; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getOrientacionEstilo() { return orientacionEstilo; }
    public void setOrientacionEstilo(String orientacionEstilo) { this.orientacionEstilo = orientacionEstilo; }
}
