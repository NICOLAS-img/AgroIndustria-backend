/* reportes.js - Configuración de Gráficos */

document.addEventListener("DOMContentLoaded", function() {
    
    // --- 1. GRÁFICO DE BARRAS (VENTAS) ---
    const ctxVentas = document.getElementById('graficoVentas');
    if (ctxVentas) {
        new Chart(ctxVentas, {
            type: 'bar',
            data: {
                labels: ['Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
                datasets: [{
                    label: 'Ventas en Soles (S/)',
                    data: [8500, 9200, 10500, 11200, 9800, 12450], // Datos simulados
                    backgroundColor: 'rgba(107, 142, 35, 0.6)', // Verde Aceituna transparente
                    borderColor: 'rgba(107, 142, 35, 1)',
                    borderWidth: 1,
                    borderRadius: 5
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: { beginAtZero: true }
                }
            }
        });
    }

    // --- 2. GRÁFICO DE PASTEL (CATEGORÍAS) ---
    const ctxCat = document.getElementById('graficoCategorias');
    if (ctxCat) {
        new Chart(ctxCat, {
            type: 'doughnut', // Tipo "Dona"
            data: {
                labels: ['Negras', 'Verdes', 'Aceites', 'Rellenas'],
                datasets: [{
                    data: [45, 25, 20, 10], // Porcentajes simulados
                    backgroundColor: [
                        '#2c3e50', // Oscuro
                        '#6b8e23', // Verde Aceituna
                        '#e67e22', // Naranja (Aceite)
                        '#e74c3c'  // Rojo
                    ],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { position: 'bottom' }
                }
            }
        });
    }
});

// Función para el botón imprimir
function imprimirReporte() {
    window.print();
}