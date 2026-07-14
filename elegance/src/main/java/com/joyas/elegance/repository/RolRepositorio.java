package com.joyas.elegance.repository;

import com.joyas.elegance.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RolRepositorio extends JpaRepository<Rol, UUID> {
    Optional<Rol> findByNombre(String nombre);
}
