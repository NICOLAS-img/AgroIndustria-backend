/* shop.js - Lógica del Carrito y Pasarela de Pagos */

let carrito = [];

// ==========================================
// 1. GESTIÓN DEL CARRITO (Agregar, Quitar, Modificar)
// ==========================================

function agregarAlCarrito(nombre, precio) {
    const existente = carrito.find(item => item.nombre === nombre);
    
    if (existente) {
        existente.cantidad++;
    } else {
        carrito.push({ nombre: nombre, precio: precio, cantidad: 1 });
    }
    
    actualizarVistaCarrito();
    
    // Feedback visual simple (Animación del botón)
    const btn = event.target.closest('button');
    if(btn) {
        const original = btn.innerHTML;
        const clasesOriginales = btn.className;
        
        btn.innerHTML = '<i class="bi bi-check2"></i> Agregado';
        btn.className = "btn btn-success rounded-pill w-auto px-3"; // Cambia temporalmente a verde
        
        setTimeout(() => {
            btn.innerHTML = original;
            btn.className = clasesOriginales; // Restaura el botón original
        }, 1000);
    }
}

function actualizarVistaCarrito() {
    const tbody = document.getElementById('tablaCarrito');
    const badge = document.getElementById('cartCount');
    const totalSpan = document.getElementById('cartTotal');
    const btnPagar = document.getElementById('btnIrPagar');
    
    // Limpiar tabla actual
    tbody.innerHTML = '';
    
    let total = 0;
    let itemsTotal = 0;

    if (carrito.length === 0) {
        // Mostrar mensaje si está vacío
        tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted py-4">Tu carrito está vacío 🛒</td></tr>';
        if(btnPagar) btnPagar.disabled = true;
    } else {
        if(btnPagar) btnPagar.disabled = false;
        
        // Generar filas por cada producto
        carrito.forEach((item, index) => {
            const subtotal = item.precio * item.cantidad;
            total += subtotal;
            itemsTotal += item.cantidad;

            tbody.insertAdjacentHTML('beforeend', `
                <tr>
                    <td>${item.nombre}</td>
                    <td>S/ ${item.precio.toFixed(2)}</td>
                    <td>
                        <div class="btn-group btn-group-sm">
                            <button class="btn btn-outline-secondary" onclick="cambiarCant(${index}, -1)">-</button>
                            <button class="btn btn-outline-secondary" disabled>${item.cantidad}</button>
                            <button class="btn btn-outline-secondary" onclick="cambiarCant(${index}, 1)">+</button>
                        </div>
                    </td>
                    <td class="fw-bold">S/ ${subtotal.toFixed(2)}</td>
                    <td><button class="btn btn-sm text-danger" onclick="eliminarItem(${index})"><i class="bi bi-trash"></i></button></td>
                </tr>
            `);
        });
    }

    // Actualizar contadores visuales
    if(badge) badge.innerText = itemsTotal;
    if(totalSpan) totalSpan.innerText = total.toFixed(2);
}

function cambiarCant(index, delta) {
    carrito[index].cantidad += delta;
    
    // Si la cantidad baja a 0 o menos, eliminar el item
    if (carrito[index].cantidad <= 0) {
        carrito.splice(index, 1);
    }
    actualizarVistaCarrito();
}

function eliminarItem(index) {
    carrito.splice(index, 1);
    actualizarVistaCarrito();
}

// ==========================================
// 2. PASARELA DE PAGO (SIMULACIÓN YAPE / TARJETA)
// ==========================================

function abrirPasarela() {
    // Cerrar modal carrito
    const modalCarritoEl = document.getElementById('modalCarrito');
    const modalCarrito = bootstrap.Modal.getInstance(modalCarritoEl);
    modalCarrito.hide();

    // Actualizar el total a pagar en el modal de pago
    const total = document.getElementById('cartTotal').innerText;
    document.getElementById('pagoTotal').innerText = "S/ " + total;

    // Abrir modal pago
    const modalPago = new bootstrap.Modal(document.getElementById('modalPago'));
    modalPago.show();

    // Iniciar simulación por defecto (Yape es la primera pestaña)
    iniciarSimulacionYape();
}

// LÓGICA DE YAPE
function iniciarSimulacionYape() {
    const status = document.getElementById('yapeStatus');
    const btn = document.getElementById('btnConfirmarYape');
    
    if(!status) return;

    // Resetear estado visual
    status.className = "mt-3 text-warning fw-bold small";
    status.innerHTML = '<span class="spinner-border spinner-border-sm"></span> Esperando escaneo del QR...';
    btn.disabled = true;

    // Simular detección de pago a los 4 segundos
    setTimeout(() => {
        // Verificar que el modal siga abierto para evitar errores si el usuario cerró
        if(document.getElementById('modalPago').classList.contains('show')) {
            status.className = "mt-3 text-success fw-bold small";
            status.innerHTML = '<i class="bi bi-check-circle-fill"></i> ¡Pago Detectado! (Juan Pérez)';
            btn.disabled = false; // Habilitar botón para finalizar
        }
    }, 4000);
}

// LÓGICA DE TARJETA
function procesarTarjeta() {
    const btn = document.getElementById('btnPayCard');
    
    btn.innerHTML = '<span class="spinner-border spinner-border-sm"></span> Procesando pago...';
    btn.disabled = true;

    setTimeout(() => {
        btn.innerHTML = '<i class="bi bi-check-lg"></i> Aprobado';
        btn.className = "btn btn-success w-100";
        setTimeout(() => finalizarCompra('TARJETA VISA **** 4242'), 1000);
    }, 2000);
}

// ==========================================
// 3. FINALIZAR COMPRA
// ==========================================

function finalizarCompra(metodo) {
    const total = document.getElementById('pagoTotal').innerText;
    const numPedido = Math.floor(Math.random() * 10000) + 1000;
    
    alert(`✅ ¡COMPRA EXITOSA!\n\n📦 Pedido Generado: #PED-${numPedido}\nMonto: ${total}\nMétodo: ${metodo}\n\nGracias por su preferencia. Le enviaremos el comprobante a su correo.`);
    
    // Limpiar carrito y recargar la página para una nueva compra
    carrito = [];
    actualizarVistaCarrito();
    location.reload();
}