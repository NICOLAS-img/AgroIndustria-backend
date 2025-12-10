/* clientes.js - VERSIÓN FINAL (Con Edición de Documento) */

document.addEventListener("DOMContentLoaded", function() {
    cargarClientes(); // Cargar la lista al entrar

    const form = document.getElementById('formCliente');
    
    // --- EVENTO: GUARDAR CAMBIOS (EDITAR) ---
    if(form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Obtenemos el ID del cliente que estamos editando
            const id = document.getElementById('cliId').value;
            
            // Primero obtenemos el cliente original para no perder datos sensibles
            fetch(`/api/clientes/${id}`)
            .then(res => res.json())
            .then(clienteOriginal => {
                
                // 1. Actualizamos datos básicos
                clienteOriginal.nombre = form.querySelector('input[name="nombre"]').value;
                clienteOriginal.apellido = form.querySelector('input[name="apellido"]').value;
                
                // 2. ACTUALIZAMOS LOS NUEVOS CAMPOS DE DOCUMENTO
                clienteOriginal.tipoDocumento = form.querySelector('select[name="tipoDocumento"]').value;
                clienteOriginal.numeroDocumento = form.querySelector('input[name="numeroDocumento"]').value;

                // 3. Actualizamos contacto
                clienteOriginal.telefono = form.querySelector('input[name="telefono"]').value;
                clienteOriginal.direccion = form.querySelector('input[name="direccion"]').value;
                
                // NOTA: El correo y la contraseña NO se tocan aquí

                // Enviamos el objeto actualizado al servidor
                return fetch('/api/clientes', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(clienteOriginal)
                });
            })
            .then(res => res.text())
            .then(resp => {
                if(resp === "OK") {
                    // Cerrar modal y recargar tabla
                    const modalEl = document.getElementById('modalCliente');
                    const modal = bootstrap.Modal.getInstance(modalEl);
                    modal.hide();
                    
                    alert("✅ Cliente actualizado correctamente.");
                    cargarClientes();
                } else {
                    alert("❌ Error al guardar: " + resp);
                }
            })
            .catch(err => console.error("Error:", err));
        });
    }
});

// --- FUNCIÓN: LISTAR CLIENTES ---
function cargarClientes() {
    fetch('/api/clientes')
    .then(res => res.json())
    .then(data => {
        const tbody = document.getElementById('tablaClientesBody');
        tbody.innerHTML = ''; // Limpiar tabla antes de pintar
        
        data.forEach(cli => {
            // VALIDACIÓN BLINDADA
            const nombre = cli.nombre || '';
            const apellido = cli.apellido || '';
            
            // Buscamos 'tipoDocumento' (Java Nuevo) O 'tipo_documento' (Java Viejo)
            const docTipo = cli.tipoDocumento || cli.tipo_documento || 'Doc';
            const docNum = cli.numeroDocumento || cli.numero_documento || '-';
            
            const tel = cli.telefono || 'Sin teléfono';
            const dir = cli.direccion || 'Sin dirección';
            
            // Aseguramos el ID
            const idReal = cli.id || cli.cliente_id;

            tbody.insertAdjacentHTML('beforeend', `
                <tr>
                    <td class="ps-4 fw-bold text-secondary">#${idReal}</td>
                    <td>
                        <div class="d-flex align-items-center">
                            <div class="cliente-avatar" style="width:35px;height:35px;background:#e9ecef;border-radius:50%;display:flex;align-items:center;justify-content:center;margin-right:10px;font-weight:bold;color:#495057;">
                                ${nombre.charAt(0)}${apellido.charAt(0)}
                            </div>
                            <div>
                                <div class="fw-bold text-dark">${nombre} ${apellido}</div>
                                <small class="text-muted" style="font-size: 0.8rem;">Registrado</small>
                            </div>
                        </div>
                    </td>
                    <td>
                        <span class="badge bg-light text-dark border">${docTipo}: ${docNum}</span>
                    </td>
                    <td>
                        <div class="d-flex flex-column">
                            <span><i class="bi bi-envelope-at text-primary me-1"></i> ${cli.correo}</span>
                            <small class="text-muted"><i class="bi bi-phone me-1"></i> ${tel}</small>
                        </div>
                    </td>
                    <td class="text-truncate" style="max-width: 150px;" title="${dir}">
                        ${dir}
                    </td>
                    <td class="text-end pe-4">
                        <button class="btn btn-sm btn-outline-primary me-1" onclick="editarCliente(${idReal})" title="Editar">
                            <i class="bi bi-pencil"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" onclick="eliminarCliente(${idReal})" title="Eliminar">
                            <i class="bi bi-trash"></i>
                        </button>
                    </td>
                </tr>
            `);
        });
    })
    .catch(err => console.error("Error cargando clientes:", err));
}

// --- FUNCIÓN: PREPARAR EDICIÓN (LLENAR MODAL) ---
function editarCliente(id) {
    fetch(`/api/clientes/${id}`)
    .then(res => res.json())
    .then(cli => {
        // Llenamos el modal con los datos actuales
        document.getElementById('cliId').value = cli.id || cli.cliente_id;
        const form = document.getElementById('formCliente');
        
        form.querySelector('input[name="nombre"]').value = cli.nombre;
        form.querySelector('input[name="apellido"]').value = cli.apellido;
        
        // --- AQUÍ LLENAMOS LOS CAMPOS DE DOCUMENTO ---
        // Usamos la lógica blindada (nuevo || antiguo || defecto)
        form.querySelector('select[name="tipoDocumento"]').value = cli.tipoDocumento || cli.tipo_documento || 'DNI';
        form.querySelector('input[name="numeroDocumento"]').value = cli.numeroDocumento || cli.numero_documento || '';
        
        form.querySelector('input[name="telefono"]').value = cli.telefono;
        form.querySelector('input[name="direccion"]').value = cli.direccion;
        form.querySelector('input[name="correo"]').value = cli.correo;
        
        // Abrir el modal
        new bootstrap.Modal(document.getElementById('modalCliente')).show();
    });
}

// --- FUNCIÓN: ELIMINAR ---
function eliminarCliente(id) {
    if(confirm("¿Estás seguro de eliminar a este cliente?\n⚠️ Si tiene compras registradas, no se podrá eliminar.")) {
        fetch(`/api/clientes/${id}`, { method: 'DELETE' })
        .then(res => res.text())
        .then(resp => {
            if(resp === "OK") {
                alert("🗑️ Cliente eliminado.");
                cargarClientes();
            } else {
                alert("⚠️ " + resp);
            }
        });
    }
}