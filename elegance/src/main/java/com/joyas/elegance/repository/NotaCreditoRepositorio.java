package com.joyas.elegance.repository;

import com.joyas.elegance.model.Clientes;
import com.joyas.elegance.model.NotaCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotaCreditoRepositorio extends JpaRepository<NotaCredito, UUID> {
    Optional<NotaCredito> findByCodigo(String codigo);
    List<NotaCredito> findByClienteOrderByFechaEmisionDesc(Clientes cliente);
    List<NotaCredito> findByClienteAndEstadoOrderByFechaEmisionDesc(Clientes cliente, String estado);
}
