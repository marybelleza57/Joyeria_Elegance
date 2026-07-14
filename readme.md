# Joyería Elegante

Aplicación web para tienda de joyas con catálogo, pedidos, devoluciones, notas de crédito y panel administrativo.

---
## Autores

### Equipo de Desarrollo

- **Wilder Eduardo Orellana Diaz**
- **Diego Javier Coveñas Guillermo**
- **María Pascual Orbegozo**
- **Dennys Rodolfo Gil Torres**
- **Carlos Andres Espinoza Vasquez**
- **Roger Mallqui Meza**
---
## Tecnologías utilizadas

| Tecnología | Versión |
|------------|---------|
| **Java** | 26 |
| **Spring Boot** | 4.0.6 |
| **Spring MVC** | 4.0.6 |
| **Spring Data JPA** | 4.0.6 |
| **Spring Security** | - |
| **Spring Boot Actuator** | - |
| **Spring Boot DevTools** | - |
| **Thymeleaf** | - |
| **JWT (JJWT)** | 0.12.6 |
| **MySQL Connector** | - |
| **Hibernate** | - |
| **BCrypt (Spring Security Crypto)** | - |
| **Maven** | 3.9+ |
| **Lombok** | 1.18.46 |
| **Cloudinary** | 2.0.0 |
| **Brevo (Sendinblue SDK)** | 6.0.0 |
| **dotenv-java** | 3.2.0 |
| **Bootstrap** | 5.3.3 |
| **FontAwesome** | 6.5.2 |

---

## Dependencias principales

| Dependencia | Propósito |
|-------------|-----------|
| `spring-boot-starter-webmvc` | Aplicación web MVC |
| `spring-boot-starter-data-jpa` | Persistencia con JPA/Hibernate |
| `spring-boot-starter-security` | Seguridad y autenticación |
| `spring-boot-starter-thymeleaf` | Motor de plantillas |
| `spring-boot-starter-validation` | Validación de datos |
| `spring-boot-starter-actuator` | Monitoreo y métricas |
| `spring-security-crypto` | Encriptación BCrypt |
| `jjwt-api/impl/jackson` | Generación y validación de JWT |
| `mysql-connector-j` | Conexión a MySQL |
| `cloudinary-http5` | Almacenamiento de imágenes |
| `sib-api-v3-sdk` | Envío de correos (Brevo) |
| `dotenv-java` | Variables de entorno |
| `lombok` | Reducción de código boilerplate |
| `spring-boot-devtools` | Recarga automática en desarrollo |

---

## Estructura del Proyecto

```
elegance/
├── src/
│   ├── main/
│   │   ├── java/com/joyas/elegance/
│   │   │   ├── config/              # Configuraciones (Security, Interceptor)
│   │   │   ├── controller/          # Controladores MVC
│   │   │   ├── model/               # Entidades JPA
│   │   │   ├── repository/          # Repositorios Spring Data JPA
│   │   │   └── service/             # Servicios (Email, Cloudinary)
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/             # Hojas de estilo
│   │       │   ├── js/              # JavaScript
│   │       │   └── images/          # Imagenes estaticas
│   │       ├── templates/
│   │       │   ├── admin/           # Plantillas panel administrador
│   │       │   ├── cliente/         # Plantillas modulo cliente
│   │       │   └── fragments/       # Fragmentos reutilizables
│   │       └── application.properties
│   └── test/                        # Pruebas unitarias
├── Base de datos/
│   └── Joyeria_elegante_produccion.sql
├── .env                             # Variables de entorno
├── pom.xml                          # Dependencias Maven
└── README.md                        # Este archivo
```
---

## Requisitos previos

| Software | Versión |
|----------|---------|
| Java | 26 o superior |
| MySQL | 8.0 o superior |
| Maven | 3.9 o superior |
| IDE | IntelliJ IDEA, VS Code o Eclipse |

---

## Instalación y configuración

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd elegance
```
### 2. Crear la base de datos

```bash
CREATE DATABASE IF NOT EXISTS joyas_elegance CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Importar el script de la base de datos

```bash
mysql -u root -p joyas_elegance < "Base de datos/Joyería_elegante_producción.sql"
```
### 4. Configurar variables de entorno

Crear un archivo **`.env`** en la raíz del proyecto con la siguiente estructura:

```env
# ============================================
#  SERVER CONFIGURATION
# ============================================
SERVER_PORT=8080

# ============================================
#  DATABASE CONFIGURATION
# ============================================
DB_URL=jdbc:mysql://localhost:3306/joyas_elegance?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true
DB_USERNAME=root
DB_PASSWORD=tu_contraseña
DB_DRIVER=com.mysql.cj.jdbc.Driver

# ============================================
#  JPA / HIBERNATE CONFIGURATION
# ============================================
JPA_DDL_AUTO=update
JPA_SHOW_SQL=false
HIBERNATE_DIALECT=org.hibernate.dialect.MySQLDialect

# ============================================
#  THYMELEAF CONFIGURATION
# ============================================
THYMELEAF_CACHE=false

# ============================================
#  FILE UPLOAD CONFIGURATION
# ============================================
MAX_FILE_SIZE=15MB
MAX_REQUEST_SIZE=15MB

# ============================================
#  CLOUDINARY CONFIGURATION (Imágenes)
# ============================================
CLOUDINARY_URL=tu_clave_api

# ============================================
#  BREVO CONFIGURATION (Email Service)
# ============================================
BREVO_API_KEY=tu_clave_api
BREVO_SENDER_EMAIL=tu_correo
BREVO_SENDER_NAME=Joyería Elegante
BREVO_BASE_URL=https://api.brevo.com/v3

# ============================================
#  JWT CONFIGURATION (Autenticación)
# ============================================
JWT_SECRET=tu_JWT_SECRET
JWT_EXPIRATION=86400000  # 24 horas en milisegundos
```

