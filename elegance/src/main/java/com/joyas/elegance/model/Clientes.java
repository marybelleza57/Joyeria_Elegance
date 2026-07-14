package com.joyas.elegance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clientes")
public class Clientes {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un correo electrónico válido")
    @Column(name = "correo")
    private String correo;

    @Column(name = "direccion")
    private String direccion;

    private String telefono;

    @Column(name = "dni", unique = true)
    private String dni;

    @Column(name = "departamento")
    private String departamento;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "preferencia_material")
    private String preferencia;

    @Column(name = "acepta_terminos")
    private Boolean aceptaTerminos;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_creacion")
    private LocalDateTime createdAt;

    private String estado;

    @Column(name = "contrasena_hash")
    private String contrasenaHash;

    @Column(name = "nivel_fidelidad")
    private String nivelFidelidad;

    @Column(name = "genero")
    private String genero;

    @Column(name = "token_recuperacion")
    private String tokenRecuperacion;

    @Column(name = "fecha_token_recuperacion")
    private LocalDateTime fechaTokenRecuperacion;

    public Clientes() {
        this.createdAt = LocalDateTime.now();
        this.fechaRegistro = LocalDateTime.now();
        this.estado = "ACTIVO";
        this.nivelFidelidad = "VIP";
        this.genero = "Otro";
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
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

    public String getDireccion() {
        return (direccion != null) ? direccion : "";
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPreferencia() {
        return preferencia;
    }

    public void setPreferencia(String preferencia) {
        this.preferencia = preferencia;
    }

    public Boolean getAceptaTerminos() {
        return aceptaTerminos;
    }

    public void setAceptaTerminos(Boolean aceptaTerminos) {
        this.aceptaTerminos = aceptaTerminos;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public String getNivelFidelidad() {
        return (nivelFidelidad != null) ? nivelFidelidad : "VIP";
    }

    public void setNivelFidelidad(String nivelFidelidad) {
        this.nivelFidelidad = nivelFidelidad;
    }

    public String getGenero() {
        return (genero != null) ? genero : "Otro";
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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