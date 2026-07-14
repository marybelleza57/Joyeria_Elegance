function togglePasswordVisibility(inputId, btn) {
    const input = document.getElementById(inputId);
    const icon = btn.querySelector('i');
    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    } else {
        input.type = 'password';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("formChangePassword");
    if (!form) return;

    form.addEventListener("submit", function (event) {
        event.preventDefault();
        event.stopPropagation();

        const currentPass = document.getElementById("currentPassword").value;
        const newPass = document.getElementById("newPassword").value;
        const confirmPass = document.getElementById("confirmNewPassword").value;
        const alertDiv = document.getElementById("changePasswordAlert");

        if (!currentPass || !newPass || !confirmPass) {
            alertDiv.className = "alert alert-danger rounded-0 py-2.5 small mb-3";
            alertDiv.textContent = "Todos los campos son obligatorios.";
            alertDiv.classList.remove("d-none");
            return;
        }

        if (newPass !== confirmPass) {
            alertDiv.className = "alert alert-danger rounded-0 py-2.5 small mb-3";
            alertDiv.textContent = "Las nuevas contraseñas no coinciden.";
            alertDiv.classList.remove("d-none");
            return;
        }

        const formData = new URLSearchParams();
        formData.append("currentPassword", currentPass);
        formData.append("newPassword", newPass);

        fetch("/admin/cambiar-contrasena", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: formData.toString()
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alertDiv.className = "alert alert-success rounded-0 py-2.5 small mb-3";
                    alertDiv.textContent = data.message;
                    alertDiv.classList.remove("d-none");
                    form.reset();
                    setTimeout(() => {
                        const modalEl = document.getElementById("changePasswordModal");
                        const modal = bootstrap.Modal.getInstance(modalEl);
                        if (modal) modal.hide();
                        alertDiv.classList.add("d-none");
                    }, 2000);
                } else {
                    alertDiv.className = "alert alert-danger rounded-0 py-2.5 small mb-3";
                    alertDiv.textContent = data.message;
                    alertDiv.classList.remove("d-none");
                }
            })
            .catch(err => {
                alertDiv.className = "alert alert-danger rounded-0 py-2.5 small mb-3";
                alertDiv.textContent = "Ocurrió un error en el servidor.";
                alertDiv.classList.remove("d-none");
            });
    });
});