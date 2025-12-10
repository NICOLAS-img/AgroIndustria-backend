/* shop.js - Carrito, Pagos y Redirección a Voucher */

let carrito = [];

// --- FUNCIONES DEL CARRITO ---
function agregarDesdeBoton(btn) {
    const id = parseInt(btn.getAttribute('data-id'));
    const nombre = btn.getAttribute('data-nombre');
    const precio = parseFloat(btn.getAttribute('data-precio'));
    agregarAlCarrito(id, nombre, precio, btn);
}

function agregarAlCarrito(id, nombre, precio, btnElement) {
    const existente = carrito.find(item => item.id === id);
    if (existente) { existente.cantidad++; } else { carrito.push({ id: id, nombre: nombre, precio: precio, cantidad: 1 }); }
    actualizarVistaCarrito();
    
    // Animación visual
    if(btnElement) {
        const original = btnElement.innerHTML;
        btnElement.innerHTML = '<i class="bi bi-check2"></i> Agregado';
        btnElement.className = "btn btn-success w-100 rounded-pill mt-2";
        setTimeout(() => { btnElement.innerHTML = original; btnElement.className = "btn btn-add w-100 rounded-pill mt-2"; }, 1000);
    }
}

function actualizarVistaCarrito() {
    const tbody = document.getElementById('tablaCarrito');
    const badge = document.getElementById('cartCount');
    const totalSpan = document.getElementById('cartTotal');
    const btnPagar = document.getElementById('btnIrPagar');
    if(!tbody) return;
    
    tbody.innerHTML = '';
    let total = 0; let itemsTotal = 0;

    if (carrito.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted">Carrito vacío</td></tr>';
        if(btnPagar) btnPagar.disabled = true;
    } else {
        if(btnPagar) btnPagar.disabled = false;
        carrito.forEach((item, index) => {
            total += item.precio * item.cantidad;
            itemsTotal += item.cantidad;
            tbody.insertAdjacentHTML('beforeend', `<tr><td>${item.nombre}</td><td>S/ ${item.precio.toFixed(2)}</td><td><button class="btn btn-sm btn-outline-secondary" onclick="cambiarCant(${index}, -1)">-</button> ${item.cantidad} <button class="btn btn-sm btn-outline-secondary" onclick="cambiarCant(${index}, 1)">+</button></td><td>S/ ${(item.precio*item.cantidad).toFixed(2)}</td><td><button class="btn btn-sm text-danger" onclick="eliminarItem(${index})">X</button></td></tr>`);
        });
    }
    if(badge) badge.innerText = itemsTotal;
    if(totalSpan) totalSpan.innerText = total.toFixed(2);
}

function cambiarCant(index, delta) {
    carrito[index].cantidad += delta;
    if (carrito[index].cantidad <= 0) carrito.splice(index, 1);
    actualizarVistaCarrito();
}
function eliminarItem(index) { carrito.splice(index, 1); actualizarVistaCarrito(); }

// --- LÓGICA DE PAGO ---

function abrirPasarela() {
    bootstrap.Modal.getInstance(document.getElementById('modalCarrito')).hide();
    document.getElementById('pagoTotal').innerText = "S/ " + document.getElementById('cartTotal').innerText;
    new bootstrap.Modal(document.getElementById('modalPago')).show();
    iniciarSimulacionYape();
}

function iniciarSimulacionYape() {
    const status = document.getElementById('yapeStatus');
    const btn = document.getElementById('btnConfirmarYape');
    
    status.className = "mt-3 text-warning fw-bold small";
    status.innerHTML = '<span class="spinner-border spinner-border-sm"></span> Esperando escaneo...';
    btn.disabled = true;

    setTimeout(() => {
        if(document.getElementById('modalPago').classList.contains('show')) {
            status.className = "mt-3 text-success fw-bold small";
            status.innerHTML = '<i class="bi bi-check-circle-fill"></i> ¡Pago Detectado!';
            btn.disabled = false;
        }
    }, 3000);
}

function finalizarCompra(metodo) {
    const totalTexto = document.getElementById('pagoTotal').innerText.replace('S/ ', '').trim();
    const tipoComprobante = document.getElementById('tipoComprobante').value;

    const orden = {
        total: parseFloat(totalTexto),
        // CORRECCIÓN 1: Enviamos null para que el backend use la dirección real del usuario
        direccion: null, 
        tipoComprobante: tipoComprobante, // BOLETA o FACTURA
        metodoPago: metodo,               // YAPE o TARJETA
        productos: carrito.map(i => ({ id: i.id, cantidad: i.cantidad }))
    };

    const btnId = metodo === 'YAPE' ? 'btnConfirmarYape' : 'btnPayCard';
    const btn = document.getElementById(btnId);
    if(btn) { btn.disabled = true; btn.innerText = "Procesando..."; }

    fetch('/pedidos/guardar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(orden)
    })
    .then(r => r.text())
    .then(resp => {
        if(resp === "OK") {
            const modalPagoEl = document.getElementById('modalPago');
            const modalPago = bootstrap.Modal.getInstance(modalPagoEl);
            if(modalPago) modalPago.hide();

            carrito = [];
            actualizarVistaCarrito();
            
            // REDIRECCIÓN AL VOUCHER
            window.location.href = "/compra-exitosa"; 
            
        } else {
            alert("❌ Error: " + resp);
            if(btn) { btn.disabled = false; btn.innerText = metodo === 'YAPE' ? "CONFIRMAR PAGO" : "PAGAR AHORA"; }
        }
    })
    .catch(err => {
        console.error(err);
        alert("Error de conexión");
        if(btn) btn.disabled = false;
    });
}

function procesarTarjeta() { finalizarCompra('TARJETA'); }

function irALogin() {
    alert("🔒 Para añadir productos, primero debes iniciar sesión.");
    window.location.href = "/login";
}