---

### **application.properties**

Inyectando directamente en `application.properties`:

```properties
spring.application.name=elegance
server.port=${SERVER_PORT}

# Config MySQL
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=${DB_DRIVER}

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=${JPA_DDL_AUTO}
spring.jpa.show-sql=${JPA_SHOW_SQL}
spring.jpa.open-in-view=false
spring.jpa.properties.hibernate.dialect=${HIBERNATE_DIALECT}
spring.jpa.properties.hibernate.format_sql=true

# Thymeleaf
spring.thymeleaf.cache=${THYMELEAF_CACHE}

# Límite de tamaño de subida de archivos
spring.servlet.multipart.max-file-size=${MAX_FILE_SIZE}
spring.servlet.multipart.max-request-size=${MAX_REQUEST_SIZE}

# Forced UTF-8 Encoding
spring.http.encoding.charset=${ENCODING_CHARSET}
spring.http.encoding.force=${ENCODING_FORCE}
spring.http.encoding.enabled=${ENCODING_ENABLED}
server.servlet.encoding.charset=${ENCODING_CHARSET}
server.servlet.encoding.force=${ENCODING_FORCE}
server.servlet.encoding.enabled=${ENCODING_ENABLED}

# Brevo Configuration
brevo.api.key=${BREVO_API_KEY}
brevo.sender.email=${BREVO_SENDER_EMAIL}
brevo.sender.name=${BREVO_SENDER_NAME}
brevo.base.url=${BREVO_BASE_URL}

# Cloudinary
cloudinary.url=${CLOUDINARY_URL}

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION}

# Error handling
server.error.whitelabel.enabled=false
server.error.path=/error
error.404=Página no encontrada
error.405=Método no permitido
error.403=Acceso denegado
error.500=Error interno del servidor
error.400=Solicitud incorrecta
error.default=Ha ocurrido un error inesperado

# Configuración de idioma
spring.messages.basename=messages
spring.messages.encoding=UTF-8
spring.web.locale=es
spring.web.locale-resolver=accept-header
```
## 5. Ejecutar el proyecto

### Opción 1: Desde el IDE
Ejecutar la clase principal:

```
com.joyas.elegance.EleganceApplication
```

### Opción 2: Desde terminal con Maven
```bash
mvn clean spring-boot:run
```
---

## Acceso a la aplicación

| Página | URL |
|--------|-----|
| Página principal | `http://localhost:8080/` |
| Catálogo | `http://localhost:8080/catalogo` |
| Panel Administrativo | `http://localhost:8080/admin/login` |

---

## Usuarios de prueba

| Rol | Correo | Contraseña |
|-----|--------|------------|
| Administrador | `h8c75tzitt@lnovic.com` | `123456789` |
| Cliente | `jonine7015@dysonc.com` | `123456789` |

---
## 🐳 Despliegue con Docker

### Requisitos previos

| Software | Descripción |
|----------|-------------|
| **Docker Desktop** | Necesario para construir y ejecutar la imagen Docker |
| **Docker Engine** | Motor de contenedores (incluido en Docker Desktop) |

**Descargar Docker Desktop:** https://www.docker.com/products/docker-desktop/

---

### Construir la imagen Docker

```bash
# Navegar a la raíz del proyecto
cd C:\Users\Jordy\Documents\proyectos\elegance
```
# Construir la imagen

```bash
docker build -t joyas-elegance .
```

### Ejecutar el contenedor

```bash
# Ejecutar con variables de entorno desde .env
docker run --name joyas-elegance -d -p 8080:8080 --env-file .env joyas-elegance:latest
```

## Características principales

### Módulo público
- Catálogo de productos con filtros
- Registro e inicio de sesión de clientes
- Recuperación de contraseña
- Carrito de compras

### Módulo cliente
- Gestión de perfil
- Historial de pedidos
- Solicitud de devoluciones
- Notas de crédito

### Módulo administrador
- Dashboard con métricas
- CRUD de productos
- CRUD de categorías
- Gestión de pedidos
- Gestión de devoluciones
- Gestión de clientes
- Gestión de usuarios
- Cambio de contraseña

### Seguridad
- Autenticación con sesión (web)
- Autenticación con JWT (API)
- Encriptación BCrypt
- Interceptor para rutas administrativas

---

## Estructura de la base de datos

| Tabla | Descripción |
|-------|-------------|
| `usuarios` | Usuarios del sistema |
| `clientes` | Clientes registrados |
| `roles` | Roles de usuarios |
| `productos` | Catálogo de productos |
| `categorias` | Categorías de productos |
| `pedidos` | Pedidos realizados |
| `detalle_pedidos` | Detalle de pedidos |
| `notas_credito` | Notas de crédito |