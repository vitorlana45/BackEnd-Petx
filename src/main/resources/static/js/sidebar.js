(function () {
    const BODY = document.body;                  // ou .layout se você usar um wrapper
    const SIDEBAR = document.querySelector('.sidebar');
    const TOGGLE = document.querySelector('#sidebarToggle'); // botão do seu header
    const STORAGE_KEY = 'sidebar-collapsed';

    if (!SIDEBAR) return;

    // Estado inicial a partir do localStorage
    const collapsed = localStorage.getItem(STORAGE_KEY) === '1';
    if (collapsed) {
        BODY.classList.add('sidebar-collapsed');
        SIDEBAR.setAttribute('aria-expanded', 'false');
    } else {
        SIDEBAR.setAttribute('aria-expanded', 'true');
    }

    // Toggle (clique no botão)
    if (TOGGLE) {
        TOGGLE.addEventListener('click', () => {
            const isCollapsed = BODY.classList.toggle('sidebar-collapsed');
            SIDEBAR.setAttribute('aria-expanded', String(!isCollapsed));
            localStorage.setItem(STORAGE_KEY, isCollapsed ? '1' : '0');
        });
    }

    // (Opcional) expandir ao passar o mouse quando colapsado
    SIDEBAR.addEventListener('mouseenter', () => {
        if (BODY.classList.contains('sidebar-collapsed')) {
            SIDEBAR.classList.add('sidebar-hover');
        }
    });
    SIDEBAR.addEventListener('mouseleave', () => {
        SIDEBAR.classList.remove('sidebar-hover');
    });
})();
