/* admindashboard.js - Lógica Final Corregida */

document.addEventListener("DOMContentLoaded", function() {
    
    // Referencias a los elementos del HTML
    const formEmpleado = document.getElementById('formEmpleado');
    const tablaEmpleadosBody = document.getElementById('tablaEmpleadosBody');
    const btnNuevoEmpleado = document.getElementById('btnNuevoEmpleado');

    // ==========================================
    // 1. BOTÓN "NUEVO EMPLEADO" (Limpieza)
    // ==========================================
    if (btnNuevoEmpleado) {
        btnNuevoEmpleado.addEventListener('click', function() {
            // 1. Limpiar campos del formulario
            formEmpleado.reset();
            // 2. Olvidar cualquier edición anterior
            delete formEmpleado.dataset.rowIndex;
            
            // 3. Restaurar textos originales del modal
            const modalTitle = document.querySelector('#modalEmpleado .modal-title');
            if(modalTitle) modalTitle.innerText = "Crear Usuario Empleado";
            
            // 4. Buscar SOLO el botón de adentro del formulario y cambiarle el texto
            const btnGuardar = formEmpleado.querySelector('button[type="submit"]');
            if(btnGuardar) btnGuardar.innerText = "Guardar Empleado";
        });
    }

    // ==========================================
    // 2. DETECTOR DE CLICS EN LA TABLA
    // ==========================================
    if (tablaEmpleadosBody) {
        tablaEmpleadosBody.addEventListener('click', function(event) {
            // Detectar si el clic fue en Editar o Eliminar (botón o ícono)
            const btnEditar = event.target.closest('.btn-editar');
            const btnEliminar = event.target.closest('.btn-eliminar');

            if (btnEditar) {
                prepararEdicion(btnEditar);
            } else if (btnEliminar) {
                eliminarFila(btnEliminar);
            }
        });
    }

    // ==========================================
    // 3. GUARDAR (CREAR O EDITAR)
    // ==========================================
    if (formEmpleado) {
        formEmpleado.addEventListener('submit', function(event) {
            event.preventDefault(); // Evitar recarga

            // Capturar datos del formulario
            const nombre = formEmpleado.querySelector('input[name="nombre"]').value;
            const correo = formEmpleado.querySelector('input[name="correo"]').value;
            const rolSelect = formEmpleado.querySelector('select[name="rol"]');
            const rolTexto = rolSelect.options[rolSelect.selectedIndex].text;
            const rolClase = getRolClass(rolSelect.value);

            // Verificar si estamos en modo edición
            const rowIndex = formEmpleado.dataset.rowIndex;

            if (rowIndex) {
                // --- MODO EDICIÓN: ACTUALIZAR FILA ---
                const fila = tablaEmpleadosBody.rows[rowIndex];
                if(fila) {
                    fila.cells[1].innerText = nombre;
                    fila.cells[2].innerText = correo;
                    fila.cells[3].innerHTML = `<span class="badge ${rolClase}">${rolTexto}</span>`;
                    alert("Empleado actualizado correctamente");
                }
            } else {
                // --- MODO CREACIÓN: NUEVA FILA ---
                const nuevaFila = `
                    <tr>
                        <td>#NEW</td>
                        <td>${nombre}</td>
                        <td>${correo}</td>
                        <td><span class="badge ${rolClase}">${rolTexto}</span></td>
                        <td>
                            <button class="btn btn-sm btn-outline-primary btn-editar"><i class="bi bi-pencil"></i></button>
                            <button class="btn btn-sm btn-outline-danger btn-eliminar"><i class="bi bi-trash"></i></button>
                        </td>
                    </tr>
                `;
                tablaEmpleadosBody.insertAdjacentHTML('beforeend', nuevaFila);
                alert("Empleado creado correctamente");
            }

            // Cerrar el modal
            const modalElement = document.getElementById('modalEmpleado');
            const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
            modalInstance.hide();
            
            // Limpiar formulario al terminar para dejarlo listo
            formEmpleado.reset();
            delete formEmpleado.dataset.rowIndex;
        });
    }

    // --- FUNCIONES AUXILIARES ---

    function getRolClass(rolValue) {
        if(rolValue === 'VENDEDOR') return 'bg-success';
        if(rolValue === 'CAJERO') return 'bg-warning text-dark';
        if(rolValue === 'ALMACENERO') return 'bg-info text-dark';
        return 'bg-secondary';
    }

    function prepararEdicion(btn) {
        const fila = btn.closest('tr');
        
        // Llenar el formulario con datos de la fila
        formEmpleado.querySelector('input[name="nombre"]').value = fila.cells[1].innerText;
        formEmpleado.querySelector('input[name="correo"]').value = fila.cells[2].innerText;
        
        // Seleccionar rol
        const rolActual = fila.cells[3].innerText.trim();
        const selectRol = formEmpleado.querySelector('select[name="rol"]');
        for (let i = 0; i < selectRol.options.length; i++) {
            if (selectRol.options[i].text === rolActual) {
                selectRol.selectedIndex = i;
                break;
            }
        }

        // Guardar índice de la fila para saber cuál actualizar luego
        const index = Array.from(fila.parentNode.children).indexOf(fila);
        formEmpleado.dataset.rowIndex = index;

        // Cambiar textos VISUALES del modal
        const modalTitle = document.querySelector('#modalEmpleado .modal-title');
        if(modalTitle) modalTitle.innerText = "Editar Empleado";
        
        const btnGuardar = formEmpleado.querySelector('button[type="submit"]');
        if(btnGuardar) btnGuardar.innerText = "Actualizar Datos";

        // Abrir modal
        const modalElement = document.getElementById('modalEmpleado');
        const modal = new bootstrap.Modal(modalElement);
        modal.show();
    }

    function eliminarFila(btn) {
        if(confirm('¿Seguro que deseas eliminar este empleado?')) {
            btn.closest('tr').remove();
        }
    }
});