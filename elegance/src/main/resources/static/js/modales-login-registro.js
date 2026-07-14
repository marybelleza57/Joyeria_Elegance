document.addEventListener("DOMContentLoaded", function () {
    // Validación de Login
    const form = document.getElementById("formLoginCliente");
    if (form) {
        const emailInput = document.getElementById("inputEmailCliente");
        const passwordInput = document.getElementById("inputPasswordCliente");
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        function validarEmail() {
            const valor = emailInput.value.trim();
            if (valor === "") {
                emailInput.classList.add("is-invalid");
                document.getElementById("emailClienteFeedback").textContent = "El correo electrónico es obligatorio.";
                return false;
            } else if (!emailRegex.test(valor)) {
                emailInput.classList.add("is-invalid");
                document.getElementById("emailClienteFeedback").textContent = "Por favor, introduce un correo válido (ejemplo@correo.com).";
                return false;
            } else {
                emailInput.classList.remove("is-invalid");
                return true;
            }
        }

        function validarPassword() {
            const valor = passwordInput.value;
            if (valor === "") {
                passwordInput.classList.add("is-invalid");
                return false;
            } else {
                passwordInput.classList.remove("is-invalid");
                return true;
            }
        }

        emailInput.addEventListener("input", validarEmail);
        passwordInput.addEventListener("input", validarPassword);

        form.addEventListener("submit", function (event) {
            const emailValido = validarEmail();
            const passwordValido = validarPassword();

            if (!emailValido || !passwordValido) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add("was-validated");
        });
    }

    // Validación de Registro
    const regForm = document.getElementById("formRegistroCliente");
    if (regForm) {
        const nameInput = document.getElementById("inputNombreCliente");
        const lastNameInput = document.getElementById("inputApellidosCliente");
        const emailInput = document.getElementById("inputEmailRegCliente");
        const telInput = document.getElementById("inputTelCliente");
        const passwordInput = document.getElementById("inputPasswordRegCliente");
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        function validarNombre() {
            if (nameInput.value.trim() === "") {
                nameInput.classList.add("is-invalid");
                return false;
            } else {
                nameInput.classList.remove("is-invalid");
                return true;
            }
        }

        function validarApellido() {
            if (lastNameInput.value.trim() === "") {
                lastNameInput.classList.add("is-invalid");
                return false;
            } else {
                lastNameInput.classList.remove("is-invalid");
                return true;
            }
        }

        function validarEmail() {
            const valor = emailInput.value.trim();
            if (valor === "") {
                emailInput.classList.add("is-invalid");
                document.getElementById("emailRegClienteFeedback").textContent = "El correo electrónico es obligatorio.";
                return false;
            } else if (!emailRegex.test(valor)) {
                emailInput.classList.add("is-invalid");
                document.getElementById("emailRegClienteFeedback").textContent = "Introduce un correo válido.";
                return false;
            } else {
                emailInput.classList.remove("is-invalid");
                return true;
            }
        }

        function validarTelefono() {
            const valor = telInput.value.trim();
            if (valor === "" || !/^9[0-9]{8}$/.test(valor)) {
                telInput.classList.add("is-invalid");
                return false;
            } else {
                telInput.classList.remove("is-invalid");
                return true;
            }
        }

        function validarPassword() {
            if (passwordInput.value === "") {
                passwordInput.classList.add("is-invalid");
                return false;
            } else {
                passwordInput.classList.remove("is-invalid");
                return true;
            }
        }

        const termsInput = document.getElementById("inputAceptaTerminosCliente");

        function validarTerms() {
            if (!termsInput.checked) {
                termsInput.classList.add("is-invalid");
                return false;
            } else {
                termsInput.classList.remove("is-invalid");
                return true;
            }
        }

        nameInput.addEventListener("input", validarNombre);
        lastNameInput.addEventListener("input", validarApellido);
        emailInput.addEventListener("input", validarEmail);
        telInput.addEventListener("input", validarTelefono);
        passwordInput.addEventListener("input", validarPassword);
        termsInput.addEventListener("change", validarTerms);

        regForm.addEventListener("submit", function (event) {
            const nV = validarNombre();
            const aV = validarApellido();
            const eV = validarEmail();
            const tV = validarTelefono();
            const pV = validarPassword();
            const termV = validarTerms();

            if (!nV || !aV || !eV || !tV || !pV || !termV) {
                event.preventDefault();
                event.stopPropagation();
            }
            regForm.classList.add("was-validated");
        });
    }

    // Toggle Password Login
    const togglePassCliente = document.getElementById("togglePasswordCliente");
    if (togglePassCliente) {
        togglePassCliente.addEventListener("click", function () {
            const passInput = document.getElementById("inputPasswordCliente");
            const eyeIcon = document.getElementById("eyeIconCliente");
            if (passInput.type === "password") {
                passInput.type = "text";
                eyeIcon.classList.remove("fa-eye-slash");
                eyeIcon.classList.add("fa-eye");
            } else {
                passInput.type = "password";
                eyeIcon.classList.remove("fa-eye");
                eyeIcon.classList.add("fa-eye-slash");
            }
        });
    }

    // Toggle Password Registro
    const togglePassRegCliente = document.getElementById("togglePasswordRegCliente");
    if (togglePassRegCliente) {
        togglePassRegCliente.addEventListener("click", function () {
            const passInput = document.getElementById("inputPasswordRegCliente");
            const eyeIcon = document.getElementById("eyeIconRegCliente");
            if (passInput.type === "password") {
                passInput.type = "text";
                eyeIcon.classList.remove("fa-eye-slash");
                eyeIcon.classList.add("fa-eye");
            } else {
                passInput.type = "password";
                eyeIcon.classList.remove("fa-eye");
                eyeIcon.classList.add("fa-eye-slash");
            }
        });
    }
});
