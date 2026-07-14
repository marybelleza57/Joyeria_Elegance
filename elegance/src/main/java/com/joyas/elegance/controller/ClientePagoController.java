package com.joyas.elegance.controller;

import com.joyas.elegance.model.Clientes;
import com.joyas.elegance.model.Pedido;
import com.joyas.elegance.model.DetallePedido;
import com.joyas.elegance.model.EstadoPedido;
import com.joyas.elegance.model.NotaCredito;
import com.joyas.elegance.repository.ClientesRepositorio;
import com.joyas.elegance.repository.PedidoRepositorio;
import com.joyas.elegance.repository.DetallePedidoRepositorio;
import com.joyas.elegance.repository.NotaCreditoRepositorio;
import com.joyas.elegance.repository.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@RestController
public class ClientePagoController {

    private final ClientesRepositorio clientesRepositorio;
    private final PedidoRepositorio pedidoRepositorio;
    private final DetallePedidoRepositorio detallePedidoRepositorio;
    private final NotaCreditoRepositorio notaCreditoRepositorio;
    private final ProductoRepositorio productoRepositorio;

    @Autowired
    public ClientePagoController(ClientesRepositorio clientesRepositorio,
            PedidoRepositorio pedidoRepositorio,
            DetallePedidoRepositorio detallePedidoRepositorio,
            NotaCreditoRepositorio notaCreditoRepositorio,
            ProductoRepositorio productoRepositorio) {
        this.clientesRepositorio = clientesRepositorio;
        this.pedidoRepositorio = pedidoRepositorio;
        this.detallePedidoRepositorio = detallePedidoRepositorio;
        this.notaCreditoRepositorio = notaCreditoRepositorio;
        this.productoRepositorio = productoRepositorio;
    }

