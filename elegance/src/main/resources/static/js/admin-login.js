document.addEventListener('DOMContentLoaded', function () {
    const toggleBtn = document.getElementById('toggleContrasena');
    const toggleIcon = document.getElementById('toggleIcon');
    const contrasenaInput = document.getElementById('contrasena');

    if (toggleBtn && contrasenaInput && toggleIcon) {
        toggleBtn.addEventListener('click', function () {
            if (contrasenaInput.type === 'password') {
                contrasenaInput.type = 'text';
                toggleIcon.classList.remove('fa-eye-slash');
                toggleIcon.classList.add('fa-eye');
            } else {
                contrasenaInput.type = 'password';
                toggleIcon.classList.remove('fa-eye');
                toggleIcon.classList.add('fa-eye-slash');
            }
        });
    }
});

(function () {
    'use strict';
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
})();