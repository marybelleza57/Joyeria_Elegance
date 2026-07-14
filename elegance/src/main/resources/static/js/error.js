document.addEventListener('DOMContentLoaded', function () {
    const codigoError = document.querySelector('.codigo-error');
    const tituloError = document.querySelector('.titulo-error');
    const mensajeError = document.querySelector('.mensaje-error');

    if (codigoError) {
        const codigo = codigoError.textContent.trim();

        const mensajes = {
            '404': {
                titulo: 'Página no encontrada',
                mensaje: 'Lo sentimos, la página que buscas no existe o ha sido movida.'
            },
            '405': {
                titulo: 'Método no permitido',
                mensaje: 'El método que intentas usar no está permitido para esta URL.'
            },
            '403': {
                titulo: 'Acceso denegado',
                mensaje: 'No tienes permisos para acceder a esta página.'
            },
            '500': {
                titulo: 'Error interno del servidor',
                mensaje: 'Algo salió mal en el servidor. Por favor, inténtalo de nuevo más tarde.'
            },
            '400': {
                titulo: 'Solicitud incorrecta',
                mensaje: 'La solicitud no pudo ser procesada. Verifica los datos enviados.'
            }
        };

        const errorInfo = mensajes[codigo] || {
            titulo: 'Error inesperado',
            mensaje: 'Ha ocurrido un error inesperado. Por favor, inténtalo de nuevo.'
        };

        if (tituloError) {
            tituloError.textContent = errorInfo.titulo;
        }
        if (mensajeError) {
            mensajeError.textContent = errorInfo.mensaje;
        }

        console.log('Código de error:', codigo);
        console.log('Título:', errorInfo.titulo);
        console.log('Mensaje:', errorInfo.mensaje);
    }
});