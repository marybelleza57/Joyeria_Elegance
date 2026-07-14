package com.joyas.elegance.repository;

import com.joyas.elegance.model.DetallePedido;
import com.joyas.elegance.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface DetallePedidoRepositorio extends JpaRepository<DetallePedido, UUID> {
    List<DetallePedido> findByPedido(Pedido pedido);
}
