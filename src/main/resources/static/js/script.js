// PetX - Scripts Principais
document.addEventListener('DOMContentLoaded', () => {
  initializeThemeSystem();
  initializeTooltips();
  enhanceNavigation();
  initializeAnimations();
  addFormEnhancements();
});

// ================== SISTEMA DE TEMA (CLARO/ESCURO) ==================
function initializeThemeSystem() {
  const htmlEl = document.documentElement;
  const toggleBtns = document.querySelectorAll('.theme-toggle');

  // Detectar preferência do sistema
  const prefersDarkScheme = window.matchMedia('(prefers-color-scheme: dark)');
  
  // Estado inicial baseado em: 1) localStorage, 2) preferência do sistema, 3) light como fallback
  const fromStorage = localStorage.getItem('theme');
  const systemPreference = prefersDarkScheme.matches ? 'dark' : 'light';
  const initial = fromStorage || systemPreference;
  
  console.log('🎨 Inicializando sistema de tema...');
  console.log('📱 Preferência do sistema:', systemPreference);
  console.log('💾 Tema salvo:', fromStorage);
  console.log('✅ Tema inicial aplicado:', initial);
  
  setTheme(initial);
  updateToggleUI(initial);

  // Escutar mudanças na preferência do sistema (apenas se não há tema salvo)
  prefersDarkScheme.addEventListener('change', (e) => {
    const savedTheme = localStorage.getItem('theme');
    if (!savedTheme) {
      // Só muda automaticamente se o usuário não definiu uma preferência manual
      const newTheme = e.matches ? 'dark' : 'light';
      console.log('🔄 Sistema mudou para:', newTheme);
      setTheme(newTheme, false); // Não salva no localStorage para manter modo automático
      updateToggleUI(newTheme);
    }
  });

  toggleBtns.forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      const current = htmlEl.getAttribute('data-theme') || 'light';
      const next = current === 'light' ? 'dark' : 'light';
      
      console.log('🎯 Usuário trocou tema:', current, '→', next);
      
      setTheme(next);
      updateToggleUI(next);
      btn.classList.add('theme-toggle-active');
      setTimeout(() => btn.classList.remove('theme-toggle-active'), 300);
    });

    // Duplo clique para resetar para tema automático do sistema
    btn.addEventListener('dblclick', (e) => {
      e.preventDefault();
      localStorage.removeItem('theme');
      const systemTheme = prefersDarkScheme.matches ? 'dark' : 'light';
      
      console.log('🔄 Reset para tema automático do sistema:', systemTheme);
      
      setTheme(systemTheme, false); // Não salva para manter modo automático
      updateToggleUI(systemTheme);
      btn.classList.add('theme-toggle-active');
      setTimeout(() => btn.classList.remove('theme-toggle-active'), 300);
      
      // Feedback visual
      const tooltip = bootstrap.Tooltip.getInstance(btn);
      if (tooltip) {
        const originalTitle = btn.getAttribute('data-bs-original-title');
        btn.setAttribute('data-bs-original-title', 'Tema automático ativado!');
        tooltip.show();
        setTimeout(() => {
          tooltip.hide();
          btn.setAttribute('data-bs-original-title', originalTitle);
        }, 1500);
      }
    });
  });

  function setTheme(theme, saveToStorage = true) {
    // evita reemitir se não mudou
    if (document.documentElement.getAttribute('data-theme') === theme) return;

    // aplica no <html>
    htmlEl.classList.add('theme-switching');
    htmlEl.setAttribute('data-theme', theme);
    
    // Só salva se solicitado (para manter modo automático)
    if (saveToStorage) {
      localStorage.setItem('theme', theme);
    }

    // 🔔 avisa todo o app (gráficos, etc.)
    document.dispatchEvent(new CustomEvent('petx:theme-changed', { detail: { theme } }));

    // meta theme-color (opcional)
    const metaThemeColor = document.querySelector('meta[name="theme-color"]');
    if (metaThemeColor) {
      metaThemeColor.setAttribute('content', theme === 'dark' ? '#1e293b' : '#4361ee');
    }

    // remove classe de transição
    requestAnimationFrame(() => requestAnimationFrame(() => {
      htmlEl.classList.remove('theme-switching');
    }));
  }

  function updateToggleUI(theme) {
    const isAutomatic = !localStorage.getItem('theme');
    
    toggleBtns.forEach(btn => {
      const icon = btn.querySelector('i');
      const text = btn.querySelector('.theme-text');
      
      if (icon) {
        icon.classList.remove('fa-sun', 'fa-moon', 'fa-adjust');
        if (isAutomatic) {
          icon.classList.add('fa-adjust'); // Ícone para modo automático
        } else {
          icon.classList.add(theme === 'dark' ? 'fa-sun' : 'fa-moon');
        }
      }
      
      if (text) {
        if (isAutomatic) {
          text.textContent = 'Auto';
        } else {
          text.textContent = theme === 'dark' ? 'Light' : 'Dark';
        }
      }
      
      // Tooltip atualizado
      if (btn.hasAttribute('data-bs-toggle')) {
        const tooltipText = isAutomatic 
          ? 'Tema automático (clique para manual, duplo-clique mantém automático)'
          : `Tema ${theme} (clique para trocar, duplo-clique para automático)`;
        btn.setAttribute('data-bs-original-title', tooltipText);
      }
    });
  }
}

