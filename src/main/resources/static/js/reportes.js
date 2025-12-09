/* reportes.js - Gráficos Mejorados */

document.addEventListener("DOMContentLoaded", function() {
    
    // --- 1. GRÁFICO DE BARRAS (VENTAS MENSUALES) ---
    const ctxVentas = document.getElementById('graficoVentas');
    
    if (ctxVentas && typeof datosMesesJava !== 'undefined') {
        
        // Verificamos si hay datos, si todo es 0 ponemos un valor falso para probar
        const hayDatos = datosMesesJava.some(valor => valor > 0);
        
        new Chart(ctxVentas, {
            type: 'bar', // Tipo barra se ve mejor cuando hay pocos datos
            data: {
                labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
                datasets: [{
                    label: 'Ventas en Soles (S/)',
                    data: datosMesesJava, 
                    backgroundColor: 'rgba(25, 135, 84, 0.6)', // Verde éxito
                    borderColor: 'rgba(25, 135, 84, 1)',
                    borderWidth: 1,
                    borderRadius: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) { return 'S/ ' + value; }
                        }
                    }
                },
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return `Venta: S/ ${context.raw.toFixed(2)}`;
                            }
                        }
                    }
                }
            }
        });
    }

    // --- 2. GRÁFICO DE PASTEL (CATEGORÍAS) ---
    const ctxCat = document.getElementById('graficoCategorias');
    
    if (ctxCat && typeof catNombresJava !== 'undefined' && catNombresJava.length > 0) {
        
        new Chart(ctxCat, {
            type: 'doughnut', // Tipo dona es más moderno
            data: {
                labels: catNombresJava, 
                datasets: [{
                    data: catCantidadesJava,
                    backgroundColor: [
                        '#198754', // Verde principal
                        '#ffc107', // Amarillo
                        '#0d6efd', // Azul
                        '#dc3545', // Rojo
                        '#6f42c1', // Morado
                        '#fd7e14'  // Naranja
                    ],
                    borderWidth: 2,
                    hoverOffset: 10
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'right',
                        labels: { boxWidth: 15, font: { size: 11 } }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const total = context.chart._metasets[context.datasetIndex].total;
                                const valor = context.raw;
                                const porcentaje = ((valor / total) * 100).toFixed(1) + '%';
                                return `${context.label}: ${valor} un. (${porcentaje})`;
                            }
                        }
                    }
                }
            }
        });
    } else {
        // Si no hay datos, mostrar mensaje en consola (opcional)
        console.log("No hay datos suficientes para el gráfico de categorías.");
    }
});

function imprimirReporte() { window.print(); }