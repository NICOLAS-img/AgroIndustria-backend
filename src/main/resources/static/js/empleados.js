/* empleados.js - CORREGIDO */

document.addEventListener("DOMContentLoaded", function() {
    cargarEmpleados(); // Cargar la tabla al iniciar

    const form = document.getElementById('formEmpleado');
    
    // LOGICA DE GUARDAR (CREAR O EDITAR)
    if(form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Creamos el objeto tal cual lo espera Java (Empleado.java)
            const data = {
                id: document.getElementById('empId').value || null, // Usamos 'id'
                nombre: form.querySelector('input[name="nombre"]').value,
                apellido: form.querySelector('input[name="apellido"]').value,
                dni: form.querySelector('input[name="dni"]').value,
                correo: form.querySelector('input[name="correo"]').value,
                rol: form.querySelector('select[name="rol"]').value
            };

            const pass = form.querySelector('input[name="password"]').value;
            if(pass) data.password = pass; // Solo enviamos pass si se escribió algo

            fetch('/api/empleados', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            })
            .then(res => res.text())
            .then(resp => {
                if(resp === "OK") {
                    alert("✅ Guardado correctamente");
                    // Cerrar modal y recargar
                    const modalEl = document.getElementById('modalEmpleado');
                    const modal = bootstrap.Modal.getInstance(modalEl);
                    modal.hide();
                    cargarEmpleados();
                    limpiarFormulario();
                } else {
                    alert("❌ Error al guardar: " + resp);
                }
            })
            .catch(error => console.error('Error:', error));
        });
    }
});

// LISTAR EN LA TABLA
function cargarEmpleados() {
    fetch('/api/empleados')
    .then(res => res.json())
    .then(data => {
        const tbody = document.getElementById('tablaEmpleadosBody');
        tbody.innerHTML = '';
        
        data.forEach(emp => {
            // Protección contra nulos para que no se vea feo
            const apellidoSeguro = emp.apellido ? emp.apellido : ''; 
            const dniSeguro = emp.dni ? emp.dni : '-';

            tbody.insertAdjacentHTML('beforeend', `
                <tr>
                    <td class="ps-4 fw-bold">#${emp.id}</td> <td>${emp.nombre} ${apellidoSeguro}</td>
                    <td>${dniSeguro}</td>
                    <td>${emp.correo}</td>
                    <td><span class="badge bg-primary bg-opacity-10 text-primary">${emp.rol}</span></td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary me-1" onclick="editarEmpleado(${emp.id})">
                            <i class="bi bi-pencil"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" onclick="eliminarEmpleado(${emp.id})">
                            <i class="bi bi-trash"></i>
                        </button>
                    </td>
                </tr>
            `);
        });
    });
}

// EDITAR (Llenar formulario)
function editarEmpleado(id) {
    fetch(`/api/empleados/${id}`)
    .then(res => res.json())
    .then(emp => {
        // Llenar campos
        document.getElementById('empId').value = emp.id; // Usamos emp.id
        const form = document.getElementById('formEmpleado');
        
        form.querySelector('input[name="nombre"]').value = emp.nombre;
        // Si el apellido es null, poner vacío
        form.querySelector('input[name="apellido"]').value = emp.apellido ? emp.apellido : '';
        form.querySelector('input[name="dni"]').value = emp.dni;
        form.querySelector('input[name="correo"]').value = emp.correo;
        form.querySelector('select[name="rol"]').value = emp.rol;
        form.querySelector('input[name="password"]').value = ""; 
        
        document.querySelector('.modal-title').innerText = "Editar Empleado";
        new bootstrap.Modal(document.getElementById('modalEmpleado')).show();
    });
}

// ELIMINAR
function eliminarEmpleado(id) {
    if(confirm("¿Seguro que deseas eliminar a este empleado?")) {
        fetch(`/api/empleados/${id}`, { method: 'DELETE' })
        .then(res => res.text())
        .then(resp => {
            if(resp === "OK") {
                cargarEmpleados();
            } else {
                alert("❌ No se pudo eliminar. Verifique que el empleado no tenga ventas asociadas.");
            }
        });
    }
}

function limpiarFormulario() {
    document.getElementById('formEmpleado').reset();
    document.getElementById('empId').value = '';
    document.querySelector('.modal-title').innerText = "Nuevo Empleado";
}