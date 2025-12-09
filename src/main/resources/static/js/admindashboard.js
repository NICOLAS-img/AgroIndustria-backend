<!DOCTYPE html>
<html lang="es" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resumen General | Agroindustrias Acora</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <link href="/css/admindashboard.css" rel="stylesheet">
</head>
<body>

    <div class="sidebar">
        <div class="sidebar-header">
            <i class="bi bi-tree-fill fs-1 text-success"></i>
            <div class="brand-title">Olivos Acora</div>
            <small class="text-muted">Panel Admin</small>
        </div>
        
        <a href="#" class="active"><i class="bi bi-speedometer2"></i> Resumen</a>
        <a href="/empleados"><i class="bi bi-people-fill"></i> Empleados</a>
        <a href="/admin/productos"><i class="bi bi-box-seam"></i> Productos</a>
        <a href="/admin/reportes"><i class="bi bi-file-earmark-text"></i> Reportes</a>
        
        <div class="mt-5 border-top border-secondary pt-3">
            <a href="/login" class="text-danger"><i class="bi bi-box-arrow-left"></i> Cerrar Sesión</a>
        </div>
    </div>

    <div class="main-content">
        
        <div class="bg-white p-4 rounded shadow-sm mb-4 border-start border-5 border-success d-flex justify-content-between align-items-center">
            <div>
                <h2 class="fw-bold text-dark m-0">¡Bienvenido, Administrador!</h2>
                <p class="text-muted m-0 mt-1">Resumen en tiempo real de tu negocio.</p>
            </div>
            <span class="badge bg-light text-dark border p-2">
                <i class="bi bi-calendar3"></i> Hoy: <span th:text="${#temporals.format(#temporals.createNow(), 'dd MMM yyyy')}">Fecha</span>
            </span>
        </div>

        <div class="row g-4 mb-5">
            <div class="col-md-3">
                <div class="stat-card border-start border-5 border-success">
                    <div>
                        <h6 class="text-muted small text-uppercase fw-bold">Personal</h6>
                        <h3 class="fw-bold m-0 text-dark" th:text="${totalEmpleados}">0</h3>
                        <small class="text-success">Registrados en BD</small>
                    </div>
                    <div class="stat-icon bg-success text-white"><i class="bi bi-people"></i></div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card border-start border-5 border-primary">
                    <div>
                        <h6 class="text-muted small text-uppercase fw-bold">Inventario Total</h6>
                        <h3 class="fw-bold m-0 text-dark" th:text="${totalInventario}">0</h3>
                        <small class="text-muted">Unidades (Stock)</small>
                    </div>
                    <div class="stat-icon bg-primary text-white"><i class="bi bi-box-seam"></i></div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card border-start border-5 border-danger">
                    <div>
                        <h6 class="text-muted small text-uppercase fw-bold">Stock Bajo</h6>
                        <h3 class="fw-bold m-0 text-danger" th:text="${totalStockBajo}">0</h3>
                        <small class="text-danger fw-bold">Requiere atención</small>
                    </div>
                    <div class="stat-icon bg-danger text-white"><i class="bi bi-exclamation-triangle"></i></div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card border-start border-5 border-warning">
                    <div>
                        <h6 class="text-muted small text-uppercase fw-bold">Ventas Hoy</h6>
                        <h3 class="fw-bold m-0 text-dark" th:text="${'S/ ' + ventasHoy}">S/ 0.00</h3>
                        <small class="text-success"><i class="bi bi-cash-coin"></i> Ingresos del día</small>
                    </div>
                    <div class="stat-icon bg-warning text-white"><i class="bi bi-currency-dollar"></i></div>
                </div>
            </div>
        </div>

        <div class="row g-4">
            
            <div class="col-lg-6">
                <div class="card border-0 shadow-sm h-100">
                    <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                        <h6 class="m-0 fw-bold text-danger"><i class="bi bi-box-seam me-2"></i> Alerta de Stock (< 20 un.)</h6>
                        <a href="/admin/productos" class="btn btn-sm btn-outline-danger">Ver Todo</a>
                    </div>
                    <div class="card-body p-0">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-4">Producto</th>
                                    <th>Categoría</th>
                                    <th>Stock</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr th:each="prod : ${productosBajos}">
                                    <td class="ps-4 fw-bold" th:text="${prod.nombre}">Prod</td>
                                    <td th:text="${prod.categoria}">Cat</td>
                                    <td><span class="badge bg-danger" th:text="${prod.stock + ' un.'}">0 un.</span></td>
                                </tr>
                                <tr th:if="${#lists.isEmpty(productosBajos)}">
                                    <td colspan="3" class="text-center text-success py-3">¡Todo el inventario está saludable! ✅</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="col-lg-6">
                <div class="card border-0 shadow-sm h-100">
                    <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                        <h6 class="m-0 fw-bold text-secondary"><i class="bi bi-person-check me-2"></i> Equipo de Trabajo</h6>
                        <a href="/empleados" class="btn btn-sm btn-outline-success">Gestionar</a>
                    </div>
                    <div class="card-body p-0">
                        <ul class="list-group list-group-flush">
                            <li class="list-group-item d-flex justify-content-between align-items-center px-4 py-3" th:each="emp : ${empleados}">
                                <div class="d-flex align-items-center">
                                    <div class="bg-light rounded-circle d-flex align-items-center justify-content-center me-3" style="width: 40px; height: 40px;">
                                        <i class="bi bi-person-fill text-secondary fs-5"></i>
                                    </div>
                                    <div>
                                        <h6 class="m-0 fw-bold" th:text="${emp.nombre}">Nombre</h6>
                                        <small class="text-muted" th:text="${emp.rol}">Rol</small>
                                    </div>
                                </div>
                                <span class="badge bg-success bg-opacity-10 text-success px-3 py-2 rounded-pill">Activo</span>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>