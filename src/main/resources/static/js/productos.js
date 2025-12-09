/* productos.js - Gestión Completa con Edición */

document.addEventListener("DOMContentLoaded", function() {
    const formProducto = document.getElementById('formProducto');

    // 1. CARGAR PRODUCTOS AL INICIAR
    cargarProductos();

    // 2. BOTÓN "NUEVO PRODUCTO" (Para limpiar el formulario)
    // Busca el botón que abre el modal y agrégale un listener para limpiar
    const btnNuevo = document.querySelector('[data-bs-target="#modalProducto"]');
    if(btnNuevo) {
        btnNuevo.addEventListener('click', () => {
            formProducto.reset();
            document.getElementById('prodId').value = ''; // Limpiar ID oculto
            document.querySelector('.modal-title').innerText = "Nuevo Producto";
        });
    }

    // 3. GUARDAR (CREAR O EDITAR)
    if (formProducto) {
        formProducto.addEventListener('submit', function(e) {
            e.preventDefault(); // Evitar recarga
            
            const data = {
                id: document.getElementById('prodId').value || null,
                nombre: formProducto.querySelector('input[name="nombre"]').value,
                categoria: formProducto.querySelector('select[name="categoria"]').value,
                precio: parseFloat(formProducto.querySelector('input[name="precio"]').value),
                stock: parseInt(formProducto.querySelector('input[name="stock"]').value),
                disponible: true
            };

            // Si tiene ID es PUT (Editar), si no es POST (Crear)
            const metodo = data.id ? 'PUT' : 'POST';
            const url = data.id ? `/api/productos/${data.id}` : '/api/productos';

            fetch(url, {
                method: metodo,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            })
            .then(res => {
                if (res.ok) return res.json();
                throw new Error("Error al guardar");
            })
            .then(() => {
                alert("✅ Guardado correctamente");
                // Cerrar modal
                const modalElement = document.getElementById('modalProducto');
                const modal = bootstrap.Modal.getInstance(modalElement);
                modal.hide();
                // Recargar tabla
                cargarProductos();
            })
            .catch(err => alert("❌ Error: " + err));
        });
    }
});

// --- FUNCIONES AUXILIARES ---

function cargarProductos() {
    fetch('/api/productos')
        .then(res => res.json())
        .then(productos => {
            const tbody = document.getElementById('tablaProductosBody');
            if(!tbody) return;
            
            tbody.innerHTML = ''; // Limpiar tabla
            
            productos.forEach(prod => {
                const stockBadge = prod.stock > 10 ? 'bg-success' : 'bg-danger';
                
                // AQUÍ AGREGAMOS EL BOTÓN EDITAR (LÁPIZ)
                tbody.insertAdjacentHTML('beforeend', `
                    <tr>
                        <td>#${prod.id}</td>
                        <td>${prod.nombre}</td>
                        <td>${prod.categoria}</td>
                        <td>S/ ${prod.precio.toFixed(2)}</td>
                        <td><span class="badge ${stockBadge}">${prod.stock} un.</span></td>
                        <td>
                            <button class="btn btn-sm btn-outline-primary me-1" onclick="editarProducto(${prod.id})">
                                <i class="bi bi-pencil"></i>
                            </button>
                            <button class="btn btn-sm btn-outline-danger" onclick="eliminarProducto(${prod.id})">
                                <i class="bi bi-trash"></i>
                            </button>
                        </td>
                    </tr>
                `);
            });
        });
}

function editarProducto(id) {
    // 1. Pedir los datos del producto al servidor
    fetch(`/api/productos/${id}`)
        .then(res => res.json())
        .then(prod => {
            // 2. Llenar el formulario con esos datos
            const form = document.getElementById('formProducto');
            document.getElementById('prodId').value = prod.id; // IMPORTANTE: El ID oculto
            form.querySelector('input[name="nombre"]').value = prod.nombre;
            form.querySelector('select[name="categoria"]').value = prod.categoria;
            form.querySelector('input[name="precio"]').value = prod.precio;
            form.querySelector('input[name="stock"]').value = prod.stock;

            // 3. Cambiar título del modal
            document.querySelector('.modal-title').innerText = "Editar Producto";

            // 4. Abrir el modal
            new bootstrap.Modal(document.getElementById('modalProducto')).show();
        });
}

function eliminarProducto(id) {
    if(confirm("¿Seguro que deseas eliminar este producto?")) {
        fetch(`/api/productos/${id}`, { method: 'DELETE' })
            .then(res => {
                if(res.ok) {
                    cargarProductos();
                } else {
                    alert("Error al eliminar");
                }
            });
    }
}