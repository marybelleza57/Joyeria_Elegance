package com.joyas.elegance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String apellidos;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un correo válido")
    private String correo;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    private String estado;

    private String telefono;

    @Column(name = "contrasena_hash")
    private String contrasenaHash;

    @Column(name = "token_recuperacion")
    private String tokenRecuperacion;

    @Column(name = "fecha_token_recuperacion")
    private LocalDateTime fechaTokenRecuperacion;

    public Usuario() {
    }

    public Usuario(UUID id, String nombre, String apellidos, String correo, Rol rol, String estado,
            String contrasenaHash) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.rol = rol;
        this.estado = estado;
        this.contrasenaHash = contrasenaHash;
    }

    public Usuario(UUID id, String nombre, String apellidos, String correo, String telefono, Rol rol, String estado,
            String contrasenaHash) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.rol = rol;
        this.estado = estado;
        this.contrasenaHash = contrasenaHash;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public String getTokenRecuperacion() {
        return tokenRecuperacion;
    }

    public void setTokenRecuperacion(String tokenRecuperacion) {
        this.tokenRecuperacion = tokenRecuperacion;
    }

    public LocalDateTime getFechaTokenRecuperacion() {
        return fechaTokenRecuperacion;
    }

    public void setFechaTokenRecuperacion(LocalDateTime fechaTokenRecuperacion) {
        this.fechaTokenRecuperacion = fechaTokenRecuperacion;
    }
}