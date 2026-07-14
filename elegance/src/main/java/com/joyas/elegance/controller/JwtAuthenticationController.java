package com.joyas.elegance.controller;

import com.joyas.elegance.model.JwtRequest;
import com.joyas.elegance.model.JwtResponse;
import com.joyas.elegance.model.Usuario;
import com.joyas.elegance.repository.UsuarioRepositorio;
import com.joyas.elegance.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class JwtAuthenticationController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {
        Optional<Usuario> optUsuario = usuarioRepositorio.findByCorreo(request.getEmail());

        if (optUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtResponse(null, null, null, null));
        }

        Usuario usuario = optUsuario.get();

        boolean contrasenaValida = BCrypt.checkpw(request.getContrasena(), usuario.getContrasenaHash());
        if (!contrasenaValida) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtResponse(null, null, null, null));
        }

        if (!"ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtResponse(null, null, null, null));
        }

        String rol = usuario.getRol() != null ? usuario.getRol().getNombre() : "USER";
        String token = jwtUtil.generateToken(usuario.getCorreo(), rol, usuario.getId());

        return ResponseEntity.ok(new JwtResponse(token, usuario.getCorreo(), rol, usuario.getId().toString()));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }

        String token = authorizationHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expirado");
        }

        String email = jwtUtil.extractEmail(token);
        String rol = jwtUtil.extractRol(token);

        return ResponseEntity.ok(new JwtResponse(token, email, rol, jwtUtil.extractUserId(token)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }

        String token = authorizationHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expirado");
        }

        String email = jwtUtil.extractEmail(token);
        String rol = jwtUtil.extractRol(token);
        String userId = jwtUtil.extractUserId(token);

        String newToken = jwtUtil.generateToken(email, rol, UUID.fromString(userId));

        return ResponseEntity.ok(new JwtResponse(newToken, email, rol, userId));
    }
}