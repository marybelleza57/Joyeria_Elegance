package com.joyas.elegance.repository;

import com.joyas.elegance.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByCorreo(String correo);
}
