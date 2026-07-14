package com.joyas.elegance.repository;

import com.joyas.elegance.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface ProductoRepositorio extends JpaRepository<Producto, UUID> {
    List<Producto> findByDestacadoTrue();

    long countByCategoriaId(UUID categoriaId);

    @Query(value = "SELECT COUNT(*) FROM productos p INNER JOIN detalle_pedidos dp ON dp.producto_id = p.id WHERE p.categoria_id = :categoriaId", nativeQuery = true)
    long countByCategoriaIdInPedidos(@Param("categoriaId") String categoriaId);

    @Query(value = "SELECT p.* FROM productos p INNER JOIN detalle_pedidos dp ON p.id = dp.producto_id GROUP BY p.id ORDER BY SUM(dp.cantidad) DESC LIMIT :limite", nativeQuery = true)
    List<Producto> findTopProductosMasVendidos(@Param("limite") int limite);
}