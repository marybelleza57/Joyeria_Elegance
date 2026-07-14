package com.joyas.elegance.model;

public class JwtResponse {
    private String token;
    private String tipo = "Bearer";
    private String email;
    private String rol;
    private String userId;

    public JwtResponse(String token, String email, String rol, String userId) {
        this.token = token;
        this.email = email;
        this.rol = rol;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}