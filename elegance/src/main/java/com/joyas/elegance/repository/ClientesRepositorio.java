package com.joyas.elegance.repository;

import com.joyas.elegance.model.Clientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientesRepositorio extends JpaRepository<Clientes, UUID> {
    Optional<Clientes> findByCorreo(String correo);
    Optional<Clientes> findByDni(String dni);
}
