package com.joyas.elegance.repository;

import com.joyas.elegance.model.Pedido;
import com.joyas.elegance.model.Clientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, UUID> {
    List<Pedido> findByClienteOrderByFechaCreacionDesc(Clientes cliente);
    long count();
}
