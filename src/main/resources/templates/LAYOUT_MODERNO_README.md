# 🎨 Sistema de Layout Moderno - ONG PetX

## 📋 Visão Geral

Este documento descreve o novo sistema de layout moderno implementado para a ONG PetX, que oferece uma interface mais limpa, organizada e fácil de manter.

## 🏗️ Estrutura do Layout

### 1. Layout Base (`layouts/base.html`)
- **Estrutura principal**: Sidebar + Header + Conteúdo
- **Responsivo**: Adapta-se a diferentes tamanhos de tela
- **Tema dinâmico**: Suporte a tema claro/escuro
- **Navegação moderna**: Sidebar organizada por seções

### 2. Sidebar (`fragmentos/sidebar.html`)
- **Organização por seções**:
  - Gestão de Animais
  - Gestão de Pessoas
  - Gestão Operacional
  - Relatórios
- **Badges informativos**: Mostra contadores
- **Perfil do usuário**: Informações e ações rápidas

### 3. Header (`fragmentos/header.html`)
- **Breadcrumb**: Navegação hierárquica
- **Notificações**: Sistema de alertas
- **Seletor de tema**: Alternância claro/escuro
- **Menu do usuário**: Ações rápidas

## 🎨 Sistema de Cores

### Paleta Principal
```css
--petx-primary: #4361ee;      /* Azul principal */
--petx-secondary: #7209b7;    /* Roxo secundário */
--petx-accent: #f72585;       /* Rosa accent */
--petx-success: #0cce6b;      /* Verde sucesso */
--petx-warning: #ff9e00;      /* Laranja aviso */
--petx-danger: #e5383b;       /* Vermelho erro */
--petx-info: #4cc9f0;         /* Azul info */
```

### Cores por Tipo de Animal
```css
--petx-dog: #6c63ff;          /* Cachorro */
--petx-cat: #ff6584;          /* Gato */
--petx-bird: #43aa8b;         /* Pássaro */
--petx-other: #f8961e;        /* Outros */
```

## 📱 Responsividade

### Breakpoints
- **Desktop**: > 992px (sidebar sempre visível)
- **Tablet**: 768px - 991px (sidebar colapsável)
- **Mobile**: < 768px (sidebar overlay)

### Comportamento Mobile
- Sidebar se transforma em overlay
- Header compacto
- Cards empilhados verticalmente
- Botões de ação em largura total

## 🧩 Componentes Modernos

### 1. Cards Modernos
```html
<div class="card modern-card">
    <div class="card-header">
        <h3 class="card-title">
            <i class="fas fa-icon me-2"></i>
            Título do Card
        </h3>
    </div>
    <div class="card-body">
        <!-- Conteúdo -->
    </div>
</div>
```

### 2. Estatísticas
```html
<div class="stat-card">
    <div class="stat-icon stat-primary">
        <i class="fas fa-paw"></i>
    </div>
    <div class="stat-content">
        <div class="stat-value">150</div>
        <div class="stat-label">Animais</div>
        <div class="stat-change positive">
            <i class="fas fa-arrow-up"></i>
            <span>+5%</span>
        </div>
    </div>
</div>
```

### 3. Ações Rápidas
```html
<a href="/link" class="quick-action-item">
    <div class="quick-action-icon primary">
        <i class="fas fa-plus"></i>
    </div>
    <div class="quick-action-content">
        <div class="quick-action-title">Nova Ação</div>
        <div class="quick-action-subtitle">Descrição</div>
    </div>
</a>
```

### 4. Formulários Modernos
```html
<div class="form-section">
    <div class="section-header">
        <h4 class="section-title">
            <i class="fas fa-icon me-2"></i>
            Seção do Formulário
        </h4>
        <p class="section-description">Descrição da seção</p>
    </div>
    <div class="form-group">
        <label class="form-label">Campo</label>
        <input class="form-control modern-input" type="text">
    </div>
</div>
```

## 🎯 Como Usar

### 1. Criando uma Nova Página
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      th:replace="~{layouts/base :: layout(~{::title}, ~{::section})}">
<head>
    <title>Título da Página - ONG PetX</title>
</head>
<body>
<section class="page-modern">
    <!-- Cabeçalho da página -->
    <div class="page-header">
        <div class="page-header-content">
            <div class="page-title">
                <h1>Título da Página</h1>
                <p class="text-muted">Descrição da página</p>
            </div>
            <div class="page-actions">
                <a href="/voltar" class="btn btn-outline-light">
                    <i class="fas fa-arrow-left me-2"></i>Voltar
                </a>
            </div>
        </div>
    </div>

    <!-- Conteúdo da página -->
    <div class="page-content">
        <!-- Seu conteúdo aqui -->
    </div>
</section>
</body>
</html>
```

### 2. Adicionando Itens ao Sidebar
Edite `fragmentos/sidebar.html` e adicione na seção apropriada:

```html
<li class="nav-item" sec:authorize="hasAnyRole('ADMIN', 'COLABORADOR')">
    <a class="nav-link" th:href="@{/nova-pagina}" 
       th:classappend="${#httpServletRequest.requestURI.startsWith('/nova-pagina') ? 'active' : ''}">
        <i class="nav-icon fas fa-icon"></i>
        <span class="nav-text">Nova Página</span>
        <span class="nav-badge" th:if="${contador != null}" th:text="${contador}">0</span>
    </a>
</li>
```

### 3. Personalizando Cores
Edite `css/estilo.css` e modifique as variáveis CSS:

```css
:root {
    --petx-primary: #sua-cor;
    --petx-secondary: #sua-cor;
    /* ... outras cores */
}
```

## 📁 Estrutura de Arquivos

```
templates/
├── layouts/
│   └── base.html              # Layout principal
├── fragmentos/
│   ├── sidebar.html           # Navegação lateral
│   ├── header.html            # Cabeçalho superior
│   └── messages.html          # Sistema de mensagens
├── exemplos/
│   └── formulario-moderno.html # Exemplo de formulário
└── index.html                 # Dashboard principal

static/css/
├── estilo.css                 # Variáveis e estilos base
├── layout-modern.css          # Layout moderno
├── dashboard-modern.css       # Dashboard específico
└── components.css             # Componentes gerais
```

## 🚀 Benefícios

### Para Desenvolvedores
- **Código organizado**: Estrutura clara e modular
- **Fácil manutenção**: Componentes reutilizáveis
- **Consistência**: Padrões uniformes em todo o sistema
- **Responsividade**: Funciona em todos os dispositivos

### Para Usuários
- **Interface moderna**: Visual atrativo e profissional
- **Navegação intuitiva**: Fácil de encontrar funcionalidades
- **Experiência fluida**: Animações e transições suaves
- **Acessibilidade**: Suporte a temas claro/escuro

## 🔧 Personalização

### Adicionando Novos Componentes
1. Crie o HTML do componente
2. Adicione os estilos CSS correspondentes
3. Documente o uso no README

### Modificando o Tema
1. Edite as variáveis CSS em `estilo.css`
2. Teste em ambos os temas (claro/escuro)
3. Verifique a responsividade

### Criando Novas Páginas
1. Use o template base
2. Siga o padrão de cabeçalho
3. Organize o conteúdo em seções
4. Teste em diferentes dispositivos

## 📞 Suporte

Para dúvidas ou sugestões sobre o layout:
1. Consulte este README
2. Verifique os exemplos na pasta `exemplos/`
3. Analise o código das páginas existentes
4. Entre em contato com a equipe de desenvolvimento

---

**Desenvolvido com ❤️ para a ONG PetX**
