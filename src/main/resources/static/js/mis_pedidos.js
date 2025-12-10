/* mis_pedidos.js - Lógica para ver detalles de compra */

function verDetalle(idPedido) {
    const tbody = document.getElementById('listaDetalles');
    
    // 1. Mostrar estado de carga
    tbody.innerHTML = '<tr><td colspan="3" class="text-center py-3">Cargando productos...</td></tr>';
    
    // 2. Abrir el Modal (usando Bootstrap)
    const modal = new bootstrap.Modal(document.getElementById('modalDetalle'));
    modal.show();

    // 3. Pedir los datos al servidor
    fetch('/api/pedido/' + idPedido + '/detalles')
        .then(res => res.json())
        .then(detalles => {
            tbody.innerHTML = ''; // Limpiar la tabla
            
            if(detalles.length === 0) {
                tbody.innerHTML = '<tr><td colspan="3" class="text-center">No hay detalles disponibles.</td></tr>';
                return;
            }

            // 4. Recorrer los productos y pintarlos
            detalles.forEach(d => {
                // --- CORRECCIÓN DEL ERROR NaN / Undefined ---
                // Si el precio histórico (d.precio) no existe, usamos el precio actual del producto
                const precioUnitario = d.precio || d.producto.precio || 0;
                
                // Calculamos subtotal
                const subtotal = precioUnitario * d.cantidad;
                
                tbody.insertAdjacentHTML('beforeend', `
                    <tr>
                        <td>
                            <div class="fw-bold text-dark">${d.producto.nombre}</div>
                            <small class="text-muted">Unit: S/ ${precioUnitario.toFixed(2)}</small>
                        </td>
                        <td class="text-center fw-bold">${d.cantidad}</td>
                        <td class="text-end fw-bold text-success">S/ ${subtotal.toFixed(2)}</td>
                    </tr>
                `);
            });
        })
        .catch(err => {
            console.error(err);
            tbody.innerHTML = '<tr><td colspan="3" class="text-danger text-center">Error al cargar datos</td></tr>';
        });
}