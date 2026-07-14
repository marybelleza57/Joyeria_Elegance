package com.joyas.elegance.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/",
            "/catalogo",
            "/reclamaciones",
            "/vip/registrar",
            "/admin/login",
            "/admin/olvide-contrasena",
            "/admin/restablecer-contrasena",
            "/cliente/login",
            "/cliente/registrar",
            "/cliente/olvide-contrasena",
            "/cliente/restablecer-contrasena",
            "/api/auth/");

    private static final List<String> STATIC_RESOURCES = Arrays.asList(
            "/css/",
            "/js/",
            "/images/",
            "/img/",
            "/favicon");

    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/error");

    private boolean isPublicPath(String path) {
        for (String publicPath : PUBLIC_PATHS) {
            if (path.equals(publicPath) || path.startsWith(publicPath)) {
                return true;
            }
        }
        for (String resource : STATIC_RESOURCES) {
            if (path.startsWith(resource)) {
                return true;
            }
        }
        for (String excluded : EXCLUDED_PATHS) {
            if (path.startsWith(excluded)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Excluir rutas públicas
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Solo validar JWT en rutas API
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token no válido");
            return;
        }

        try {
            String token = authHeader.substring(7);

            if (!jwtUtil.validateToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expirado");
                return;
            }

            String email = jwtUtil.extractEmail(token);
            request.setAttribute("email", email);
            request.setAttribute("userId", jwtUtil.extractUserId(token));
            request.setAttribute("rol", jwtUtil.extractRol(token));

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expirado");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido");
        }
    }
}