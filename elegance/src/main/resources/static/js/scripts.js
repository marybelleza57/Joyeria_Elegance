document.addEventListener("DOMContentLoaded", () => {
    // 28. Popover Initialization
    const popoverTriggerList = [].slice.call(
        document.querySelectorAll('[data-bs-toggle="popover"]'),
    );
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // 19. Form Validation
    const forms = document.querySelectorAll(".needs-validation");
    Array.from(forms).forEach((form) => {
        form.addEventListener(
            "submit",
            (event) => {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add("was-validated");
            },
            false,
        );
    });

    // Sync Cart Count Badge
    const updateCartCount = () => {
        const cart = JSON.parse(localStorage.getItem('cart')) || [];
        const totalItems = cart.reduce((sum, item) => sum + item.cantidad, 0);
        const badges = document.querySelectorAll('#cart-count');
        badges.forEach(badge => {
            badge.textContent = totalItems;
        });
    };
    updateCartCount();

    // Listen to storage events to keep badge synced across tabs
    window.addEventListener('storage', (e) => {
        if (e.key === 'cart') {
            updateCartCount();
        }
    });
});
