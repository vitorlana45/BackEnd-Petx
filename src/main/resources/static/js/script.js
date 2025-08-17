// PetX - Scripts Principais
document.addEventListener('DOMContentLoaded', () => {
    initializeThemeSystem();
    initializeTooltips();
    enhanceNavigation();
    initializeAnimations();
    addFormEnhancements();
});

// Sistema de temas (claro/escuro)
function initializeThemeSystem() {
    console.log("Inicializando sistema de temas...");
    const htmlEl = document.documentElement;
    const toggleBtns = document.querySelectorAll('.theme-toggle');

    if (toggleBtns.length === 0) {
        console.log("AVISO: Botões de tema não encontrados!");
    }

    // Detecta preferências
    const fromStorage = localStorage.getItem('theme');
    const prefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;

    // Tema inicial (prioridade: storage > prefers-color-scheme > 'light')
    const initial = fromStorage || (prefersDark ? 'dark' : 'light');
    setTheme(initial);
    updateToggleUI(initial);

    // Listener de clique para todos os botões
    toggleBtns.forEach(btn => {
        btn.addEventListener('click', event => {
            event.preventDefault();
            const current = htmlEl.getAttribute('data-theme') || 'light';
            const next = current === 'light' ? 'dark' : 'light';
            setTheme(next);
            updateToggleUI(next);

            btn.classList.add('theme-toggle-active');
            setTimeout(() => btn.classList.remove('theme-toggle-active'), 300);
        });
    });

    function setTheme(theme) {
        console.log(`Definindo tema: ${theme}`);
        htmlEl.setAttribute('data-theme', theme);
        localStorage.setItem('theme', theme);

        // Atualize meta theme-color para browsers mobile
        const metaThemeColor = document.querySelector('meta[name="theme-color"]');
        if (metaThemeColor) {
            metaThemeColor.setAttribute('content',
                theme === 'dark' ? '#1e293b' : '#4361ee');
        }
    }

    function updateToggleUI(theme) {
        toggleBtns.forEach(btn => {
            const icon = btn.querySelector('i');
            const text = btn.querySelector('.theme-text');
            if (icon) {
                icon.classList.remove('fa-sun', 'fa-moon');
                icon.classList.add(theme === 'dark' ? 'fa-sun' : 'fa-moon');
            }
            if (text) {
                text.textContent = theme === 'dark' ? 'Light' : 'Dark';
            }
        });
    }
}

// Inicializa tooltips do Bootstrap
function initializeTooltips() {
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));
}

// Melhora a navegação e interatividade
function enhanceNavigation() {
    // Marca o item de menu ativo baseado na URL atual
    const currentPath = window.location.pathname;
    document.querySelectorAll('.navbar-nav .nav-link').forEach(link => {
        const href = link.getAttribute('href');
        if (href && currentPath.startsWith(href) && href !== '/') {
            link.classList.add('active');
        }
    });

    // Animação suave ao clicar em links de âncora internos
    document.querySelectorAll('a[href^="#"]:not([data-bs-toggle])').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            const targetId = this.getAttribute('href');
            if (targetId !== '#') {
                e.preventDefault();
                const targetElement = document.querySelector(targetId);
                if (targetElement) {
                    targetElement.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            }
        });
    });
}

// Inicializa animações em elementos específicos
function initializeAnimations() {
    // Aplica animações aos cards
    const animatedCards = document.querySelectorAll('.card[data-aos]');
    if (animatedCards.length > 0 && typeof AOS !== 'undefined') {
        // Aplica um delay sequencial aos cards
        animatedCards.forEach((card, index) => {
            card.setAttribute('data-aos-delay', (index * 100).toString());
        });
    }

    // Animação nos cards de estatísticas
    const statCards = document.querySelectorAll('.stat-card');
    statCards.forEach(card => {
        card.addEventListener('mouseenter', () => {
            card.classList.add('shadow-lg');
        });
        card.addEventListener('mouseleave', () => {
            card.classList.remove('shadow-lg');
        });
    });
}

// Melhora interatividade de formulários
function addFormEnhancements() {
    // Adiciona validação visual em tempo real
    const forms = document.querySelectorAll('.needs-validation');
    forms.forEach(form => {
        const inputs = form.querySelectorAll('input, select, textarea');
        inputs.forEach(input => {
            input.addEventListener('blur', () => {
                if (input.checkValidity()) {
                    input.classList.add('is-valid');
                    input.classList.remove('is-invalid');
                } else if (input.value !== '') {
                    input.classList.add('is-invalid');
                    input.classList.remove('is-valid');
                }
            });
        });

        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });

    // Máscara para campos de data
    const dateInputs = document.querySelectorAll('input[type="date"]');
    dateInputs.forEach(input => {
        if (!input.value) {
            const today = new Date().toISOString().split('T')[0];
            input.value = today;
        }
    });

    // Melhora interatividade de checkboxes e radios
    const checkboxes = document.querySelectorAll('.form-check-input');
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            const label = this.nextElementSibling;
            if (label && label.classList.contains('form-check-label')) {
                if (this.checked) {
                    label.classList.add('fw-medium');
                } else {
                    label.classList.remove('fw-medium');
                }
            }
        });
    });
}

// Utilitários para formatação de dados
const formatUtils = {
    // Formata data para pt-BR
    formatDate(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString('pt-BR');
    },

    // Formata número para moeda brasileira
    formatCurrency(value) {
        return new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        }).format(value);
    },

    // Formata CPF com pontuação
    formatCPF(cpf) {
        if (!cpf) return '';
        return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
    },

    // Formata telefone brasileiro
    formatPhone(phone) {
        if (!phone) return '';
        return phone.replace(/(\d{2})(\d{4,5})(\d{4})/, '($1) $2-$3');
    }
};

// Carrega dados de maneira lazy (sob demanda)
function lazyLoadData(endpoint, containerId, template, loadingMessage = 'Carregando...') {
    const container = document.getElementById(containerId);
    if (!container) return;

    container.innerHTML = `<div class="text-center py-5"><div class="spinner-border text-primary" role="status"></div><p class="mt-2">${loadingMessage}</p></div>`;

    fetch(endpoint)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao carregar dados');
            }
            return response.json();
        })
        .then(data => {
            if (data.length === 0) {
                container.innerHTML = '<div class="alert alert-info">Nenhum dado encontrado.</div>';
                return;
            }

            container.innerHTML = '';
            data.forEach(item => {
                const element = document.createElement('div');
                element.innerHTML = template(item);
                container.appendChild(element.firstChild);
            });
        })
        .catch(error => {
            container.innerHTML = `<div class="alert alert-danger">Erro ao carregar dados: ${error.message}</div>`;
        });
}
