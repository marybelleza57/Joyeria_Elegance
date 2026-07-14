document.addEventListener("DOMContentLoaded", () => {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    let appliedNotaCreditoCode = null;

    const updateCartCount = () => {
        const totalItems = cart.reduce((sum, item) => sum + item.cantidad, 0);
        const badges = document.querySelectorAll('#cart-count');
        badges.forEach(badge => {
            badge.textContent = totalItems;
        });
    };

    const saveCart = () => {
        localStorage.setItem('cart', JSON.stringify(cart));
        updateCartCount();
    };

    document.addEventListener('click', (e) => {
        const btn = e.target.closest('.btn-carrito');
        if (btn) {
            e.preventDefault();
            const id = btn.getAttribute('data-id');
            const nombre = btn.getAttribute('data-nombre');
            const precio = parseFloat(btn.getAttribute('data-precio') || '0');
            const imagen = btn.getAttribute('data-imagen');
            const material = btn.getAttribute('data-material');

            if (id) {
                const existingItem = cart.find(item => item.id === id);
                if (existingItem) {
                    existingItem.cantidad += 1;
                } else {
                    cart.push({ id, nombre, precio, imagen, material, cantidad: 1 });
                }

                saveCart();

                const originalText = btn.innerHTML;
                btn.innerHTML = '<i class="fas fa-check me-1"></i> ¡Añadido!';
                btn.classList.add('bg-success', 'text-white');
                btn.disabled = true;

                setTimeout(() => {
                    btn.innerHTML = originalText;
                    btn.classList.remove('bg-success', 'text-white');
                    btn.disabled = false;
                }, 1200);
            }
        }
    });

    const modalCarrito = document.getElementById('modalCarrito');
    if (modalCarrito) {
        const renderCart = () => {
            const emptyMsg = document.getElementById('carrito-vacio-msg');
            const hasItemsDiv = document.getElementById('carrito-con-items');
            const tbody = document.getElementById('carrito-tbody');
            const totalMonto = document.getElementById('carrito-total-monto');

            if (!emptyMsg || !hasItemsDiv || !tbody || !totalMonto) return;

            if (cart.length === 0) {
                emptyMsg.style.display = 'block';
                hasItemsDiv.style.display = 'none';
            } else {
                emptyMsg.style.display = 'none';
                hasItemsDiv.style.display = 'block';
                tbody.innerHTML = '';

                let grandTotal = 0;
                cart.forEach((item, index) => {
                    const subtotal = item.precio * item.cantidad;
                    grandTotal += subtotal;

                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td>
                            <div class="d-flex align-items-center">
                                <img src="${item.imagen || '/img/anillo-diamante.jpg'}" alt="${item.nombre}" class="img-thumbnail me-3" style="width: 50px; height: 50px; object-fit: cover;">
                                <div>
                                    <h6 class="mb-0 fw-bold text-dark small">${item.nombre}</h6>
                                </div>
                            </div>
                        </td>
                        <td class="text-muted small">${item.material || 'Oro'}</td>
                        <td class="small">S/. ${item.precio.toFixed(2)}</td>
                        <td>
                            <div class="input-group input-group-sm" style="width: 100px;">
                                <button class="btn btn-outline-secondary btn-minus" data-index="${index}" type="button">-</button>
                                <input type="text" class="form-control text-center input-qty" data-index="${index}" value="${item.cantidad}" readonly style="font-size: 0.75rem;">
                                <button class="btn btn-outline-secondary btn-plus" data-index="${index}" type="button">+</button>
                            </div>
                        </td>
                        <td class="fw-bold text-dark small">S/. ${subtotal.toFixed(2)}</td>
                        <td class="text-end">
                            <button class="btn btn-sm btn-link text-danger btn-remove" data-index="${index}" type="button">
                                <i class="fas fa-trash"></i>
                            </button>
                        </td>
                    `;
                    tbody.appendChild(tr);
                });

                totalMonto.textContent = `S/. ${grandTotal.toFixed(2)}`;
            }
        };

        modalCarrito.addEventListener('show.bs.modal', renderCart);

        modalCarrito.addEventListener('click', (e) => {
            const btnMinus = e.target.closest('.btn-minus');
            const btnPlus = e.target.closest('.btn-plus');
            const btnRemove = e.target.closest('.btn-remove');

            if (btnMinus) {
                const idx = parseInt(btnMinus.getAttribute('data-index'));
                if (cart[idx] && cart[idx].cantidad > 1) {
                    cart[idx].cantidad -= 1;
                    saveCart();
                    renderCart();
                }
            } else if (btnPlus) {
                const idx = parseInt(btnPlus.getAttribute('data-index'));
                if (cart[idx]) {
                    cart[idx].cantidad += 1;
                    saveCart();
                    renderCart();
                }
            } else if (btnRemove) {
                const idx = parseInt(btnRemove.getAttribute('data-index'));
                if (cart[idx]) {
                    cart.splice(idx, 1);
                    saveCart();
                    renderCart();
                }
            }
        });

        const btnProcederPago = document.getElementById('btn-proceder-pago');
        if (btnProcederPago) {
            btnProcederPago.addEventListener('click', () => {
                const userLoggedState = document.getElementById('user-logged-in-state');
                const isLoggedIn = userLoggedState && userLoggedState.getAttribute('data-logged') === 'true';

                const modalCarritoInstance = bootstrap.Modal.getInstance(modalCarrito);
                if (modalCarritoInstance) {
                    modalCarritoInstance.hide();
                }

                if (!isLoggedIn) {
                    const loginModalEl = document.getElementById('modalLogin');
                    if (loginModalEl) {
                        const modalLogin = new bootstrap.Modal(loginModalEl);
                        modalLogin.show();
                    } else {
                        alert('Debe iniciar sesión para proceder al pago.');
                    }
                } else {
                    const isComplete = userLoggedState && userLoggedState.getAttribute('data-completo') === 'true';
                    if (!isComplete) {
                        const modalCompletarEl = document.getElementById('modalCompletarDatos');
                        if (modalCompletarEl) {
                            const modalCompletar = new bootstrap.Modal(modalCompletarEl);
                            modalCompletar.show();
                        } else {
                            alert('Para proceder al pago, por favor complete todos sus datos personales (nombres, apellidos, teléfono, dirección, departamento y provincia) en su perfil.');
                            window.location.href = '/cliente/configuracion';
                        }
                    } else {
                        const modalPagoEl = document.getElementById('modalPago');
                        if (modalPagoEl) {
                            appliedNotaCreditoCode = null;
                            const inputNC = document.getElementById('inputNotaCredito');
                            if (inputNC) {
                                inputNC.value = '';
                                inputNC.disabled = false;
                            }
                            const btnNC = document.getElementById('btnAplicarNotaCredito');
                            if (btnNC) {
                                btnNC.disabled = false;
                                btnNC.textContent = 'Aplicar';
                                btnNC.className = 'btn btn-dark fw-bold text-uppercase';
                            }
                            const msgNC = document.getElementById('nc-status-message');
                            if (msgNC) {
                                msgNC.className = 'small mt-1 d-none';
                                msgNC.textContent = '';
                            }
                            const alertCompleto = document.getElementById('alert-pago-completo');
                            if (alertCompleto) {
                                alertCompleto.classList.add('d-none');
                            }
                            const qrContainer = document.getElementById('qr-yape-container');
                            if (qrContainer) {
                                qrContainer.classList.remove('d-none');
                            }
                            const titleInstrucciones = document.getElementById('pago-instrucciones-titulo');
                            if (titleInstrucciones) {
                                titleInstrucciones.classList.remove('d-none');
                            }
                            const btnConfirmar = document.getElementById('btn-confirmar-yape');
                            if (btnConfirmar) {
                                btnConfirmar.className = 'btn btn-success w-100 py-2.5 fw-bold shadow-sm';
                                btnConfirmar.innerHTML = '<i class="fab fa-whatsapp me-2"></i>Confirmar Pedido y Enviar WhatsApp';
                            }

                            const totalMonto = cart.reduce((sum, item) => sum + (item.precio * item.cantidad), 0);
                            document.getElementById('pago-total-monto').textContent = `S/. ${totalMonto.toFixed(2)}`;

                            const modalPago = new bootstrap.Modal(modalPagoEl);
                            modalPago.show();
                        }
                    }
                }
            });
        }
    }

    const formCheckout = document.getElementById('formCheckout');
    if (formCheckout) {
        formCheckout.addEventListener('submit', (e) => {
            e.preventDefault();
            e.stopPropagation();

            if (!formCheckout.checkValidity()) {
                formCheckout.classList.add('was-validated');
                return;
            }

            const btnConfirmar = document.getElementById('btn-confirmar-yape');
            btnConfirmar.disabled = true;
            btnConfirmar.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Procesando...';

            const direccion = document.getElementById('pagoDireccion').value;
            const telefono = document.getElementById('pagoTelefono').value;
            const notas = document.getElementById('pagoNotas').value;
            const itemsJson = JSON.stringify(cart);

            const formData = new URLSearchParams();
            formData.append('direccionEnvio', direccion);
            formData.append('telefonoContacto', telefono);
            formData.append('notas', notas);
            formData.append('itemsJson', itemsJson);
            if (appliedNotaCreditoCode) {
                formData.append('codigoNotaCredito', appliedNotaCreditoCode);
            }

            fetch('/cliente/pedido/checkout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: formData.toString()
            })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        cart = [];
                        saveCart();

                        const modalPagoEl = document.getElementById('modalPago');
                        const modalPagoInstance = bootstrap.Modal.getInstance(modalPagoEl);
                        if (modalPagoInstance) {
                            modalPagoInstance.hide();
                        }

                        const wasZero = parseFloat(data.total) === 0;
                        if (wasZero) {
                            alert(`¡Pedido ${data.numeroPedido} registrado con éxito!`);
                            window.location.href = '/cliente/configuracion?tab=pedidos';
                        } else {
                            const waText = `Hola Joyería Elegante, aquí envío el comprobante de pago de mi pedido ${data.numeroPedido} por un total de S/. ${parseFloat(data.total).toFixed(2)}. Adjunto la captura de Yape.`;
                            const waUrl = `https://wa.me/51996037711?text=${encodeURIComponent(waText)}`;

                            alert(`¡Pedido ${data.numeroPedido} registrado con éxito!\n\nRedirigiendo a WhatsApp para enviar el comprobante de pago.`);

                            window.open(waUrl, '_blank');

                            window.location.href = '/cliente/configuracion?tab=pedidos';
                        }
                    } else {
                        alert('Error: ' + data.message);
                        btnConfirmar.disabled = false;
                        const isZeroPaying = parseFloat(document.getElementById('pago-total-monto').textContent.replace('S/. ', '')) === 0;
                        if (appliedNotaCreditoCode && isZeroPaying) {
                            btnConfirmar.innerHTML = '<i class="fas fa-check-circle me-2"></i>Confirmar y Registrar Pedido';
                        } else {
                            btnConfirmar.innerHTML = '<i class="fab fa-whatsapp me-2"></i>Confirmar Pedido y Enviar WhatsApp';
                        }
                    }
                })
                .catch(err => {
                    console.error(err);
                    alert('Ocurrió un error al procesar el pedido. Inténtelo de nuevo.');
                    btnConfirmar.disabled = false;
                    const isZeroPaying = parseFloat(document.getElementById('pago-total-monto').textContent.replace('S/. ', '')) === 0;
                    if (appliedNotaCreditoCode && isZeroPaying) {
                        btnConfirmar.innerHTML = '<i class="fas fa-check-circle me-2"></i>Confirmar y Registrar Pedido';
                    } else {
                        btnConfirmar.innerHTML = '<i class="fab fa-whatsapp me-2"></i>Confirmar Pedido y Enviar WhatsApp';
                    }
                });
        });
    }

    const btnAplicarNC = document.getElementById('btnAplicarNotaCredito');
    if (btnAplicarNC) {
        btnAplicarNC.addEventListener('click', () => {
            const inputNC = document.getElementById('inputNotaCredito');
            const msgNC = document.getElementById('nc-status-message');
            const code = inputNC.value.trim();

            msgNC.className = 'small mt-1 d-none';
            msgNC.textContent = '';

            if (!code) {
                msgNC.textContent = 'Por favor ingrese un código.';
                msgNC.className = 'small mt-1 text-danger d-block';
                return;
            }

            btnAplicarNC.disabled = true;
            btnAplicarNC.textContent = 'Validando...';

            fetch(`/api/notas-credito/validar?codigo=${encodeURIComponent(code)}`)
                .then(res => {
                    if (!res.ok) {
                        return res.json().then(err => {
                            throw new Error(err.message || 'Código no válido');
                        });
                    }
                    return res.json();
                })
                .then(data => {
                    if (data.success) {
                        appliedNotaCreditoCode = data.codigo;
                        const montoDisponible = parseFloat(data.montoDisponible) || 0;

                        msgNC.textContent = `Nota de crédito aplicada. Saldo disponible: S/. ${montoDisponible.toFixed(2)}`;
                        msgNC.className = 'small mt-1 text-success d-block';

                        btnAplicarNC.disabled = true;
                        btnAplicarNC.textContent = 'Aplicado';
                        inputNC.disabled = true;
                        inputNC.classList.add('border-success');

                        const totalMontoOriginal = cart.reduce((sum, item) => sum + (item.precio * item.cantidad), 0);
                        const nuevoTotal = Math.max(0, totalMontoOriginal - montoDisponible);
                        document.getElementById('pago-total-monto').textContent = `S/. ${nuevoTotal.toFixed(2)}`;

                        document.getElementById('pago-total-monto').dataset.totalConDescuento = nuevoTotal;

                        const alertCompleto = document.getElementById('alert-pago-completo');
                        const qrContainer = document.getElementById('qr-yape-container');
                        const titleInstrucciones = document.getElementById('pago-instrucciones-titulo');
                        const btnConfirmar = document.getElementById('btn-confirmar-yape');

                        if (nuevoTotal === 0) {
                            if (qrContainer) qrContainer.classList.add('d-none');
                            if (titleInstrucciones) titleInstrucciones.classList.add('d-none');
                            if (alertCompleto) alertCompleto.classList.remove('d-none');
                            if (btnConfirmar) {
                                btnConfirmar.className = 'btn btn-dark w-100 py-2.5 fw-bold shadow-sm';
                                btnConfirmar.innerHTML = '<i class="fas fa-check-circle me-2"></i>Confirmar y Registrar Pedido';
                            }
                        } else {
                            if (qrContainer) qrContainer.classList.remove('d-none');
                            if (titleInstrucciones) titleInstrucciones.classList.remove('d-none');
                            if (alertCompleto) alertCompleto.classList.add('d-none');
                            if (btnConfirmar) {
                                btnConfirmar.className = 'btn btn-success w-100 py-2.5 fw-bold shadow-sm';
                                btnConfirmar.innerHTML = '<i class="fab fa-whatsapp me-2"></i>Confirmar Pedido y Enviar WhatsApp';
                            }
                        }
                    } else {
                        appliedNotaCreditoCode = null;
                        msgNC.textContent = `${data.message || 'Código inválido.'}`;
                        msgNC.className = 'small mt-1 text-danger d-block';
                        btnAplicarNC.disabled = false;
                        btnAplicarNC.textContent = 'Aplicar';
                        inputNC.classList.remove('border-success');

                        const totalMontoOriginal = cart.reduce((sum, item) => sum + (item.precio * item.cantidad), 0);
                        document.getElementById('pago-total-monto').textContent = `S/. ${totalMontoOriginal.toFixed(2)}`;

                        const alertCompleto = document.getElementById('alert-pago-completo');
                        const qrContainer = document.getElementById('qr-yape-container');
                        const titleInstrucciones = document.getElementById('pago-instrucciones-titulo');
                        const btnConfirmar = document.getElementById('btn-confirmar-yape');

                        if (qrContainer) qrContainer.classList.remove('d-none');
                        if (titleInstrucciones) titleInstrucciones.classList.remove('d-none');
                        if (alertCompleto) alertCompleto.classList.add('d-none');
                        if (btnConfirmar) {
                            btnConfirmar.className = 'btn btn-success w-100 py-2.5 fw-bold shadow-sm';
                            btnConfirmar.innerHTML = '<i class="fab fa-whatsapp me-2"></i>Confirmar Pedido y Enviar WhatsApp';
                        }
                    }
                })
                .catch(err => {
                    console.error('Error al validar nota de crédito:', err);
                    appliedNotaCreditoCode = null;
                    msgNC.textContent = `${err.message || 'Error al validar la nota de crédito.'}`;
                    msgNC.className = 'small mt-1 text-danger d-block';
                    btnAplicarNC.disabled = false;
                    btnAplicarNC.textContent = 'Aplicar';
                    inputNC.classList.remove('border-success');

                    const totalMontoOriginal = cart.reduce((sum, item) => sum + (item.precio * item.cantidad), 0);
                    document.getElementById('pago-total-monto').textContent = `S/. ${totalMontoOriginal.toFixed(2)}`;

                    const alertCompleto = document.getElementById('alert-pago-completo');
                    const qrContainer = document.getElementById('qr-yape-container');
                    const titleInstrucciones = document.getElementById('pago-instrucciones-titulo');
                    const btnConfirmar = document.getElementById('btn-confirmar-yape');

                    if (qrContainer) qrContainer.classList.remove('d-none');
                    if (titleInstrucciones) titleInstrucciones.classList.remove('d-none');
                    if (alertCompleto) alertCompleto.classList.add('d-none');
                    if (btnConfirmar) {
                        btnConfirmar.className = 'btn btn-success w-100 py-2.5 fw-bold shadow-sm';
                        btnConfirmar.innerHTML = '<i class="fab fa-whatsapp me-2"></i>Confirmar Pedido y Enviar WhatsApp';
                    }
                });
        });
    }

    // Toggle password visibility for login and register modals
    document.addEventListener('click', function (e) {
        const toggleLogin = e.target.closest('#togglePasswordLoginCliente');
        if (toggleLogin) {
            const input = document.getElementById('inputPasswordLoginCliente');
            const icon = document.getElementById('eyeIconLoginCliente');
            if (input.type === 'password') {
                input.type = 'text';
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            } else {
                input.type = 'password';
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            }
            return;
        }

        const toggleRegistro = e.target.closest('#togglePasswordRegCliente');
        if (toggleRegistro) {
            const input = document.getElementById('inputPasswordRegCliente');
            const icon = document.getElementById('eyeIconRegCliente');
            if (input.type === 'password') {
                input.type = 'text';
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            } else {
                input.type = 'password';
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            }
            return;
        }
    });

    document.addEventListener('click', (e) => {
        const qrImg = e.target.closest('.qr-zoomable');
        if (qrImg) {
            e.preventDefault();

            const overlay = document.createElement('div');
            overlay.style.position = 'fixed';
            overlay.style.top = '0';
            overlay.style.left = '0';
            overlay.style.width = '100%';
            overlay.style.height = '100%';
            overlay.style.backgroundColor = 'rgba(0, 0, 0, 0.85)';
            overlay.style.backdropFilter = 'blur(6px)';
            overlay.style.display = 'flex';
            overlay.style.flexDirection = 'column';
            overlay.style.justifyContent = 'center';
            overlay.style.alignItems = 'center';
            overlay.style.zIndex = '99999';
            overlay.style.opacity = '0';
            overlay.style.transition = 'opacity 0.25s ease';
            overlay.style.cursor = 'zoom-out';

            const img = document.createElement('img');
            img.src = qrImg.src;
            img.style.maxWidth = '90%';
            img.style.maxHeight = '75vh';
            img.style.borderRadius = '12px';
            img.style.border = '6px solid white';
            img.style.boxShadow = '0 10px 30px rgba(0, 0, 0, 0.5)';
            img.style.transition = 'transform 0.25s ease';
            img.style.transform = 'scale(0.9)';

            const text = document.createElement('div');
            text.textContent = 'Haz click en cualquier lugar para cerrar';
            text.style.color = '#fff';
            text.style.marginTop = '20px';
            text.style.fontSize = '0.9rem';
            text.style.fontFamily = 'sans-serif';
            text.style.opacity = '0.7';

            overlay.appendChild(img);
            overlay.appendChild(text);
            document.body.appendChild(overlay);

            overlay.offsetHeight;
            overlay.style.opacity = '1';
            img.style.transform = 'scale(1)';

            overlay.addEventListener('click', () => {
                overlay.style.opacity = '0';
                img.style.transform = 'scale(0.9)';
                setTimeout(() => {
                    overlay.remove();
                }, 250);
            });
        }
    });

    updateCartCount();
});