    @GetMapping("/api/notas-credito/validar")
    public ResponseEntity<?> validarNotaCredito(@RequestParam String codigo, HttpSession session) {
        Clientes clienteLogueado = (Clientes) session.getAttribute("usuarioLogueado");
        if (clienteLogueado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Debe iniciar sesión."));
        }

        java.util.Optional<NotaCredito> optNC = notaCreditoRepositorio.findByCodigo(codigo.trim());
        if (optNC.isEmpty()) {
            return ResponseEntity
                    .ok(Map.of("success", false, "message", "El código de Nota de Crédito no existe o es inválido."));
        }

        NotaCredito nc = optNC.get();
        if (!nc.getCliente().getId().equals(clienteLogueado.getId())) {
            return ResponseEntity
                    .ok(Map.of("success", false, "message", "Esta Nota de Crédito no pertenece a su cuenta."));
        }

        if (!"ACTIVO".equals(nc.getEstado()) && !"USADO_PARCIAL".equals(nc.getEstado())) {
            return ResponseEntity.ok(Map.of("success", false, "message",
                    "La Nota de Crédito ya ha sido utilizada por completo o ha expirado. Estado: " + nc.getEstado()));
        }

        if (nc.getMontoDisponible().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity
                    .ok(Map.of("success", false, "message", "La Nota de Crédito no tiene saldo disponible."));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "codigo", nc.getCodigo(),
                "montoDisponible", nc.getMontoDisponible()));
    }

    @PostMapping("/cliente/pedido/checkout")
    public ResponseEntity<?> checkout(@RequestParam String direccionEnvio,
            @RequestParam String telefonoContacto,
            @RequestParam(required = false) String notas,
            @RequestParam String itemsJson,
            @RequestParam(required = false) String codigoNotaCredito,
            HttpSession session) {
        Clientes clienteLogueado = (Clientes) session.getAttribute("usuarioLogueado");
        if (clienteLogueado == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Debe iniciar sesión para realizar un pedido.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        try {
            java.util.Optional<Clientes> optCliente = clientesRepositorio.findById(clienteLogueado.getId());
            if (optCliente.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Cliente no encontrado.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            Clientes cliente = optCliente.get();

            org.springframework.boot.json.JsonParser parser = org.springframework.boot.json.JsonParserFactory
                    .getJsonParser();
            List<Object> rawItems = parser.parseList(itemsJson);
            if (rawItems == null || rawItems.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "El carrito de compras está vacío.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            NotaCredito nc = null;
            if (codigoNotaCredito != null && !codigoNotaCredito.trim().isEmpty()) {
                java.util.Optional<NotaCredito> optNC = notaCreditoRepositorio.findByCodigo(codigoNotaCredito.trim());
                if (optNC.isPresent()) {
                    NotaCredito tempNc = optNC.get();
                    if (tempNc.getCliente().getId().equals(cliente.getId()) &&
                            ("ACTIVO".equals(tempNc.getEstado()) || "USADO_PARCIAL".equals(tempNc.getEstado())) &&
                            tempNc.getMontoDisponible().compareTo(BigDecimal.ZERO) > 0) {
                        nc = tempNc;
                    }
                }
            }

            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setDireccionEnvio(direccionEnvio);
            pedido.setTelefonoContacto(telefonoContacto.trim().isEmpty() ? cliente.getTelefono() : telefonoContacto);
            pedido.setNotas(notas);
            pedido.setEstado(EstadoPedido.PENDIENTE);

            String numeroPedido = "PE-" + String.format("%06d", pedidoRepositorio.count() + 1);
            pedido.setNumeroPedido(numeroPedido);

            BigDecimal total = BigDecimal.ZERO;
            pedido.setTotal(total);

            pedido = pedidoRepositorio.save(pedido);

            for (Object rawItem : rawItems) {
                Map<String, Object> item = (Map<String, Object>) rawItem;
                String productoIdStr = (String) item.get("id");
                Object cantidadObj = item.get("cantidad");
                int cantidad = 0;
                if (cantidadObj instanceof Number) {
                    cantidad = ((Number) cantidadObj).intValue();
                }
                if (cantidad <= 0)
                    continue;

                java.util.Optional<com.joyas.elegance.model.Producto> optProducto = productoRepositorio
                        .findById(UUID.fromString(productoIdStr));
                if (optProducto.isPresent()) {
                    com.joyas.elegance.model.Producto producto = optProducto.get();
                    DetallePedido detalle = new DetallePedido();
                    detalle.setPedido(pedido);
                    detalle.setProducto(producto);
                    detalle.setCantidad(cantidad);
                    detalle.setPrecioUnitario(producto.getPrecio());
                    detallePedidoRepositorio.save(detalle);

                    BigDecimal subtotal = producto.getPrecio().multiply(new BigDecimal(cantidad));
                    total = total.add(subtotal);
                }
            }

            BigDecimal totalFinal = total;
            BigDecimal descuentoAplicado = BigDecimal.ZERO;
            if (nc != null) {
                BigDecimal saldoDisponible = nc.getMontoDisponible();
                if (saldoDisponible.compareTo(total) >= 0) {
                    descuentoAplicado = total;
                    nc.setMontoDisponible(saldoDisponible.subtract(total));
                    if (nc.getMontoDisponible().compareTo(BigDecimal.ZERO) == 0) {
                        nc.setEstado("USADO_TOTAL");
                    } else {
                        nc.setEstado("USADO_PARCIAL");
                    }
                    totalFinal = BigDecimal.ZERO;
                } else {
                    descuentoAplicado = saldoDisponible;
                    nc.setMontoDisponible(BigDecimal.ZERO);
                    nc.setEstado("USADO_TOTAL");
                    totalFinal = total.subtract(saldoDisponible);
                }
                notaCreditoRepositorio.save(nc);

                String infoNC = "[Nota de Crédito aplicada: " + nc.getCodigo() + ", Descuento: S/. "
                        + descuentoAplicado.setScale(2, java.math.RoundingMode.HALF_UP) + "] ";
                if (notas != null && !notas.trim().isEmpty()) {
                    pedido.setNotas(infoNC + "| " + notas);
                } else {
                    pedido.setNotas(infoNC);
                }
            }

            pedido.setTotal(totalFinal);
            pedido = pedidoRepositorio.save(pedido);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("pedidoId", pedido.getId().toString());
            response.put("numeroPedido", pedido.getNumeroPedido());
            response.put("total", totalFinal);
            if (nc != null) {
                response.put("codigoNotaCredito", nc.getCodigo());
                response.put("descuento", descuentoAplicado);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al procesar el pedido: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}