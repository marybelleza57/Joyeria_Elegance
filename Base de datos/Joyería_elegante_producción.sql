CREATE DATABASE  IF NOT EXISTS `joyas_elegance` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `joyas_elegance`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: joyas_elegance
-- ------------------------------------------------------
-- Server version	9.6.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '3cde2219-65b2-11f1-af2d-0a0027000012:1-668';

--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `estado` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES ('093eabc3-65b4-11f1-9b81-0a0027000012','Anillos','test','ACTIVO','2026-06-11 16:38:54','2026-07-11 16:24:59'),('093eb991-65b4-11f1-9b81-0a0027000012','Aretes','Aretes elegantes para toda ocasión','ACTIVO','2026-06-11 16:38:54','2026-07-11 01:36:53'),('093ebf66-65b4-11f1-9b81-0a0027000012','Collares','Collares y gargantillas de lujo','ACTIVO','2026-06-11 16:38:54','2026-07-11 01:36:53'),('093ec36c-65b4-11f1-9b81-0a0027000012','Pulseras','Pulseras finas en oro y plata','ACTIVO','2026-06-11 16:38:54','2026-07-11 01:36:53'),('446b4e53-65d2-11f1-9b81-0a0027000012','Dijes','Dijes y colgantes con diseños exclusivos','ACTIVO','2026-06-11 20:15:19','2026-07-11 01:36:53'),('4472f83f-65d2-11f1-9b81-0a0027000012','Relojes','Relojes de pulsera para dama y caballero','ACTIVO','2026-06-11 20:15:19','2026-07-11 01:36:53'),('447318f2-65d2-11f1-9b81-0a0027000012','Tobilleras','Tobilleras finas y casuales','ACTIVO','2026-06-11 20:15:19','2026-07-11 01:36:53'),('447322be-65d2-11f1-9b81-0a0027000012','Juegos de Joyas','Sets completos de joyería fina','ACTIVO','2026-06-11 20:15:19','2026-07-11 01:36:53');
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombres` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellidos` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `genero` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `correo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `direccion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `contrasena_hash` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telefono` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dni` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `departamento` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `provincia` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `preferencia_material` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nivel_fidelidad` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `acepta_terminos` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_registro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `ultima_compra` date DEFAULT NULL,
  `total_gastado` decimal(12,2) DEFAULT '0.00',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_token_recuperacion` datetime(6) DEFAULT NULL,
  `token_recuperacion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`correo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES ('0940ab53-65b4-11f1-9b81-0a0027000012','María ','González','FEMENINO','maria.vip@example.com','Casa sola','$2a$10$N3HxgNcFPFMCpA6nsr.8Lu2Ff5xpJOrBuTnpcJLNQpHg5FRkIGZNG','999123456','12345678','Lambayeque','Chiclayo','Oro','PLATA',1,'2026-06-11 16:38:54',NULL,8500.00,NULL,'ACTIVO',NULL,NULL),('50a50bc8-8d76-42e4-b50d-dcf09faa8829','Test','Test','Otro','testyape@example.com','','$2a$10$N3HxgNcFPFMCpA6nsr.8Lu2Ff5xpJOrBuTnpcJLNQpHg5FRkIGZNG','999123456',NULL,NULL,NULL,NULL,'VIP',1,'2026-07-13 05:21:59',NULL,0.00,'2026-07-13 05:21:59','SUSPENDIDO',NULL,NULL),('5e62342f-8ba9-46aa-ae74-57f73bdbe808','Juan','Perez','MASCULINO','juan.perez@example.com','','$2a$10$Ah05DlJmZi88cKfgXoSOXOgHzbnzTLOmlcsMUxU8krp6u/shr4vRa','999888777','','','','','VIP',1,'2026-07-13 04:21:47',NULL,0.00,'2026-07-13 04:21:47','ACTIVO',NULL,NULL),('966aa66f-bec3-47d0-b765-00b9152022fe','Josep','González','MASCULINO','jonine7015@dysonc.com','','$2a$10$p3R.rHjiUfJMPD/HUZ0P6uDaKv.ipgzimKdU8OYT/v7f1OeTY/mEK','963852147','','','','Oro 14k','VIP',1,'2026-07-14 03:10:05',NULL,0.00,'2026-07-14 03:10:05','ACTIVO','2026-07-14 00:43:05.059730','f566a663-5c6c-46ce-8109-1d3185a5920d');
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_pedidos`
--

DROP TABLE IF EXISTS `detalle_pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_pedidos` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pedido_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `producto_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cantidad` int NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `estado_devolucion` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'NINGUNO',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_pedido_producto` (`pedido_id`,`producto_id`),
  KEY `fk_detalle_productos` (`producto_id`),
  CONSTRAINT `fk_detalle_pedidos` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_detalle_productos` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_pedidos`
