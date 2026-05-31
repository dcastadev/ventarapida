// VentaRápida — app.js

// Auto-cerrar alertas flash después de 5 segundos
document.addEventListener('DOMContentLoaded', function () {

    // Cierre automático de alertas
    const alertas = document.querySelectorAll('.alert.alert-success, .alert.alert-danger');
    alertas.forEach(function (alerta) {
        setTimeout(function () {
            const bsAlert = new bootstrap.Alert(alerta);
            bsAlert.close();
        }, 5000);
    });

    // Activar tooltips de Bootstrap
    const tooltips = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    tooltips.forEach(function (el) {
        new bootstrap.Tooltip(el);
    });

    // Marcar el nav-link activo según la URL actual
    const path = window.location.pathname;
    document.querySelectorAll('.navbar .nav-link').forEach(function (link) {
        if (link.getAttribute('href') && path.startsWith(link.getAttribute('href'))
            && link.getAttribute('href') !== '/ventarapida/') {
            link.classList.add('active');
        }
    });
});
