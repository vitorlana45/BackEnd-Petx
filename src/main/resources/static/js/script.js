// Scripts PetX
document.addEventListener('DOMContentLoaded', () => {
    const htmlEl = document.documentElement;
    const toggleBtn = document.getElementById('theme-toggle');

    // --- Detecta preferências
    const fromCookie = getCookie('petx-theme');
    const fromStorage = localStorage.getItem('petx-theme');
    const prefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;

    // Tema inicial (ordem de prioridade: cookie > storage > prefers-color-scheme > 'light')
    const initial = fromCookie || fromStorage || (prefersDark ? 'dark' : 'light');
    setTheme(initial);

    // Atualiza UI do botão
    updateToggleUI(initial);

    // Listener de clique
    if (toggleBtn) {
        toggleBtn.addEventListener('click', () => {
            const current = htmlEl.getAttribute('data-theme') || 'light';
            const next = current === 'light' ? 'dark' : 'light';
            setTheme(next);
            updateToggleUI(next);

            // feedback visual
            toggleBtn.style.transform = 'scale(0.96)';
            setTimeout(() => (toggleBtn.style.transform = 'scale(1)'), 120);
        });
    }

    function setTheme(theme) {
        htmlEl.setAttribute('data-theme', theme);
        localStorage.setItem('petx-theme', theme);
        setCookie('petx-theme', theme, 365); // para o Thymeleaf ler no próximo render
    }

    function updateToggleUI(theme) {
        if (!toggleBtn) return; // evita erro se botão não existir nessa página

        const icon = toggleBtn.querySelector('i');
        const text = toggleBtn.querySelector('.theme-text');

        // se por algum motivo faltar o ícone/texto, não estoura erro
        if (icon) icon.className = theme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';
        if (text) text.textContent = theme === 'dark' ? 'Light' : 'Dark';
    }

    // utils cookies
    function setCookie(name, value, days) {
        const d = new Date();
        d.setTime(d.getTime() + days * 24 * 60 * 60 * 1000);
        document.cookie = `${name}=${value};expires=${d.toUTCString()};path=/;SameSite=Lax`;
    }

    function getCookie(name) {
        const m = document.cookie.match('(^|;)\\s*' + name + '\\s*=\\s*([^;]+)');
        return m ? m.pop() : '';
    }
});