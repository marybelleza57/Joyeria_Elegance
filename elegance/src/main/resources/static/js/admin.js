document.addEventListener('DOMContentLoaded', () => {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl)
    });

    // Inicializar Tablas con DataTables (jQuery)
    if (typeof $ !== 'undefined' && $.fn.DataTable) {
        $('.datatable').DataTable({
            responsive: true,
            pageLength: 5,
            lengthMenu: [5, 10, 15, 20],
            language: {
                search: "_INPUT_",
                searchPlaceholder: "Buscar registro",
                lengthMenu: " _MENU_ registros por página",
                info: "Mostrando _START_ a _END_ de _TOTAL_ registros",
                infoEmpty: "Mostrando 0 a 0 de 0 registros",
                emptyTable: "No se encontraron registros",
                zeroRecords: "No se encontraron registros que coincidan",
                paginate: {
                    first: "Primero",
                    last: "Último",
                    next: "Siguiente",
                    previous: "Anterior"
                }
            },
            // Configuración nativa de Layout (DataTables v2)
            layout: {
                topStart: null, // Ocultar lo que normalmente va arriba a la izquierda
                topEnd: 'search', // Buscador arriba a la derecha
                bottomStart: 'info', // Texto "Mostrando 1 a 5..." abajo a la izquierda
                bottomEnd: ['pageLength', 'paging'] // Selector y paginador abajo a la derecha, agrupados
            },
            // Eliminar estilos y ordenar cuando se inicializa
            initComplete: function () {
                var $tableContainer = $(this).closest('.dt-container');
                var $searchDiv = $tableContainer.find('.dt-search');
                var $searchInput = $searchDiv.find('input');

                $searchInput.addClass('premium-search');

                $searchDiv.addClass('position-relative d-flex align-items-center');
                if ($searchDiv.find('i.fa-search').length === 0) {
                    $searchDiv.prepend('<i class="fas fa-search text-muted position-absolute ms-3" style="pointer-events: none; z-index: 10;"></i>');
                }
                $searchInput.css('padding-left', '2.5rem');

                // Asegurar que el selector y paginador tengan estilos alineados
                $tableContainer.find('.dt-length select').addClass('dataTable-selector');
            }
        });
    }
});
