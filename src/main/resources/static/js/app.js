document.addEventListener('DOMContentLoaded', () => {
    // =========================================================================
    // INITIALIZATION & CONFIGURATION
    // =========================================================================

    // Initialize AOS (Animate on Scroll) library
    AOS.init({
        duration: 800,
        easing: 'ease',
        once: true
    });

    // Initialize Bootstrap tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // =========================================================================
    // CSRF & HTMX CONFIGURATION
    // =========================================================================

    const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

    // Attach CSRF token to all HTMX requests
    document.body.addEventListener('htmx:configRequest', (evt) => {
        if (csrfToken && csrfHeader) {
            evt.detail.headers[csrfHeader] = csrfToken;
        }
    });

    // Create a global fetch function with CSRF headers
    if (csrfToken && csrfHeader) {
        window.fetchWithCsrf = function(url, options = {}) {
            options.headers = options.headers || {};
            options.headers[csrfHeader] = csrfToken;
            return fetch(url, options);
        };
    }

    // =========================================================================
    // SIDEBAR LOGIC
    // =========================================================================

    const sidebarToggle = document.getElementById('pxSidebarToggle');
    const body = document.body;

    if (sidebarToggle) {
        const icon = sidebarToggle.querySelector('i');

        // Function to apply the saved sidebar state on page load
        const applyInitialSidebarState = () => {
            const isCollapsed = localStorage.getItem('sidebarCollapsed') === 'true';
            if (isCollapsed) {
                body.classList.add('px-collapsed');
                icon?.classList.remove('fa-angle-double-left');
                icon?.classList.add('fa-angle-double-right');
            } else {
                body.classList.remove('px-collapsed');
                icon?.classList.remove('fa-angle-double-right');
                icon?.classList.add('fa-angle-double-left');
            }
        };

        applyInitialSidebarState();

        // Event listener for the sidebar toggle button
        sidebarToggle.addEventListener('click', () => {
            body.classList.toggle('px-collapsed');
            const isCollapsed = body.classList.contains('px-collapsed');
            localStorage.setItem('sidebarCollapsed', isCollapsed);

            // Animate the toggle icon
            if (icon) {
                icon.style.transform = 'rotate(180deg)';
                icon.style.transition = 'transform 0.3s ease';
                setTimeout(() => {
                    if (isCollapsed) {
                        icon.classList.remove('fa-angle-double-left');
                        icon.classList.add('fa-angle-double-right');
                    } else {
                        icon.classList.remove('fa-angle-double-right');
                        icon.classList.add('fa-angle-double-left');
                    }
                    icon.style.transform = '';
                }, 150);
            }
        });

        // Expand sidebar when a menu item is clicked while it's collapsed
        const sidebarLinks = document.querySelectorAll('.px-nav .px-link');
        sidebarLinks.forEach(link => {
            link.addEventListener('click', () => {
                if (body.classList.contains('px-collapsed')) {
                    sidebarToggle.click(); // Simulate a click to expand
                }
            });
        });
    }

    // =========================================================================
    // GLOBAL EVENT LISTENERS (HTMX Triggers)
    // =========================================================================

    // Listener to close modals triggered by server events
    document.body.addEventListener('closeModal', (e) => {
        const modalId = e.detail?.id || 'editarPerfilModal';
        const modalElement = document.getElementById(modalId);
        if (modalElement) {
            const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
            modalInstance.hide();
        }
    });

    // Listener for toast notifications from the server
    document.body.addEventListener('toast', (e) => {
        // Placeholder for a proper toast notification system
        console.log(`[${e.detail?.type || 'info'}] ${e.detail?.text || ''}`);
        // Example: toastr[e.detail?.type || 'info'](e.detail?.text || '');
    });

    // =========================================================================
    // THEME TOGGLE LOGIC
    // =========================================================================

    const themeToggle = document.getElementById('theme-toggle');
    if (themeToggle) {
        const themeIcon = themeToggle.querySelector('i');
        const doc = document.documentElement;

        const updateIcon = () => {
            const currentTheme = doc.getAttribute('data-theme');
            if (currentTheme === 'dark') {
                themeIcon.classList.remove('fa-moon');
                themeIcon.classList.add('fa-sun');
            } else {
                themeIcon.classList.remove('fa-sun');
                themeIcon.classList.add('fa-moon');
            }
        };

        themeToggle.addEventListener('click', () => {
            const currentTheme = doc.getAttribute('data-theme');
            const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
            doc.setAttribute('data-theme', newTheme);
            localStorage.setItem('theme', newTheme);
            updateIcon();
        });

        // Set initial icon on page load
        updateIcon();
    }
});