--

LOCK TABLES `detalle_pedidos` WRITE;
/*!40000 ALTER TABLE `detalle_pedidos` DISABLE KEYS */;
INSERT INTO `detalle_pedidos` VALUES ('07f87cd5-c4c0-43a6-b3fb-954727a4e2f8','ba890310-8cd6-4bdb-b6e1-513a1c804ed1','5a0aae33-65d2-11f1-9b81-0a0027000012',1,850.00,'NINGUNO'),('0d210c6e-48df-4146-a746-ca3b90af8ece','b1d4c55e-b816-40a6-9a2c-ce05711dbe90','5a0aae33-65d2-11f1-9b81-0a0027000012',1,850.00,'DEV_SOLICITADA'),('18f7b94b-749e-47d0-9999-1fc4a72136fb','a7e46c62-5d9e-49b7-9658-023c318ce586','4b601325-302e-49b0-b159-92184c454dc6',3,100.00,'NINGUNO'),('2fbb4973-0fcc-499c-8a78-a67d3c8f32f5','ceae703b-a8cb-4bf2-b591-736583112797','4b601325-302e-49b0-b159-92184c454dc6',1,100.00,'NINGUNO'),('553235ea-7edc-44f9-a303-97cea72c65d4','799c1325-11ee-4a22-a028-67ca72ff5c5d','4b601325-302e-49b0-b159-92184c454dc6',1,100.00,'DEV_APROBADA'),('56b6f06b-6555-4c9e-9db7-a7c71e833ef4','f6daacad-aa81-4e99-9d21-6fa14d704b17','4b601325-302e-49b0-b159-92184c454dc6',1,100.00,'NINGUNO'),('69abec68-896c-4ac0-9275-815d3d27593e','174a995e-84a8-4a0d-b909-d7bf2207275f','5a0aae33-65d2-11f1-9b81-0a0027000012',4,850.00,'NINGUNO'),('aaa7200d-e811-4825-8dcc-4693852d6da3','b1d4c55e-b816-40a6-9a2c-ce05711dbe90','4b601325-302e-49b0-b159-92184c454dc6',1,100.00,'DEV_APROBADA'),('c037048a-6fb7-4a9e-9eb9-fcd2820c54dc','ba890310-8cd6-4bdb-b6e1-513a1c804ed1','4b601325-302e-49b0-b159-92184c454dc6',1,100.00,'NINGUNO'),('d6a02107-428f-4061-a2e8-39f9e40f7e62','5aa20a21-2011-4222-88b5-152fbda0dc7f','4b601325-302e-49b0-b159-92184c454dc6',1,100.00,'NINGUNO'),('edb66591-0708-4d4f-b613-50cb65caa68a','f20f27b8-963d-4eeb-bb58-987b9c783daf','5a0aae33-65d2-11f1-9b81-0a0027000012',2,850.00,'NINGUNO'),('f4f3cc0c-bd6a-4a8f-b5a1-66b1a186f855','174a995e-84a8-4a0d-b909-d7bf2207275f','5a0aeaf6-65d2-11f1-9b81-0a0027000012',6,28500.00,'NINGUNO');
/*!40000 ALTER TABLE `detalle_pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notas_credito`
--

DROP TABLE IF EXISTS `notas_credito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notas_credito` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `codigo` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_emision` date NOT NULL,
  `monto_disponible` decimal(10,2) NOT NULL,
  `monto_inicial` decimal(10,2) NOT NULL,
  `cliente_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pedido_origen_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKn1hnq303nt0vmu1dgpgqhqwsw` (`codigo`),
  KEY `FK19u73okarhuuvbc1mrlug6kay` (`cliente_id`),
  KEY `FK383479ws9pqt9dexyfditaatv` (`pedido_origen_id`),
  CONSTRAINT `FK19u73okarhuuvbc1mrlug6kay` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  CONSTRAINT `FK383479ws9pqt9dexyfditaatv` FOREIGN KEY (`pedido_origen_id`) REFERENCES `pedidos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notas_credito`
--

LOCK TABLES `notas_credito` WRITE;
/*!40000 ALTER TABLE `notas_credito` DISABLE KEYS */;
INSERT INTO `notas_credito` VALUES ('9e3f77df-8343-4730-9a24-09781bc567b5','NC-PE000020-AAA7','USADO_TOTAL','2026-07-13',0.00,100.00,'0940ab53-65b4-11f1-9b81-0a0027000012','b1d4c55e-b816-40a6-9a2c-ce05711dbe90'),('ecbc23f6-6f83-4c2d-9342-4613edb9d6a9','NC-PE000007-5532','ACTIVO','2026-07-13',100.00,100.00,'0940ab53-65b4-11f1-9b81-0a0027000012','799c1325-11ee-4a22-a028-67ca72ff5c5d');
/*!40000 ALTER TABLE `notas_credito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos`
--

DROP TABLE IF EXISTS `pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cliente_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `numero_pedido` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `total` decimal(12,2) NOT NULL,
  `estado` enum('PENDIENTE','CONFIRMADO','EN_TRANSITO','ENTREGADO','CANCELADO','DEV_SOLICITADA','DEV_APROBADA','DEV_RECHAZADA') COLLATE utf8mb4_unicode_ci NOT NULL,
  `motivo_cancelacion` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `direccion_envio` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono_contacto` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `notas` text COLLATE utf8mb4_unicode_ci,
  `fecha_entrega` date DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `numero_pedido` (`numero_pedido`),
  KEY `fk_pedidos_clientes` (`cliente_id`),
  CONSTRAINT `fk_pedidos_clientes` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos`
--

LOCK TABLES `pedidos` WRITE;
/*!40000 ALTER TABLE `pedidos` DISABLE KEYS */;
INSERT INTO `pedidos` VALUES ('174a995e-84a8-4a0d-b909-d7bf2207275f','0940ab53-65b4-11f1-9b81-0a0027000012','PE-000018',174400.00,'PENDIENTE',NULL,'as','963852741','asd',NULL,'2026-07-13 05:19:06','2026-07-13 05:19:06'),('5aa20a21-2011-4222-88b5-152fbda0dc7f','0940ab53-65b4-11f1-9b81-0a0027000012','PE-000009',100.00,'CANCELADO','Pago no realizado','Casa sola','999123456','',NULL,'2026-07-14 03:31:50','2026-07-14 03:32:10'),('799c1325-11ee-4a22-a028-67ca72ff5c5d','0940ab53-65b4-11f1-9b81-0a0027000012','PE-000007',100.00,'DEV_APROBADA',NULL,'Casa sola','999123456','','2026-07-13','2026-07-14 03:24:41','2026-07-14 03:32:38'),('a7e46c62-5d9e-49b7-9658-023c318ce586','5e62342f-8ba9-46aa-ae74-57f73bdbe808','PE-000016',300.00,'CONFIRMADO',NULL,'Av. Arequipa 4560, Miraflores, Lima','999888777','Entregar en la recepcion.',NULL,'2026-07-13 04:24:19','2026-07-13 18:53:40'),('b1d4c55e-b816-40a6-9a2c-ce05711dbe90','0940ab53-65b4-11f1-9b81-0a0027000012','PE-000020',950.00,'DEV_SOLICITADA',NULL,'Casa sola','999123456','','2026-07-13','2026-07-13 06:26:10','2026-07-14 02:39:05'),('ba890310-8cd6-4bdb-b6e1-513a1c804ed1','0940ab53-65b4-11f1-9b81-0a0027000012','PE-000017',950.00,'PENDIENTE',NULL,'Av.Casa','963852748','Es una casa',NULL,'2026-07-13 04:24:40','2026-07-13 21:23:55'),('ceae703b-a8cb-4bf2-b591-736583112797','0940ab53-65b4-11f1-9b81-0a0027000012','PE-000006',0.00,'ENTREGADO',NULL,'Casa sola','999123456','[Nota de Crédito aplicada: NC-PE000020-AAA7, Descuento: S/. 100.00] ','2026-07-13','2026-07-14 02:47:20','2026-07-14 03:31:21'),('f20f27b8-963d-4eeb-bb58-987b9c783daf','50a50bc8-8d76-42e4-b50d-dcf09faa8829','PE-000019',1700.00,'PENDIENTE',NULL,'Av. Larco 123, Miraflores, Lima','999123456','',NULL,'2026-07-13 05:24:18','2026-07-13 05:24:18'),('f6daacad-aa81-4e99-9d21-6fa14d704b17','0940ab53-65b4-11f1-9b81-0a0027000012','PE-000008',100.00,'ENTREGADO',NULL,'Casa sola','999123456','','2026-07-13','2026-07-14 03:29:24','2026-07-14 03:30:18');
/*!40000 ALTER TABLE `pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `categoria_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `enlace_amigable` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `precio` decimal(38,2) DEFAULT NULL,
  `material` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `imagen_principal` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `destacado` tinyint(1) DEFAULT '0',
  `estado` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `orientacion_estilo` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cantidad` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `enlace_amigable` (`enlace_amigable`),
  KEY `fk_productos_categorias` (`categoria_id`),
  CONSTRAINT `fk_productos_categorias` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES ('4b601325-302e-49b0-b159-92184c454dc6','093ec36c-65b4-11f1-9b81-0a0027000012','Pulsera plata Hombre','pulsera-plata-hombre',100.00,'Plata 925','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783825618/productos/pulsera-plata-hombre.webp',0,'ACTIVO','2026-07-12 03:06:58','2026-07-13 22:31:20','UNISEX',98),('5a0aae33-65d2-11f1-9b81-0a0027000012','093eabc3-65b4-11f1-9b81-0a0027000012','Anillo Aro Plata Ley 925','anillo-aro-plata-ley-925',850.00,'Plata 925','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783797554/productos/anillo-aro-plata-ley-925.webp',0,'ACTIVO','2026-06-11 20:15:55','2026-07-12 02:12:58','UNISEX',50),('5a0aeaf6-65d2-11f1-9b81-0a0027000012','093eabc3-65b4-11f1-9b81-0a0027000012','Anillo Compromiso Brillantes','anillo-compromiso-brillantes',28500.00,'Oro Blanco 18k','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783808940/productos/anillo-compromiso-brillantes.webp',0,'ACTIVO','2026-06-11 20:15:55','2026-07-12 02:13:21','FEMENINO',50),('5a0af848-65d2-11f1-9b81-0a0027000012','093eabc3-65b4-11f1-9b81-0a0027000012','Anillo Ajustable Corazón','anillo-ajustable-corazon',350.00,'Plata 925','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783809003/productos/anillo-ajustable-corazon.webp',0,'ACTIVO','2026-06-11 20:15:55','2026-07-12 02:13:26','FEMENINO',50),('5a0b02a4-65d2-11f1-9b81-0a0027000012','093eb991-65b4-11f1-9b81-0a0027000012','Aretes Argolla Oro 14k','aretes-argolla-oro-14k',2200.00,'Oro 14k','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783809114/productos/aretes-argolla-oro-14k.webp',0,'ACTIVO','2026-06-11 20:15:55','2026-07-12 02:13:38','FEMENINO',50),('5a0b0b1e-65d2-11f1-9b81-0a0027000012','093eb991-65b4-11f1-9b81-0a0027000012','Aretes Colgantes Lágrima','aretes-colgantes-lagrima',1800.00,'Plata 925','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783809231/productos/aretes-colgantes-lagrima.webp',0,'ACTIVO','2026-06-11 20:15:55','2026-07-12 02:13:44','FEMENINO',50),('5a0b12d2-65d2-11f1-9b81-0a0027000012','093eb991-65b4-11f1-9b81-0a0027000012','Aretes Perla Cultivada','aretes-perla-cultivada',2800.00,'Plata 925 / Perla','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783809296/productos/aretes-perla-cultivada.webp',0,'ACTIVO','2026-06-11 20:15:55','2026-07-12 02:13:59','FEMENINO',50),('5a0b2d87-65d2-11f1-9b81-0a0027000012','093eb991-65b4-11f1-9b81-0a0027000012','Aretes Bohemios Pluma','aretes-bohemios-pluma',1200.00,'Plata bañada en oro rosa','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783817398/productos/aretes-bohemios-pluma.webp',0,'ACTIVO','2026-06-11 20:15:55','2026-07-12 02:14:09','FEMENINO',50),('5a0b3a6d-65d2-11f1-9b81-0a0027000012','093ebf66-65b4-11f1-9b81-0a0027000012','Collar Gargantilla Corazón','collar-gargantilla-corazon',5800.00,'Oro 18k','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783817686/productos/collar-gargantilla-corazon.webp',0,'ACTIVO','2026-06-11 20:15:55','2026-07-12 02:14:20','FEMENINO',50),('5a0b448d-65d2-11f1-9b81-0a0027000012','093ebf66-65b4-11f1-9b81-0a0027000012','Collar Largo Cadena Veneciana','collar-largo-cadena-veneciana',1650.00,'Plata 925','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783817954/productos/collar-largo-cadena-veneciana.webp',0,'ACTIVO','2026-06-11 20:15:55','2026-07-12 02:14:33','UNISEX',50),('5a0b4dba-65d2-11f1-9b81-0a0027000012','093ebf66-65b4-11f1-9b81-0a0027000012','Collar Colgante Luna','collar-colgante-luna',4200.00,'Oro 18k','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783818929/productos/collar-colgante-luna.webp',0,'ACTIVO','2026-06-11 20:15:55','2026-07-12 02:14:43','FEMENINO',50),('5a0b712d-65d2-11f1-9b81-0a0027000012','093ebf66-65b4-11f1-9b81-0a0027000012','Collar Gargantilla Choker Negra','collar-gargantilla-choker-negra',250.00,'Acero dorado / Terciopelo','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783819089/productos/collar-gargantilla-choker-negra.webp',0,'ACTIVO','2026-06-11 20:15:55','2026-07-12 02:14:51','FEMENINO',50),('5a0b7e48-65d2-11f1-9b81-0a0027000012','093ec36c-65b4-11f1-9b81-0a0027000012','Pulsera Esclava Oro 18k','pulsera-esclava-oro-18k',9500.00,'Oro 18k','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783819151/productos/pulsera-esclava-oro-18k.webp',0,'ACTIVO','2026-06-11 20:15:55','2026-07-12 02:15:01','FEMENINO',50),('5a0b86ab-65d2-11f1-9b81-0a0027000012','093ec36c-65b4-11f1-9b81-0a0027000012','Pulsera Nudos Marineros','pulsera-nudos-marineros',450.00,'Plata 925 / Hilo','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783819197/productos/pulsera-nudos-marineros.webp',0,'ACTIVO','2026-06-11 20:15:55','2026-07-12 02:15:11','FEMENINO',50),('5a0b8ea4-65d2-11f1-9b81-0a0027000012','093ec36c-65b4-11f1-9b81-0a0027000012','Pulsera Bolitas Oro 14k','pulsera-bolitas-oro-14k',3200.00,'Oro 14k','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783819249/productos/pulsera-bolitas-oro-14k.webp',0,'ACTIVO','2026-06-11 20:15:55','2026-07-12 02:15:19','FEMENINO',50),('5a0b97bc-65d2-11f1-9b81-0a0027000012','093ec36c-65b4-11f1-9b81-0a0027000012','Pulsera Hilo Rojo','pulsera-hilo-rojo',180.00,'Plata / Hilo','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783819353/productos/pulsera-hilo-rojo.webp',0,'ACTIVO','2026-06-11 20:15:55','2026-07-12 02:15:39','FEMENINO',50),('62adbf31-65d2-11f1-9b81-0a0027000012','446b4e53-65d2-11f1-9b81-0a0027000012','Dije Letra Elegante','dije-letra-elegante',3500.00,'Oro 18k','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783819419/productos/dije-letra-elegante.webp',0,'ACTIVO','2026-06-11 20:16:09','2026-07-12 02:15:45','UNISEX',50),('62adecce-65d2-11f1-9b81-0a0027000012','446b4e53-65d2-11f1-9b81-0a0027000012','Dije Virgencita Plata','dije-virgencita-plata',890.00,'Plata 925','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783819482/productos/dije-virgencita-plata.webp',0,'ACTIVO','2026-06-11 20:16:09','2026-07-12 02:15:53','UNISEX',50),('62adf7bd-65d2-11f1-9b81-0a0027000012','446b4e53-65d2-11f1-9b81-0a0027000012','Dije Árbol de la Vida','dije-arbol-de-la-vida',1100.00,'Plata 925','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783819554/productos/dije-arbol-de-la-vida.webp',0,'ACTIVO','2026-06-11 20:16:09','2026-07-12 02:15:59','UNISEX',50),('62ae04cc-65d2-11f1-9b81-0a0027000012','4472f83f-65d2-11f1-9b81-0a0027000012','Reloj Dama Dorado','reloj-dama-dorado',4500.00,'Acero bañado en oro','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783819602/productos/reloj-dama-dorado.webp',0,'ACTIVO','2026-06-11 20:16:09','2026-07-12 02:16:08','FEMENINO',50),('62ae0f59-65d2-11f1-9b81-0a0027000012','4472f83f-65d2-11f1-9b81-0a0027000012','Reloj Caballero Deportivo','reloj-caballero-deportivo',3800.00,'Acero inoxidable','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783819685/productos/reloj-caballero-deportivo.webp',0,'ACTIVO','2026-06-11 20:16:09','2026-07-12 02:16:15','MASCULINO',50),('62ae233c-65d2-11f1-9b81-0a0027000012','4472f83f-65d2-11f1-9b81-0a0027000012','Reloj Inteligente Dama','reloj-inteligente-dama',2800.00,'Aleación de zinc / Silicona','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783819744/productos/reloj-inteligente-dama.webp',0,'ACTIVO','2026-06-11 20:16:09','2026-07-12 02:16:23','FEMENINO',50),('62ae2e62-65d2-11f1-9b81-0a0027000012','447318f2-65d2-11f1-9b81-0a0027000012','Tobillera Delgada Plata','tobillera-delgada-plata',350.00,'Plata 925','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783819795/productos/tobillera-delgada-plata.webp',0,'ACTIVO','2026-06-11 20:16:09','2026-07-12 02:16:30','FEMENINO',50),('62ae3897-65d2-11f1-9b81-0a0027000012','447318f2-65d2-11f1-9b81-0a0027000012','Tobillera con Dijes','tobillera-con-dijes',550.00,'Plata 925','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783819892/productos/tobillera-con-dijes.webp',0,'ACTIVO','2026-06-11 20:16:09','2026-07-12 02:16:38','FEMENINO',50),('62ae42a7-65d2-11f1-9b81-0a0027000012','447322be-65d2-11f1-9b81-0a0027000012','Juego Joyas Bodas Plata','juego-joyas-bodas-plata',4200.00,'Plata 925','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783820049/productos/juego-joyas-bodas-plata.webp',0,'ACTIVO','2026-06-11 20:16:09','2026-07-12 02:16:49','FEMENINO',50),('62ae4b08-65d2-11f1-9b81-0a0027000012','447322be-65d2-11f1-9b81-0a0027000012','Juego Joyas Diario 3 piezas','juego-joyas-diario-3-piezas',1800.00,'Plata bañada en oro rosa','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783820124/productos/juego-joyas-diario-3-piezas.webp',0,'ACTIVO','2026-06-11 20:16:09','2026-07-12 02:16:59','FEMENINO',50),('62ae5273-65d2-11f1-9b81-0a0027000012','447322be-65d2-11f1-9b81-0a0027000012','Juego Collar y Aretes Oro','juego-collar-y-aretes-oro',8500.00,'Oro 14k','https://res.cloudinary.com/hvjwtrtr/image/upload/v1783820197/productos/juego-collar-y-aretes-oro.webp',0,'ACTIVO','2026-06-11 20:16:09','2026-07-12 02:17:10','FEMENINO',50);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES ('2c702009-b57d-49e2-b62e-80ada32e077a','Vendedor','Personal de ventas con acceso al catálogo y pedidos'),('94a5ed78-17a2-41c3-9f94-543db534e2a1','Administrador','Administrador del sistema con acceso total');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellidos` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `correo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contrasena_hash` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telefono` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `rol_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `estado` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `token_recuperacion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_token_recuperacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`correo`),
  KEY `fk_usuarios_roles` (`rol_id`),
  CONSTRAINT `fk_usuarios_roles` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES ('093b406e-65b4-11f1-9b81-0a0027000012','Diego Javier',' Coveñas Guillermo','admin@joyaselegance.com','$2b$10$Wztv4V.ATZC.SRm3YNwAwOcD7Jv9XTnCrQvp4xHc7PKwt12iFeXxK','963852741','2026-06-11 16:38:54','2026-07-11 15:42:20','94a5ed78-17a2-41c3-9f94-543db534e2a1','ACTIVO',NULL,NULL),('cb597ef3-a196-4f02-8b05-325f79256b07','María',' Pascual Orbegozo','h8c75tzitt@lnovic.com','$2a$10$DTyTVjc4lSW149Gh9BbtvOYjNIUE0qFRoo89WSnubf/ErDHuHl5LC','902527137','2026-07-11 15:10:49','2026-07-14 04:56:59','94a5ed78-17a2-41c3-9f94-543db534e2a1','ACTIVO',NULL,NULL),('ded1711f-65c5-11f1-9b81-0a0027000012','Elsa','Alarcón Sánchez','elsa.alarcon@hotmail.com','$2a$06$cMK3cs26D0D4RG0LNUleKO4Bv5Un5NL9EA0KPHzLfUZClTSC9ZKna','963852742','2026-06-11 18:46:34','2026-07-10 22:37:38','2c702009-b57d-49e2-b62e-80ada32e077a','ACTIVO',NULL,NULL);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-14  7:28:40