// ================== BOOTSTRAP TOOLTIP ==================
function initializeTooltips() {
  const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
  [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));
}

// ================== NAVEGAÇÃO ==================
function enhanceNavigation() {
  const currentPath = window.location.pathname;
  document.querySelectorAll('.navbar-nav .nav-link').forEach(link => {
    const href = link.getAttribute('href');
    if (href && currentPath.startsWith(href) && href !== '/') {
      link.classList.add('active');
    }
  });

  document.querySelectorAll('a[href^="#"]:not([data-bs-toggle])').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
      const targetId = this.getAttribute('href');
      if (targetId !== '#') {
        e.preventDefault();
        const targetElement = document.querySelector(targetId);
        if (targetElement) targetElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }
    });
  });
}

// ================== ANIMAÇÕES ==================
function initializeAnimations() {
  const animatedCards = document.querySelectorAll('.card[data-aos]');
  if (animatedCards.length > 0 && typeof AOS !== 'undefined') {
    animatedCards.forEach((card, index) => {
      card.setAttribute('data-aos-delay', (index * 100).toString());
    });
  }

  const statCards = document.querySelectorAll('.stat-card');
  statCards.forEach(card => {
    card.addEventListener('mouseenter', () => card.classList.add('shadow-lg'));
    card.addEventListener('mouseleave', () => card.classList.remove('shadow-lg'));
  });
}

// ================== FORM ENHANCEMENTS ==================
function addFormEnhancements() {
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

  const dateInputs = document.querySelectorAll('input[type="date"]');
  dateInputs.forEach(input => {
    if (!input.value) {
      const today = new Date().toISOString().split('T')[0];
      input.value = today;
    }
  });

  const checkboxes = document.querySelectorAll('.form-check-input');
  checkboxes.forEach(checkbox => {
    checkbox.addEventListener('change', function() {
      const label = this.nextElementSibling;
      if (label && label.classList.contains('form-check-label')) {
        label.classList.toggle('fw-medium', this.checked);
      }
    });
  });
}

// ================== UTILS ==================
const formatUtils = {
  formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR');
  },
  formatCurrency(value) {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
  },
  formatCPF(cpf) {
    if (!cpf) return '';
    return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  },
  formatPhone(phone) {
    if (!phone) return '';
    return phone.replace(/(\d{2})(\d{4,5})(\d{4})/, '($1) $2-$3');
  }
};

// ================== LAZY LOAD ==================
function lazyLoadData(endpoint, containerId, template, loadingMessage = 'Carregando...') {
  const container = document.getElementById(containerId);
  if (!container) return;

  container.innerHTML = `<div class="text-center py-5"><div class="spinner-border text-primary" role="status"></div><p class="mt-2">${loadingMessage}</p></div>`;

  fetch(endpoint)
    .then(response => {
      if (!response.ok) throw new Error('Erro ao carregar dados');
      return response.json();
    })
    .then(data => {
      if (!data || data.length === 0) {
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
