console.log(">>> CARGANDO JS V2..."); // Si no ves esto, no se actualizó

document.addEventListener("DOMContentLoaded", function() {
    
    // Referencias
    const formProducto = document.getElementById('formProducto');
    const tablaProductosBody = document.getElementById('tablaProductosBody'); // Busca este ID en tu HTML

    // 1. CLICK EN LA TABLA (EDITAR / ELIMINAR)
    if (tablaProductosBody) {
        tablaProductosBody.addEventListener('click', function(event) {
            // Detectar botón o ícono
            const btnEditar = event.target.closest('.btn-editar');
            const btnEliminar = event.target.closest('.btn-eliminar');

            if (btnEditar) {
                abrirModalEditar(btnEditar);
            } else if (btnEliminar) {
                eliminarProducto(btnEliminar);
            }
        });
    } else {
        alert("ERROR: Falta id='tablaProductosBody' en el HTML");
    }

    // 2. GUARDAR
    if (formProducto) {
        formProducto.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Datos
            const nombre = formProducto.querySelector('input[name="nombre"]').value;
            const categoria = formProducto.querySelector('select[name="categoria"]').value;
            const precio = formProducto.querySelector('input[name="precio"]').value;
            const stock = formProducto.querySelector('input[name="stock"]').value;
            let stockBadge = stock > 10 ? 'bg-success' : 'bg-danger';

            // Edición vs Creación
            const filaEditando = document.querySelector('tr.editando-ahora');

            if (filaEditando) {
                // EDITAR
                filaEditando.cells[1].innerText = nombre;
                filaEditando.cells[2].innerText = categoria;
                filaEditando.cells[3].innerText = "S/ " + parseFloat(precio).toFixed(2);
                filaEditando.cells[4].innerHTML = `<span class="badge ${stockBadge}">${stock} un.</span>`;
                filaEditando.classList.remove('editando-ahora');
                document.querySelector('.modal-title').innerText = "Nuevo Producto";
            } else {
                // CREAR
                const nuevaFila = `
                    <tr>
                        <td>#NEW</td>
                        <td>${nombre}</td>
                        <td>${categoria}</td>
                        <td>S/ ${parseFloat(precio).toFixed(2)}</td>
                        <td><span class="badge ${stockBadge}">${stock} un.</span></td>
                        <td>
                            <button class="btn btn-sm btn-outline-primary btn-editar"><i class="bi bi-pencil"></i></button>
                            <button class="btn btn-sm btn-outline-danger btn-eliminar"><i class="bi bi-trash"></i></button>
                        </td>
                    </tr>
                `;
                tablaProductosBody.insertAdjacentHTML('beforeend', nuevaFila);
            }

            // Cerrar
            formProducto.reset();
            const modalElement = document.getElementById('modalProducto');
            const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
            modalInstance.hide();
        });
    }
});

// FUNCIONES AUXILIARES
function abrirModalEditar(btn) {
    const fila = btn.closest('tr');
    const modalElement = document.getElementById('modalProducto');
    
    // Cargar datos
    modalElement.querySelector('input[name="nombre"]').value = fila.cells[1].innerText;
    modalElement.querySelector('select[name="categoria"]').value = fila.cells[2].innerText;
    modalElement.querySelector('input[name="precio"]').value = fila.cells[3].innerText.replace('S/ ', '').trim();
    modalElement.querySelector('input[name="stock"]').value = fila.cells[4].innerText.replace(' un.', '').trim();

    // Marcar fila
    document.querySelectorAll('tr').forEach(tr => tr.classList.remove('editando-ahora'));
    fila.classList.add('editando-ahora');
    
    document.querySelector('.modal-title').innerText = "Editar Producto";
    new bootstrap.Modal(modalElement).show();
}

function eliminarProducto(btn) {
    if(confirm("¿Eliminar?")) btn.closest('tr').remove();
}