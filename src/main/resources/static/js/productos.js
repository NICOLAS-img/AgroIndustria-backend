/* productos.js - Gestión Completa (Con Imagen y Descripción) */

document.addEventListener("DOMContentLoaded", function() {
    const formProducto = document.getElementById('formProducto');

    // 1. CARGAR PRODUCTOS AL INICIAR
    cargarProductos();

    // 2. BOTÓN "NUEVO PRODUCTO" (Limpiar form)
    const btnNuevo = document.querySelector('[data-bs-target="#modalProducto"]');
    if(btnNuevo) {
        btnNuevo.addEventListener('click', () => {
            formProducto.reset();
            document.getElementById('prodId').value = '';
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
                // CAPTURAMOS LOS NUEVOS CAMPOS
                imagen: formProducto.querySelector('input[name="imagen"]').value,
                descripcion: formProducto.querySelector('textarea[name="descripcion"]').value,
                disponible: true
            };

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
                alert("✅ Producto guardado correctamente");
                const modalElement = document.getElementById('modalProducto');
                const modal = bootstrap.Modal.getInstance(modalElement);
                modal.hide();
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
                
                // Imagen por defecto si viene vacía
                const imgUrl = (prod.imagen && prod.imagen !== '') ? prod.imagen : 'https://dummyimage.com/50x50/dee2e6/6c757d.jpg&text=IMG';

                tbody.insertAdjacentHTML('beforeend', `
                    <tr class="align-middle">
                        <td>#${prod.id}</td>
                        <td>
                            <div class="d-flex align-items-center">
                                <img src="${imgUrl}" class="rounded me-2 border" width="40" height="40" style="object-fit: cover;">
                                <div>
                                    <div class="fw-bold">${prod.nombre}</div>
                                </div>
                            </div>
                        </td>
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
    fetch(`/api/productos/${id}`)
        .then(res => res.json())
        .then(prod => {
            const form = document.getElementById('formProducto');
            document.getElementById('prodId').value = prod.id;
            
            // LLENAR TODOS LOS CAMPOS
            form.querySelector('input[name="nombre"]').value = prod.nombre;
            form.querySelector('select[name="categoria"]').value = prod.categoria;
            form.querySelector('input[name="precio"]').value = prod.precio;
            form.querySelector('input[name="stock"]').value = prod.stock;
            
            // Llenar Imagen y Descripción
            form.querySelector('input[name="imagen"]').value = prod.imagen || '';
            form.querySelector('textarea[name="descripcion"]').value = prod.descripcion || '';

            document.querySelector('.modal-title').innerText = "Editar Producto";
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
                    alert("Error al eliminar (puede tener ventas asociadas)");
                }
            });
    }
}