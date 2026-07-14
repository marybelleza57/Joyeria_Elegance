package com.joyas.elegance.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();

        if (uri.equals("/admin/login") ||
                uri.equals("/admin/olvide-contrasena") ||
                uri.equals("/admin/restablecer-contrasena") ||
                uri.startsWith("/css/") ||
                uri.startsWith("/js/") ||
                uri.startsWith("/images/") ||
                uri.startsWith("/favicon")) {
            return true;
        }

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("adminLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }

        Object adminObj = session.getAttribute("adminLogueado");
        if (adminObj instanceof com.joyas.elegance.model.Usuario) {
            com.joyas.elegance.model.Usuario usuario = (com.joyas.elegance.model.Usuario) adminObj;
            if (usuario.getRol() == null || (!"Administrador".equalsIgnoreCase(usuario.getRol().getNombre())
                    && !"ADMIN".equalsIgnoreCase(usuario.getRol().getNombre()))) {
                session.removeAttribute("adminLogueado");
                response.sendRedirect(request.getContextPath() + "/admin/login");
                return false;
            }
        }

        return true;
    }
}